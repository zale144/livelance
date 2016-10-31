package livelance.app.model;

public class Circle {

	private Double latitude;
	private Double longitude;
	private Double radius;

	public Circle() {
		super();
	}

	public Circle(Double latitude, Double longitude, Double radius) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

}
