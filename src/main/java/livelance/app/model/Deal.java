package livelance.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Deal {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Profile profile;

	@ManyToOne(fetch = FetchType.EAGER)
	private Service service;

	@NotNull
	private double serviceCost;

	@OneToMany(cascade = CascadeType.REMOVE,
			fetch = FetchType.LAZY, mappedBy = "deal")
	private List<Rating> ratings = new ArrayList<>();

	@OneToMany(cascade = CascadeType.REMOVE, 
			fetch = FetchType.EAGER, mappedBy = "deal")
	private List<Location> locations = new ArrayList<>();

	private String description;
	
	private String longDescription;

	public void addLocation(Location newLocation) {
		if (newLocation != null && !locations.contains(newLocation)) {
			locations.add(newLocation);
		}
	}

	public void removeLocation(Location toRemove) {
		if (toRemove != null && locations.contains(toRemove)) {
			locations.remove(toRemove);
		}
	}

	public void addRating(Rating newRating) {
		if (newRating != null && !ratings.contains(newRating)) {
			ratings.add(newRating);
		}
	}

	public void removeRating(Rating toRemove) {
		if (toRemove != null && ratings.contains(toRemove)) {
			ratings.remove(toRemove);
		}
	}

	public double getAverageRating() {
		int sumOfRatings = 0;
		double averageRating = 0;
		if (ratings.size() > 0) {
			for (Rating r : ratings) {
				sumOfRatings += r.getRating();
			}
			averageRating = (double) sumOfRatings / ratings.size();
			averageRating = (double) (Math.round(averageRating * 100)) / 100; 
		}
		return averageRating;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
		if(profile!=null && !profile.getDeals().contains(this)){
			profile.getDeals().add(this);
		}
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public double getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(double serviceCost) {
		this.serviceCost = serviceCost;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

}
