package de.nkdk.berlintripassistant;

public class Station {

	private int vbbNr;
	private int hafasNr;
	private String name;
	private LocationVector locationVector;

	public Station(String vbbNr, String hafasNr, String name, LocationVector locationVector) {
		this.vbbNr = Integer.valueOf(vbbNr);
		this.hafasNr = Integer.valueOf(hafasNr);
		this.name = name;
		
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



	@Override
	public String toString() {
		return super.toString() + getVbbNr() + " " + getHafasNr() + " ";
	}

	public LocationVector getLocationVector() {
		return locationVector;
	}

}
