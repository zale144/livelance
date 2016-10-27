package livelance.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Category {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	private String name;
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.REMOVE,
			fetch = FetchType.EAGER,mappedBy = "category")
	private List<Service> services = new ArrayList<>();

	public void addService(Service newService) {
		if(newService != null && !services.contains(newService)) {
			services.add(newService);
		}
	}
	
	public void removeService(Service toRemove) {
		if(toRemove != null && services.contains(toRemove)) {
			services.remove(toRemove);
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
}
