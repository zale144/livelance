package livelance.app.web.resource;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.model.Deal;
import livelance.app.model.Profile;
import livelance.app.model.Service;

public class DealResource extends ResourceSupport {

	private Long rid;
	private double serviceCost;
	private double averageRating;
	private String description;
	private String longDescription;
	private ProfileResource profile;
	private ServiceResource service;
	private List<LocationResource> locations;

	public List<LocationResource> getLocations() {
		return locations;
	}

	public void setLocations(List<LocationResource> locations) {
		this.locations = locations;
	}

	public ServiceResource getService() {
		return service;
	}

	public void setService(ServiceResource service) {
		this.service = service;
	}

	public ProfileResource getProfile() {
		return profile;
	}

	public void setProfile(ProfileResource profile) {
		this.profile = profile;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public double getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(double serviceCost) {
		this.serviceCost = serviceCost;
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
	
	public Deal toDeal() {
		Deal deal = new Deal();
		Profile profile = this.profile.toProfile();
		Service service = this.service.toService();
		deal.setProfile(profile);
		deal.setService(service);
		deal.setServiceCost(serviceCost);
		deal.setDescription(description);
		deal.setLongDescription(longDescription);
		return deal;
	}

}
