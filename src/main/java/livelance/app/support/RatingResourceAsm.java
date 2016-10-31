package livelance.app.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.Rating;
import livelance.app.web.controller.ApiRatingController;
import livelance.app.web.resource.RatingResource;

@Component
public class RatingResourceAsm extends ResourceAssemblerSupport<Rating, RatingResource>{
	
	public RatingResourceAsm() {
		super(ApiRatingController.class, RatingResource.class);
    }

	public RatingResource toResource(Rating rating) {
		RatingResource res = new RatingResource(); 
		res.setComment(rating.getComment());
		res.setCustomerName(rating.getCustomerName());
		res.setRating(rating.getRating());
		res.setTimeOfRating(rating.getTimeOfRating());
		res.setRid(rating.getId());
		res.add(linkTo(methodOn(ApiRatingController.class).get(
				rating.getId(), rating.getDeal().getId())).withSelfRel());
		return res;
	}
}
