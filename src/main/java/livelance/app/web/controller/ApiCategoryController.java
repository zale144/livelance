package livelance.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import livelance.app.model.Category;
import livelance.app.model.list.CategoryList;
import livelance.app.service.CategoryService;
import livelance.app.support.CategoryListResourceAsm;
import livelance.app.support.CategoryResourceAsm;
import livelance.app.web.resource.CategoryResource;

@CrossOrigin
@RestController
@RequestMapping("/api/categories")
public class ApiCategoryController {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	CategoryResourceAsm asm;
	
	@Autowired
	CategoryListResourceAsm asmList;

	// GET ALL
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoryResource>> getAll() {

		CategoryList entities = categoryService.findAll();

		return new ResponseEntity<>(asmList.toResource(entities).getCategories(), HttpStatus.OK);
	}

	// GET ONE
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CategoryResource> get(@PathVariable Long id) {

		Category found = categoryService.findOne(id);
		if (found == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(asm.toResource(found), HttpStatus.OK);
	}

	// DELETE
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<CategoryResource> delete(@PathVariable Long id) {

		Category deleted = categoryService.delete(id);
		if (deleted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ADD
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<CategoryResource> add(
			@RequestBody CategoryResource newEntity) {
		
		Category category = newEntity.toCategory();
		
		Category persisted = categoryService.save(category);

		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.CREATED);
	}

	// EDIT
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<CategoryResource> edit(
			@PathVariable Long id, @RequestBody CategoryResource newEntity) {

		Category category = newEntity.toCategory();
		category.setId(id);
		
		Category persisted = categoryService.save(category);

		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.OK);
	}

}
