package livelance.app.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.Profile;
import livelance.app.web.controller.ApiDealController;
import livelance.app.web.controller.ApiProfileController;
import livelance.app.web.resource.ProfileResource;

@Component
public class ProfileResourceAsm extends ResourceAssemblerSupport<Profile, ProfileResource>{
	
	public ProfileResourceAsm() {
		super(ApiProfileController.class, ProfileResource.class);
    }

	public ProfileResource toResource(Profile profile) {
		ProfileResource res = new ProfileResource();  
		res.setFirstname(profile.getFirstname());
		res.setLastname(profile.getLastname());
		res.setDateOfRegistration(profile.getDateOfRegistration());
		res.setEmail(profile.getEmail());
		res.setPhoneNumber(profile.getPhoneNumber());
		res.setRid(profile.getId());
		res.setPictureLink(profile.getPictureLink());
		res.setWebsite(profile.getWebsite());
		res.setFacebook(profile.getFacebook());
		res.setAbout(profile.getAbout());
		res.add(linkTo(methodOn(ApiProfileController.class).get(profile.getId())).withSelfRel());
		res.add(linkTo(methodOn(ApiDealController.class).getAll(profile.getId(), null, null, null, null)).withRel("deals"));
		return res;
	}
}
