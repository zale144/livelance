package livelance.app.support;

import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.list.CategoryList;
import livelance.app.web.controller.ApiCategoryController;
import livelance.app.web.resource.CategoryResource;
import livelance.app.web.resource.list.CategoryListResource;

@Component
public class CategoryListResourceAsm  extends ResourceAssemblerSupport<CategoryList, CategoryListResource> {


    public CategoryListResourceAsm() {
        super(ApiCategoryController.class, CategoryListResource.class);
    }

    @Override
    public CategoryListResource toResource(CategoryList accountList) {
        List<CategoryResource> resList = new CategoryResourceAsm().toResources(accountList.getCategories());
        CategoryListResource finalRes = new CategoryListResource();
        finalRes.setCategories(resList);
        return finalRes;
    }

}
