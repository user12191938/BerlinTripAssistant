package de.nkdk.berlintripassistant;

public class LocationVector
{
	private double lon;
	private double lat;
	private double currentDistance;

	public double getlon() {
		return lon;
	}

	public double getlat() {
		return lat;
	}
	
	public void estimateCurrentDistance(double locallat, double locallon) 
	{
//		this.currentDistance=Math.sqrt(Math.pow(Math.abs(localX - this.getX()), 2)+Math.pow(Math.abs(localY - this.getY()), 2));
		double dx;
		double dy;
		double lat;
		
		lat = (((this.lat+locallat)/2) * 0.01745);		
		dx = 111.3* Math.cos(lat) * (this.lon-locallon);
		dy = 111.3 * (this.lat-locallat);
		
		double distance = Math.sqrt(dx * dx + dy * dy)*1000;
		this.currentDistance=distance;
	}
	
	public double getCurrentDistance()
	{
		return this.currentDistance;
	}

	public LocationVector(String lat, String lon) 
	{
		this.lon = (Double.parseDouble(lon)/1000000);
		this.lat = (Double.parseDouble(lat)/1000000);
	}
	
	public void printLocationvector()
	{
		System.out.println(this.currentDistance);
	}
}
