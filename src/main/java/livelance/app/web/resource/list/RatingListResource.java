package livelance.app.web.resource.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.web.resource.RatingResource;

public class RatingListResource extends ResourceSupport {
	 private List<RatingResource> profiles = new ArrayList<RatingResource>();

	    public List<RatingResource> getRatings() {
	        return profiles;
	    }

	    public void setRatings(List<RatingResource> profiles) {
	        this.profiles = profiles;
	    }
}
