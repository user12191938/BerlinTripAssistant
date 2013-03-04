package de.nkdk.berlintripassistant.Activities;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapViewActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapview);
		MainActivity.instance.INSTANCE.setMapView((MapView) findViewById(R.id.mapview));

		GeoPoint p = new GeoPoint((int) (MainActivity.instance.INSTANCE.getCurrentLocation().getLatitude() * 1000000),
				(int) (MainActivity.instance.INSTANCE.getCurrentLocation().getLongitude() * 1000000));
		MapController mapController = MainActivity.instance.INSTANCE.getMapView().getController();
		mapController.animateTo(p);
		mapController.setZoom(17);
		
		MainActivity.instance.INSTANCE.getStationHandler().updateMapView();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
