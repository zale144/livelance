package livelance.app.support;

import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.list.DealList;
import livelance.app.web.controller.ApiDealController;
import livelance.app.web.resource.DealResource;
import livelance.app.web.resource.list.DealListResource;

@Component
public class DealListResourceAsm extends ResourceAssemblerSupport<DealList, DealListResource> {


    public DealListResourceAsm() {
        super(ApiDealController.class, DealListResource.class);
    }

    @Override
    public DealListResource toResource(DealList accountList) {
        List<DealResource> resList = new DealResourceAsm().toResources(accountList.getDeals());
        DealListResource finalRes = new DealListResource();
        finalRes.setDeals(resList);
        return finalRes;
    }
}
