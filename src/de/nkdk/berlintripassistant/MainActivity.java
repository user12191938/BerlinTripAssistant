package de.nkdk.berlintripassistant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity
{
	// SINGLETON
	public enum instance
	{      //test
		INSTANCE;
		private LinkedHashMap<Integer, Station> stations;
		private ArrayList<Station> stationsInVicinity = new ArrayList<Station>();
		private GPS gpsHandler;

		// Aenderung
		public LinkedHashMap<Integer, Station> getStations()
		{
			return stations;
		}

		public void setStations(LinkedHashMap<Integer, Station> stations)
		{
			this.stations = stations;
		}

		public ArrayList<Station> getStationsInVicinity()
		{
			return stationsInVicinity;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try
		{
			instance.INSTANCE.setStations(readStations());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		long a = System.currentTimeMillis();
		int vicinity = 0;
		instance.INSTANCE.gpsHandler.initializeGPS();
		for (Entry<Integer, Station> entry : instance.INSTANCE.getStations()
				.entrySet())
		{
			LocationVector curLocVec = entry.getValue().getLocationVector();

			curLocVec.estimateCurrentDistance(instance.INSTANCE.gpsHandler
					.getLocation().getLatitude(), instance.INSTANCE.gpsHandler
					.getLocation().getLongitude());

			double distance = curLocVec.getCurrentDistance();

			if (distance < 1000)
			{
				System.out.println(distance + " " + entry.getValue().getName());
				vicinity++;
				instance.INSTANCE.getStationsInVicinity().add(entry.getValue());
			}
		}
		System.out.println("DONE: in " + (System.currentTimeMillis() - a)
				+ " Haltestellen in der Nähe: " + vicinity);

		// Cache-Liste
		for (Station station : instance.INSTANCE.getStationsInVicinity())
		{
			System.out.println(station.toString());
		}

	}

	public LinkedHashMap<Integer, Station> readStations() throws IOException
	{

		LinkedHashMap<Integer, Station> stations = new LinkedHashMap<Integer, Station>();
		InputStream stream = getAssets().open(
				"VBB_Haltestellen_Entwicklertag.csv");

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));

		String c;
		reader.readLine();
		while ((c = reader.readLine()) != null)
		{

			String[] splittedData = c.split(";");
			try
			{
				if (!(splittedData[3].contentEquals("") && splittedData[4]
						.contentEquals("")))
				{
					stations.put(Integer.parseInt(splittedData[1]),
							new Station(splittedData[0], splittedData[1],
									splittedData[2], new LocationVector(
											splittedData[5], splittedData[6])));
				}
			} catch (Exception e)
			{
			}
		}
		return stations;
	}

}
