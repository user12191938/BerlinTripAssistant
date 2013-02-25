package de.nkdk.berlintripassistant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	// SINGLETON
	public enum instance {
		INSTANCE;
		private LinkedHashMap<Integer, Station> stations;

		public LinkedHashMap<Integer, Station> getStations() {
			return stations;
		}

		public void setStations(LinkedHashMap<Integer, Station> stations) {
			this.stations = stations;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			instance.INSTANCE.setStations(readStations());
		} catch (IOException e) {
			e.printStackTrace();
		}
		long a = System.currentTimeMillis();
		for (Entry<Integer, Station> entry : instance.INSTANCE.getStations().entrySet()) {

		System.out.println(getDistance(instance.INSTANCE.getStations().get(9415010).getLocationVector(), entry.getValue()
					.getLocationVector()));
		}
		System.out.println("DONE: in " + (System.currentTimeMillis() - a) + " Millis");

	}
 
	public LinkedHashMap<Integer, Station> readStations() throws IOException {

		LinkedHashMap<Integer, Station> stations = new LinkedHashMap<Integer, Station>();
		InputStream stream = getAssets().open("VBB_Haltestellen_Entwicklertag.csv");

		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String c;
		reader.readLine();
		while ((c = reader.readLine()) != null) {

			String[] splittedData = c.split(";");
			try {
				if (!(splittedData[3].contentEquals("") && splittedData[4].contentEquals(""))) {
					stations.put(Integer.parseInt(splittedData[1]), new Station(splittedData[0], splittedData[1],
							splittedData[2], splittedData[5], splittedData[6], new LocationVector(splittedData[3],
									splittedData[4])));
				}
			} catch (Exception e) {
			}
		}
		return stations;
	}

	public double getDistance(LocationVector lV1, LocationVector lV2) {
		return Math.sqrt(Math.pow(Math.abs(lV1.getX() - lV2.getX()), 2)
				+ Math.pow(Math.abs(lV1.getY() - lV2.getY()), 2));
	}
}
