package livelance.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import livelance.app.model.Account;
import livelance.app.model.Profile;
import livelance.app.model.list.ProfileList;
import livelance.app.service.AccountService;
import livelance.app.service.ProfileService;
import livelance.app.support.ProfileListResourceAsm;
import livelance.app.support.ProfileResourceAsm;
import livelance.app.web.resource.ProfileResource;

@CrossOrigin
@RestController
@RequestMapping("/api/profiles")
public class ApiProfileController {

	@Autowired
	ProfileService profileService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	ProfileResourceAsm asm;
	
	@Autowired
	ProfileListResourceAsm asmList;

	//GET ALL
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ProfileResource>> getAll(
			@RequestParam(value = "name", required = false) String name) {
		ProfileList profiles;

		if (name != null) {
			String[] spl = name.split(" ");
			if (spl.length > 1) {
				profiles = profileService.findByFirstNameAndLastName(spl[0], spl[1]);
			} else {
				profiles = profileService.findByNameLike(name);
			}
		} else {
			profiles = profileService.findAll();
		}
		
		return new ResponseEntity<>(asmList.toResource(profiles).getProfiles(), HttpStatus.OK);
	}

	// GET ONE
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ProfileResource> get(@PathVariable Long id) {

		Profile found = profileService.findOne(id);
		if(found == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(asm.toResource(found), HttpStatus.OK);
	}

	// DELETE
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ProfileResource> delete(@PathVariable Long id) {
		
		Profile deleted = profileService.delete(id);
		if(deleted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<> (HttpStatus.NO_CONTENT);
	}
	
	// ADD
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<ProfileResource> add(@RequestBody ProfileResource newEntity) {
		
		Profile profile = newEntity.toProfile();
		
		Profile persisted = profileService.save(profile);
		
		if(persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.CREATED);
	}
	
	// EDIT
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<ProfileResource> edit(
			@PathVariable Long id, @RequestBody ProfileResource editEntity) {
		
		Profile profile = editEntity.toProfile();
		Account account = accountService.findOne(editEntity.getAccount().getRid());
		account.setProfile(profile);
		accountService.save(account);
		profile.setId(id);
		Profile persisted = profileService.save(profile);
		
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
}
