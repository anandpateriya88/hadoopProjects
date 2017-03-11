package com.arch.domainobjects;

public class City {
	private String city;

	public City(){
		
	}
	
	public City(String city){
		this.city=city;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof City) {
			City toCompare = (City) o;
			return this.city.equalsIgnoreCase(toCompare.getCity());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return city.hashCode();
	}
}
