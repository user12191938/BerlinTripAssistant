package de.nkdk.berlintripassistant;

public class LocationVector {
	private double lon;
	private double lat;
	private double currentDistance;

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}

	public void estimateCurrentDistance(double locaLat, double localLon) {
		double dx;
		double dy;

		dx = 111.3 * Math.cos((((this.lat + locaLat) / 2) * 0.01745)) * (this.lon - localLon);
		dy = 111.3 * (this.lat - locaLat);

		this.currentDistance = Math.sqrt(dx * dx + dy * dy) * 1000;
	}

	public double getCurrentDistance() {
		return this.currentDistance;
	}

	public LocationVector(String lat, String lon) {
		this.lon = (Double.parseDouble(lon) / 1000000);
		this.lat = (Double.parseDouble(lat) / 1000000);
	}

	public void printLocationvector() {
		System.out.println(this.currentDistance);
	}
}
