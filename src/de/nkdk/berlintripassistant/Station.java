package de.nkdk.berlintripassistant;

public class Station {

	private int vbbNr;
	private int hafasNr;
	private String name;
	private float lat;
	private float lon;
	private LocationVector locationVector;

	public Station(String vbbNr, String hafasNr, String name, String lat, String lon, LocationVector locationVector) {
		this.vbbNr = Integer.valueOf(vbbNr);
		this.hafasNr = Integer.valueOf(hafasNr);
		this.name = name;
		this.lat = Float.parseFloat(lat);
		this.lon = Float.parseFloat(lon);
		this.locationVector = locationVector;
	}

	public int getVbbNr() {
		return vbbNr;
	}

	public void setVbbNr(int vbbNr) {
		this.vbbNr = vbbNr;
	}

	public int getHafasNr() {
		return hafasNr;
	}

	public void setHafasNr(int hafasNr) {
		this.hafasNr = hafasNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return super.toString() + getVbbNr() + " " + getHafasNr() + " " + getLat() + " " + getLon();
	}

	public LocationVector getLocationVector() {
		return locationVector;
	}

}
