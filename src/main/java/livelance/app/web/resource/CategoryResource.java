package livelance.app.web.resource;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.model.Category;

public class CategoryResource extends ResourceSupport {

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
	
	public Category toCategory() {
		Category category = new Category();
		category.setName(name);
		return category;
	}

}
