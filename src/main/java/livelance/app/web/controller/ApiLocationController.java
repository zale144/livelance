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

import livelance.app.model.Deal;
import livelance.app.model.Location;
import livelance.app.model.list.LocationList;
import livelance.app.service.DealService;
import livelance.app.service.LocationService;
import livelance.app.support.LocationListResourceAsm;
import livelance.app.support.LocationResourceAsm;
import livelance.app.web.resource.LocationResource;

@CrossOrigin
@RestController
@RequestMapping("/api/deals/{dealId}/locations")
public class ApiLocationController {

	@Autowired
	LocationService locationService;
	
	@Autowired
	DealService dealService;
	
	@Autowired
	LocationResourceAsm asm;
	
	@Autowired
	LocationListResourceAsm asmList;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<LocationResource>> getAll(@PathVariable Long dealId) {

		// HttpHeaders httpHeaders = new HttpHeaders();

		LocationList entities= locationService.findAll(dealId);

		return new ResponseEntity<>(asmList.toResource(entities).getLocations(), HttpStatus.OK);
	}

	// GET ONE
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<LocationResource> get(@PathVariable Long id, @PathVariable Long dealId) {

		Location found = locationService.findOne(id, dealId);
		if (found == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(asm.toResource(found), HttpStatus.OK);
	}

	// DELETE
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<LocationResource> delete(@PathVariable Long id, @PathVariable Long dealId) {

		Location deleted = locationService.delete(id, dealId);
		if (deleted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ADD
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<LocationResource> add(@PathVariable Long dealId,
			@RequestBody LocationResource newEntity) {

		Location location = newEntity.toLocation();
		Deal deal = dealService.findOne(dealId);
		location.setDeal(deal);
		Location persisted = locationService.save(location);

		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.CREATED);
	}

	// EDIT
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<LocationResource> edit(@RequestBody LocationResource editEntity, 
			@PathVariable Long id) {
		
		Location location = editEntity.toLocation();
		location.setId(id);		
		Location persisted = locationService.save(location);

		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.OK);
	}
}
