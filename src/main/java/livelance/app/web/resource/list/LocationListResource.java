package livelance.app.web.resource.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.web.resource.LocationResource;

public class LocationListResource extends ResourceSupport {
	 private List<LocationResource> locations = new ArrayList<LocationResource>();

	    public List<LocationResource> getLocations() {
	        return locations;
	    }

	    public void setLocations(List<LocationResource> locations) {
	        this.locations = locations;
	    }
}
