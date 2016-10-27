package livelance.app.web.resource;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.model.Location;

public class LocationResource extends ResourceSupport {

	private Long rid;
	private String country;
	private String city;
	private String street;
	private String number;
	private double longitude;
	private double latitude;

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public Location toLocation() {
		Location location = new Location();
		location.setId(rid);
		location.setCountry(country);
		location.setCity(city);
		location.setNumber(number);
		location.setStreet(street);
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		return location;
	}
}
