package livelance.app.support;

import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.list.RatingList;
import livelance.app.web.controller.ApiRatingController;
import livelance.app.web.resource.RatingResource;
import livelance.app.web.resource.list.RatingListResource;

@Component
public class RatingListResourceAsm  extends ResourceAssemblerSupport<RatingList, RatingListResource> {


    public RatingListResourceAsm() {
        super(ApiRatingController.class, RatingListResource.class);
    }

    @Override
    public RatingListResource toResource(RatingList accountList) {
        List<RatingResource> resList = new RatingResourceAsm().toResources(accountList.getRatings());
        RatingListResource finalRes = new RatingListResource();
        finalRes.setRatings(resList);
        return finalRes;
    }
}
