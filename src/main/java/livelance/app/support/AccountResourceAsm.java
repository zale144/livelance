package livelance.app.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.Account;
import livelance.app.web.controller.ApiAccountController;
import livelance.app.web.controller.ApiProfileController;
import livelance.app.web.resource.AccountResource;

@Component
public class AccountResourceAsm extends ResourceAssemblerSupport<Account, AccountResource>{
	
	public AccountResourceAsm() {
		super(ApiAccountController.class, AccountResource.class);
    }

	public AccountResource toResource(Account account) {
		AccountResource res = new AccountResource();  
		res.setUsername(account.getUsername());
		res.setPassword(account.getPassword());
		res.setRid(account.getId());
		res.add(linkTo(methodOn(ApiAccountController.class).get(account.getUsername())).withSelfRel());
		res.add(linkTo(methodOn(ApiAccountController.class).getRoles(account.getUsername())).withRel("roles"));
		res.add(linkTo(methodOn(ApiProfileController.class).get(account.getProfile().getId())).withRel("profile"));
		return res;
	}

}
