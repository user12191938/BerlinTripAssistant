package de.nkdk.berlintripassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class GPS extends Activity implements LocationListener
{

	protected static final CharSequence GPSON = null;
	protected static final CharSequence GPSOUT = null;
	private static LocationManager locationManager;
	private static String provider;
	private static Location location;

	LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
	boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

	public void initializeGPS()
	{
		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings
		if (!enabled)
		{
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(provider);

		locationManager.requestLocationUpdates(provider, 4000, 1, this);

		if (getLocation() != null)
		{

			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(getLocation());
		} else
		{
			System.out.println("Location not available");
			System.out.println("Location not available");
		}
	}

	protected void onResume()
	{
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1f, this);
	}

	protected void onPause()
	{
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		double lat = (location.getLatitude());
		double lng = (location.getLongitude());
		System.out.println("Latitude: " + String.valueOf(lat));
		System.out.println("Longitutde: " + String.valueOf(lng));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider)
	{
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider)
	{
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	public Location getLocation()
	{
		return location;
	}

}
