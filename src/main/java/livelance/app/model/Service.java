package livelance.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Service {

	@Id
	@GeneratedValue
	private Long id;
	
	@OneToMany(cascade = CascadeType.REMOVE, 
			fetch = FetchType.EAGER, mappedBy = "service")
	private List<Deal> deals = new ArrayList<>();

	@NotNull
	private String name;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	private Category category;

	public void addDeal(Deal newDeal) {
		if(newDeal != null && !deals.contains(newDeal)) {
			deals.add(newDeal);
		}
	}
	
	public void removeDeal(Deal toRemove) {
		if(toRemove != null && deals.contains(toRemove)) {
			deals.remove(toRemove);
		}
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Deal> getDeals() {
		return deals;
	}

	public void setDeals(List<Deal> deals) {
		this.deals = deals;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
		if(category!=null && !category.getServices().contains(this)){
			category.getServices().add(this);
		}
	}

}
