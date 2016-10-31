package livelance.app.web.resource.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.web.resource.DealResource;

public class DealListResource extends ResourceSupport {
	 private List<DealResource> deals = new ArrayList<DealResource>();

	    public List<DealResource> getDeals() {
	        return deals;
	    }

	    public void setDeals(List<DealResource> deals) {
	        this.deals = deals;
	    }
}
