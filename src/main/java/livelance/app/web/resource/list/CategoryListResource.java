package livelance.app.web.resource.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.web.resource.CategoryResource;

public class CategoryListResource extends ResourceSupport {
	 private List<CategoryResource> categories = new ArrayList<CategoryResource>();

	    public List<CategoryResource> getCategories() {
	        return categories;
	    }

	    public void setCategories(List<CategoryResource> categories) {
	        this.categories = categories;
	    }
}
