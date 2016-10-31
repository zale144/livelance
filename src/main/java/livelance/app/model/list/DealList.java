package livelance.app.model.list;

import java.util.ArrayList;
import java.util.List;

import livelance.app.model.Deal;

public class DealList {
	   private List<Deal> deals = new ArrayList<Deal>();

	    public DealList(List<Deal> list) {
	        this.deals = list;
	    }

	    public List<Deal> getDeals() {
	        return deals;
	    }

	    public void setDeals(List<Deal> deals) {
	        this.deals = deals;
	    }
}
