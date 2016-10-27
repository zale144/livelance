package livelance.app.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.Service;
import livelance.app.web.controller.ApiServiceController;
import livelance.app.web.resource.ServiceResource;

@Component
public class ServiceResourceAsm extends ResourceAssemblerSupport<Service, ServiceResource>{
	
	@Bean 
    public ServiceResourceAsm asm(){
      return new ServiceResourceAsm();
    }
	
	public ServiceResourceAsm() {
		super(ApiServiceController.class, ServiceResource.class);
    }

	public ServiceResource toResource(Service service) {
		ServiceResource res = new ServiceResource(); 
		res.setName(service.getName());
		res.setRid(service.getId());
		res.add(linkTo(methodOn(ApiServiceController.class).get(
				service.getId(), service.getCategory().getId())).withSelfRel());
		return res;
	}
	
}
