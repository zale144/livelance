package livelance.app.web.resource.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.web.resource.ProfileResource;

public class ProfileListResource extends ResourceSupport {
	 private List<ProfileResource> profiles = new ArrayList<ProfileResource>();

	    public List<ProfileResource> getProfiles() {
	        return profiles;
	    }

	    public void setProfiles(List<ProfileResource> profiles) {
	        this.profiles = profiles;
	    }
}
