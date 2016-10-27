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
import livelance.app.model.Rating;
import livelance.app.model.list.RatingList;
import livelance.app.service.DealService;
import livelance.app.service.RatingService;
import livelance.app.support.RatingListResourceAsm;
import livelance.app.support.RatingResourceAsm;
import livelance.app.web.resource.RatingResource;

@CrossOrigin
@RestController
@RequestMapping("/api/deals/{dealId}/ratings")
public class ApiRatingController {

	@Autowired
	RatingService ratingService;
	
	@Autowired
	DealService dealService;
	
	@Autowired
	RatingResourceAsm asm;
	
	@Autowired
	RatingListResourceAsm asmList;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<RatingResource>> getAll(@PathVariable Long dealId) {

		// HttpHeaders httpHeaders = new HttpHeaders();

		RatingList entities= ratingService.findAll(dealId);

		return new ResponseEntity<>(asmList.toResource(entities).getRatings(), HttpStatus.OK);
	}

	// GET ONE
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<RatingResource> get(@PathVariable Long id, @PathVariable Long dealId) {

		Rating found = ratingService.findOne(id, dealId);
		if (found == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(asm.toResource(found), HttpStatus.OK);
	}

	// DELETE
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<RatingResource> delete(
			@PathVariable Long id, @PathVariable Long dealId) {

		Rating deleted = ratingService.delete(id, dealId);
		if(deleted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ADD
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<RatingResource> add(
			@RequestBody RatingResource newEntity, @PathVariable Long dealId) {

		Rating rating = newEntity.toRating();
		Deal deal = dealService.findOne(dealId);
		rating.setDeal(deal);
		Rating persisted = ratingService.save(rating);

		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.CREATED);
	}

	// EDIT
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<RatingResource> edit(@RequestBody RatingResource editEntity, 
			@PathVariable Long id) {
		
		Rating rating = editEntity.toRating();
		rating.setId(id);
		Rating persisted = ratingService.save(rating);
		
		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.OK);
	}
}
