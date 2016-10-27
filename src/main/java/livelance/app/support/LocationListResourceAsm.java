package livelance.app.support;

import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.list.LocationList;
import livelance.app.web.controller.ApiLocationController;
import livelance.app.web.resource.LocationResource;
import livelance.app.web.resource.list.LocationListResource;

@Component
public class LocationListResourceAsm  extends ResourceAssemblerSupport<LocationList, LocationListResource> {


    public LocationListResourceAsm() {
        super(ApiLocationController.class, LocationListResource.class);
    }

    @Override
    public LocationListResource toResource(LocationList accountList) {
        List<LocationResource> resList = new LocationResourceAsm().toResources(accountList.getLocations());
        LocationListResource finalRes = new LocationListResource();
        finalRes.setLocations(resList);
        return finalRes;
    }

}
