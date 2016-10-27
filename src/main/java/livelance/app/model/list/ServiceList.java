package livelance.app.model.list;

import java.util.ArrayList;
import java.util.List;

import livelance.app.model.Service;

public class ServiceList {
	   private List<Service> services = new ArrayList<Service>();

	    public ServiceList(List<Service> list) {
	        this.services = list;
	    }

	    public List<Service> getServices() {
	        return services;
	    }

	    public void setService(List<Service> services) {
	        this.services = services;
	    }
}
