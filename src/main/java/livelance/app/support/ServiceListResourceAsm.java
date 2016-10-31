package livelance.app.support;

import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.list.ServiceList;
import livelance.app.web.controller.ApiServiceController;
import livelance.app.web.resource.ServiceResource;
import livelance.app.web.resource.list.ServiceListResource;

@Component
public class ServiceListResourceAsm  extends ResourceAssemblerSupport<ServiceList, ServiceListResource> {


    public ServiceListResourceAsm() {
        super(ApiServiceController.class, ServiceListResource.class);
    }

    @Override
    public ServiceListResource toResource(ServiceList accountList) {
        List<ServiceResource> resList = new ServiceResourceAsm().toResources(accountList.getServices());
        ServiceListResource finalRes = new ServiceListResource();
        finalRes.setServices(resList);
        return finalRes;
    }
}
