package livelance.app.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.Category;
import livelance.app.web.controller.ApiCategoryController;
import livelance.app.web.controller.ApiServiceController;
import livelance.app.web.resource.CategoryResource;

@Component
public class CategoryResourceAsm extends ResourceAssemblerSupport<Category, CategoryResource>{
	
	public CategoryResourceAsm() {
		super(ApiCategoryController.class, CategoryResource.class);
    }

	public CategoryResource toResource(Category category) {
		CategoryResource res = new CategoryResource();  
		res.setName(category.getName());
		res.setRid(category.getId());
		res.add(linkTo(methodOn(ApiCategoryController.class).get(category.getId())).withSelfRel());
		res.add(linkTo(methodOn(ApiServiceController.class).getAll(category.getId())).withRel("services"));
		return res;
	}
}
