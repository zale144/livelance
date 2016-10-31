package livelance.app.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.Location;
import livelance.app.web.controller.ApiLocationController;
import livelance.app.web.resource.LocationResource;

@Component
public class LocationResourceAsm extends ResourceAssemblerSupport<Location, LocationResource>{
	
	public LocationResourceAsm() {
		super(ApiLocationController.class, LocationResource.class);
    }

	public LocationResource toResource(Location location) {
		LocationResource res = new LocationResource();  
		res.setCountry(location.getCountry());
		res.setCity(location.getCity());
		res.setStreet(location.getStreet());
		res.setNumber(location.getNumber());
		res.setLatitude(location.getLatitude());
		res.setLongitude(location.getLongitude());
		res.setRid(location.getId());
		res.add(linkTo(methodOn(ApiLocationController.class).
				get(location.getId(), location.getDeal().getId())).withSelfRel());
		return res;
	}
}
