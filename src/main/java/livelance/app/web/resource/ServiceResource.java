package livelance.app.web.resource;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.model.Service;

public class ServiceResource extends ResourceSupport {

	private Long rid;
	private String name;

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Service toService() {
		Service service = new Service();
		service.setId(rid);
		service.setName(name);
		return service;
	}
}
