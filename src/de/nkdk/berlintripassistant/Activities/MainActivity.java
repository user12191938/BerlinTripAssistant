package de.nkdk.berlintripassistant.Activities;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import de.nkdk.berlintripassistant.StationHandler;

public class MainActivity extends MapActivity implements LocationListener {
	public enum instance {
		INSTANCE;
		private StationHandler stationHandler;
		private Location currentLocation;
		private MapView mapView;
		private boolean isActive = true;

		public StationHandler getStationHandler() {
			return stationHandler;
		}

		public void setStationHandler(StationHandler stationHandler) {
			this.stationHandler = stationHandler;
		}

		public Location getCurrentLocation() {
			return currentLocation;
		}

		public void setCurrentLocation(Location currentLocation) {
			this.currentLocation = currentLocation;
		}

		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}

		public MapView getMapView() {
			return mapView;
		}

		public void setMapView(MapView mapView) {
			this.mapView = mapView;
			mapView.setBuiltInZoomControls(true);
			mapView.setMinimumWidth(200);
		}
	}

	private LocationManager locationManager;
	private String provider;
	private Location location;
	private LocationManager service;
	private int exit = 0;
	

	// TODO Temporary implementation of radius // Temporary Static Context
	public static SeekBar radius;
	public TextView radiusText;
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;

		// TODO Temporary implementation of radius
		radius = (SeekBar) findViewById(R.id.seekSetRadius);
		radius.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				radiusText.setText(String.valueOf("Current Value: " + progress));
			}
		});
		radiusText = (TextView) findViewById(R.id.textViewRadius);
		radiusText.setText(String.valueOf("Current Value: " + radius.getProgress()));

		if (initializeGPSSignal())
			instance.INSTANCE.setStationHandler(new StationHandler(getAssets(), getResources()));
	}

	public void onLocationChanged(Location location) {
		instance.INSTANCE.setCurrentLocation(location);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
	}

	public boolean initializeGPSSignal() {
		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings

		service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean gpsEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(provider);

		locationManager.requestLocationUpdates(provider, 4000, 1, this);

		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);
			return true;
		} else {
			System.out.println("Location not available");
			return false;
		}
	}

	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1f, this);
	}

	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	protected void onDestroy() {
		instance.INSTANCE.setActive(false);
		super.onDestroy();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnToActivityMap:
			Intent intent = new Intent(MainActivity.this, MapViewActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (exit == 1) {
			this.finish();
		} else {
			Thread timer = new Thread(new Runnable() {
				public void run() {
					Handler refresh = new Handler(Looper.getMainLooper());
					refresh.post(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), "Press again to exit..", Toast.LENGTH_SHORT).show();
						}
					});
					exit++;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					exit = 0;
				}
			});
			timer.start();
		}
	}
}
