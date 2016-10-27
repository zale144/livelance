package livelance.app.web.resource;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.model.Account;
import livelance.app.model.Profile;
import livelance.app.model.Role;

public class AccountRegistrationResource extends ResourceSupport {

	private Long rid;
	private Profile profile;
	private String username;
	private String password;
	private String passwordConfirm;
	private boolean enabled = true;
	private boolean credentialsexpired = false;
	private boolean expired = false;
	private boolean locked = false;
	private Set<Role> roles = new HashSet<Role>();
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isCredentialsexpired() {
		return credentialsexpired;
	}

	public void setCredentialsexpired(boolean credentialsexpired) {
		this.credentialsexpired = credentialsexpired;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	public Account toAccount() {
		Account account = new Account();
		account.setUsername(username);
		account.setPassword(password);
		account.setProfile(profile);
		account.setRoles(roles);
		return account;
	}

}
