package livelance.app.model.list;

import java.util.ArrayList;
import java.util.List;

import livelance.app.model.Category;

public class CategoryList {
    private List<Category> categories = new ArrayList<Category>();

    public CategoryList(List<Category> list) {
        this.categories = list;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategory(List<Category> categories) {
        this.categories = categories;
    }
}
