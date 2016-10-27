package livelance.app.web.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import livelance.app.model.Account;
import livelance.app.model.Profile;
import livelance.app.model.Role;
import livelance.app.model.list.AccountList;
import livelance.app.service.AccountService;
import livelance.app.service.DealService;
import livelance.app.service.ProfileService;
import livelance.app.service.RoleService;
import livelance.app.support.AccountListResourceAsm;
import livelance.app.support.AccountResourceAsm;
import livelance.app.web.exceptions.ForbiddenException;
import livelance.app.web.resource.AccountRegistrationResource;
import livelance.app.web.resource.AccountResource;

@CrossOrigin
@RestController
@RequestMapping("/api/accounts")
public class ApiAccountController {

	@Autowired
	AccountService accountService;

	@Autowired
	RoleService roleService;
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
	DealService dealService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	AccountResourceAsm asm;  
	
	@Autowired
	AccountListResourceAsm asmList;  

	// GET ALL
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<AccountResource>> getAll() {
		AccountList accounts = accountService.findAll();
		return new ResponseEntity<>(asmList.toResource(accounts).getAccounts(), HttpStatus.OK);
	}

	// GET ONE
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public ResponseEntity<AccountResource> get(@PathVariable String username) {
		
		Account found = accountService.findByUsername(username);
		if (found == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		 Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        if(principal instanceof UserDetails) {
	            UserDetails details = (UserDetails)principal;
	            Account loggedIn = accountService.findByUsername(details.getUsername());
	            if(loggedIn.getUsername().equals(username)) {
	                    return new ResponseEntity<>(asm.toResource(found), HttpStatus.OK);
	            } else {
	                throw new ForbiddenException();
	            }
	        } else {
	            throw new ForbiddenException();
	        }
	}
	
	// GET ROLES
	@RequestMapping(value = "/{username}/roles", method = RequestMethod.GET)
	public ResponseEntity<Set<Role>> getRoles(@PathVariable String username) {
		
		Account found = accountService.findByUsername(username);
		if (found == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	    return new ResponseEntity<>(found.getRoles(), HttpStatus.OK);
	}
	
	// DELETE
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	public ResponseEntity<AccountResource> delete(@PathVariable String username) {

		Account toDelete = accountService.findByUsername(username);
		toDelete.getRoles().clear();
		Account deleted = accountService.delete(toDelete.getId());
		if(deleted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ADD
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<AccountResource> add(@RequestBody AccountRegistrationResource newEntity) {
		Account entity = accountService.findByUsername(newEntity.getUsername());
		
		if(entity != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		if(!newEntity.getPassword().equals(newEntity.getPasswordConfirm())) {
			return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
		}
		Account account = newEntity.toAccount();
		String rawPassword = account.getPassword(); 
		String cryptPassword = encoder.encode(rawPassword);
		account.setPassword(cryptPassword);
		Profile toSave = account.getProfile();
		account.setProfile(null);
		Account persisted = accountService.save(account);
		if (persisted == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Role userRole = roleService.getRole(1L);
		persisted.getRoles().add(userRole);
		toSave.setAccount(persisted);
		Profile newProfile = profileService.save(toSave);
		persisted.setProfile(newProfile);
		accountService.save(persisted);
		return new ResponseEntity<AccountResource>(asm.toResource(persisted), HttpStatus.CREATED);
	}

	// EDIT
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<AccountResource> edit(@PathVariable Long id, 
			@RequestBody AccountResource newEntity) {


		Account account = newEntity.toAccount();
		account.setId(id);
		
		Account persisted = accountService.save(account);
		
		return new ResponseEntity<>(asm.toResource(persisted), HttpStatus.OK);
	}

}
