package livelance.app.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Account {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	private String username;
	
	@NotNull
	private String password;
	
	@JsonManagedReference
	@OneToOne(cascade = CascadeType.REMOVE,
			mappedBy = "account", fetch = FetchType.LAZY)
	private Profile profile;
	
	// ACCOUNT STATE
	private boolean enabled = true;

	private boolean credentialsexpired = false;

	private boolean expired = false;

	private boolean locked = false;

	@ManyToMany
	(
			fetch = FetchType.EAGER, 
			cascade = CascadeType.ALL)
	@JoinTable(
			name = "AccountRole", 
			joinColumns = @JoinColumn(
					name="accountId",
					referencedColumnName = "id"), 
					inverseJoinColumns = @JoinColumn(
							name="roleId", 
							referencedColumnName = "id") )
	private Set<Role> roles;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
}
