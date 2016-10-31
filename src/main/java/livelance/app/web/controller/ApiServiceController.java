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
import livelance.app.model.Service;
import livelance.app.model.list.ServiceList;
import livelance.app.service.CategoryService;
import livelance.app.service.ServiceService;
import livelance.app.support.ServiceListResourceAsm;
import livelance.app.support.ServiceResourceAsm;
import livelance.app.web.resource.ServiceResource;

@CrossOrigin
@RestController
@RequestMapping("/api/categories/{catId}/services")
public class ApiServiceController {

	@Autowired
	ServiceService serviceService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ServiceResourceAsm asm;

	@Autowired
	ServiceListResourceAsm asmList;

	// GET ALL
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ServiceResource>> getAll(@PathVariable Long catId) {

		// HttpHeaders httpHeaders = new HttpHeaders();

		ServiceList services = serviceService.findAll(catId);

		return new ResponseEntity<>(asmList.toResource(services).getServices(), HttpStatus.OK);
	}

	// GET ONE
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ServiceResource> get(@PathVariable Long id, @PathVariable Long catId) {

		Service found = serviceService.findOne(id, catId);
		if (found == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(asm.toResource(found), HttpStatus.OK);
	}

	// DELETE
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ServiceResource> delete(@PathVariable Long id, 
			@PathVariable Long catId) {

		Service deleted = serviceService.delete(id, catId);
		if (deleted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ADD
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<ServiceResource> add(@PathVariable Long catId, @RequestBody ServiceResource newEntity) {

		Service service = newEntity.toService();
		Category category = categoryService.findOne(catId);
		service.setCategory(category);
		Service persisted = serviceService.save(service);

		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.CREATED);
	}

	// EDIT
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<ServiceResource> edit(@PathVariable Long catId, @PathVariable Long id,
			@RequestBody ServiceResource editEntity) {
		Service service = editEntity.toService();
		service.setId(id);
		Category category = categoryService.findOne(catId);
		service.setCategory(category);
		Service persisted = serviceService.save(service);

		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.OK);
	}

}
