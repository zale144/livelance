package livelance.app.web.resource.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.web.resource.ServiceResource;

public class ServiceListResource extends ResourceSupport {
	 private List<ServiceResource> services = new ArrayList<ServiceResource>();

	    public List<ServiceResource> getServices() {
	        return services;
	    }

	    public void setServices(List<ServiceResource> services) {
	        this.services = services;
	    }
}
