package de.nkdk.berlintripassistant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import de.nkdk.berlintripassistant.Activities.MainActivity;

public class StationHandler {

	private AssetManager assetManager;
	private Resources resources;

	private LinkedHashMap<Integer, Station> stations;
	private ArrayList<Station> stationsInVicinity = new ArrayList<Station>();
	private ArrayList<Station> accessibleStations = new ArrayList<Station>();
	Thread updater = new Thread(new Updater());;

	public StationHandler(AssetManager assetManager, Resources resources) {
		this.resources = resources;
		this.assetManager = assetManager;
		try {
			this.stations = readStations();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getCurrentStationsInVicinity() {
		int vicinity = 0;
		ArrayList<Station> tempStations = new ArrayList<Station>();
		for (Entry<Integer, Station> entry : getStations().entrySet()) {
			LocationVector curLocVec = entry.getValue().getLocationVector();

			curLocVec.estimateCurrentDistance(MainActivity.instance.INSTANCE.getCurrentLocation().getLatitude(),
					MainActivity.instance.INSTANCE.getCurrentLocation().getLongitude());

			double distance = curLocVec.getCurrentDistance();
			// 1000 = 1km
			if (distance < 1000) {
				System.out.println(distance + " " + entry.getValue().getName());
				vicinity++;
				tempStations.add(entry.getValue());
			}
		}
		System.out.println("DONE. Haltestellen in der Nähe: " + vicinity);
		stationsInVicinity = tempStations;
	}

	public LinkedHashMap<Integer, Station> readStations() throws IOException {

		LinkedHashMap<Integer, Station> stations = new LinkedHashMap<Integer, Station>();
		InputStream stream = assetManager.open("VBB_Haltestellen_Entwicklertag.csv");

		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String c;
		reader.readLine();
		while ((c = reader.readLine()) != null) {

			String[] splittedData = c.split(";");
			try {
				if (!(splittedData[3].contentEquals("") && splittedData[4].contentEquals(""))) {
					stations.put(Integer.parseInt(splittedData[1]), new Station(splittedData[0], splittedData[1],
							splittedData[2], new LocationVector(splittedData[5], splittedData[6])));
				}
			} catch (Exception e) {
			}
		}
		updater.start();
		return stations;
	}

	public LinkedHashMap<Integer, Station> getStations() {
		return stations;
	}

	public void setStations(LinkedHashMap<Integer, Station> stations) {
		this.stations = stations;
	}

	public ArrayList<Station> getStationsInVicinity() {
		return stationsInVicinity;
	}

	public void updateMapView() {

		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {
				GeoPoint geopoint = new GeoPoint((int) (MainActivity.instance.INSTANCE.getCurrentLocation()
						.getLatitude() * 1000000), (int) (MainActivity.instance.INSTANCE.getCurrentLocation()
						.getLongitude() * 1000000));

				List<Overlay> listOfOverlays = MainActivity.instance.INSTANCE.getMapView().getOverlays();
				listOfOverlays.clear();
				MapOverlay mapOverlay = new MapOverlay(resources, geopoint);
				listOfOverlays.add(mapOverlay);
				Log.e("User", geopoint.getLatitudeE6() + "   " + geopoint.getLongitudeE6());
				for (Station station : stationsInVicinity) {
					mapOverlay = new MapOverlay(resources, new GeoPoint(
							(int) (station.getLocationVector().getLat() * 1000000), (int) (station.getLocationVector()
									.getLon() * 1000000)));
					listOfOverlays.add(mapOverlay);
					Log.e(station.getName(), (int) (station.getLocationVector().getLat() * 1000000) + "   "
							+ (int) (station.getLocationVector().getLon() * 1000000));
				}
				MainActivity.instance.INSTANCE.getMapView().invalidate();

			}
		});

	}

	private class Updater implements Runnable {
		
		@Override
		public void run() {
			
			while (MainActivity.instance.INSTANCE.isActive()) {
				getCurrentStationsInVicinity();
				setAccessibleStations();

				if (MainActivity.instance.INSTANCE.getMapView() != null)
					updateMapView();
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
				}

				// TODO temporary context
				Handler handler = new Handler(Looper.getMainLooper());
				if (accessibleStations.size() != 0) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							StringBuilder builder = new StringBuilder();
							for (Station station : accessibleStations) {
								builder.append(station.getName() + "; ");
							}
							AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.context);
							alert.setTitle("ACHUNG!");
							alert.setMessage(builder.toString() + "\n");
							alert.setPositiveButton("Glueckwunsch!", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									return;
								}
							});
							alert.show();
						}
					});
				}
			}
		}
	}

	private void setAccessibleStations() {

		ArrayList<Station> stations = new ArrayList<Station>();
		for (Station station : stationsInVicinity) {
			if (station.getLocationVector().getCurrentDistance() < MainActivity.radius.getProgress()) {
				stations.add(station);
			}
		}
		accessibleStations = stations;
	}
}
