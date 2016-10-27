package livelance.app.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.Deal;
import livelance.app.web.controller.ApiDealController;
import livelance.app.web.controller.ApiLocationController;
import livelance.app.web.controller.ApiProfileController;
import livelance.app.web.controller.ApiRatingController;
import livelance.app.web.controller.ApiServiceController;
import livelance.app.web.resource.DealResource;

@Component
public class DealResourceAsm extends ResourceAssemblerSupport<Deal, DealResource>{
	
	public DealResourceAsm() {
		super(ApiDealController.class, DealResource.class);
    }

	public DealResource toResource(Deal deal) {
		DealResource res = new DealResource();  
		res.setDescription(deal.getDescription());
		res.setServiceCost(deal.getServiceCost());
		res.setAverageRating(deal.getAverageRating());
		res.setLongDescription(deal.getLongDescription());
		res.setRid(deal.getId());
		res.add(linkTo(methodOn(ApiDealController.class).get(deal.getId())).withSelfRel());
		res.add(linkTo(methodOn(ApiLocationController.class).getAll(deal.getId())).withRel("locations"));
		res.add(linkTo(methodOn(ApiRatingController.class).getAll(deal.getId())).withRel("ratings"));
		res.add(linkTo(methodOn(ApiProfileController.class).get(deal.getProfile().getId())).withRel("profile"));
		res.add(linkTo(methodOn(ApiServiceController.class).get(
				deal.getService().getId(), deal.getService().getCategory().getId())).withRel("service"));
		return res;
	}
}
