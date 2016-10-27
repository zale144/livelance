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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import livelance.app.model.Circle;
import livelance.app.model.Deal;
import livelance.app.model.Location;
import livelance.app.model.Service;
import livelance.app.model.list.DealList;
import livelance.app.service.DealService;
import livelance.app.service.LocationService;
import livelance.app.service.ServiceService;
import livelance.app.support.DealListResourceAsm;
import livelance.app.support.DealResourceAsm;
import livelance.app.web.resource.DealResource;
import livelance.app.web.resource.LocationResource;

@CrossOrigin
@RestController
@RequestMapping("/api/deals")
public class ApiDealController {

	@Autowired
	DealService dealService;
	
	@Autowired
	ServiceService serviceService;
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	DealResourceAsm asm;
	
	@Autowired
	DealListResourceAsm asmList;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<DealResource>> getAll(
			@RequestParam(value = "profileId", required = false) Long profileId,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = false) Double radius) {
		
		Circle circle = new Circle(latitude, longitude, radius);
		
		DealList deals = null;
		
		if(profileId != null) {
			deals = dealService.findByProfileId(profileId);
		} else if(search != null) {
			deals = dealService.findBySearchTerm(search, circle);
		} else {
			deals = dealService.findAll(circle);
		}

		if(deals == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(asmList.toResource(deals).getDeals(), HttpStatus.OK);
	}

	// GET ONE
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<DealResource> get(@PathVariable Long id) {

		Deal found = dealService.findOne(id);
		if (found == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(asm.toResource(found), HttpStatus.OK);
	}

	// DELETE
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<DealResource> delete(@PathVariable Long id) {

		Deal deleted = dealService.delete(id);
		if (deleted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ADD
	@RequestMapping(
			method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<DealResource> add(@RequestBody DealResource newEntity) {

		Deal deal = newEntity.toDeal();
		Service service = serviceService.findByName(deal.getService().getName());
		deal.setService(service);
		Deal persisted = dealService.save(deal);
 		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		for(LocationResource l : newEntity.getLocations()) {
			Location loc = l.toLocation();
			loc.setDeal(persisted);
			locationService.save(loc);
		}
		
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.CREATED);
	}

	// EDIT
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<DealResource> edit(@RequestBody DealResource editEntity, 
			@PathVariable Long id) {

		Deal deal = editEntity.toDeal();
		deal.setId(id);
		
		Deal persisted = dealService.save(deal);
		
		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.OK);
	}

}
