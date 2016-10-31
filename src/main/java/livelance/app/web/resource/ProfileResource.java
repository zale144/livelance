package livelance.app.web.resource;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import livelance.app.model.Account;
import livelance.app.model.Profile;

public class ProfileResource extends ResourceSupport {
	
	private Long rid;
	private AccountResource account = new AccountResource();
	private String firstname;
	private String lastname;
	private String email;
	private String phoneNumber;
	private String pictureLink;
	private String website;
	private String facebook;
	private String about;
	private Date dateOfRegistration;
	
	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateOfRegistration() {
		return dateOfRegistration;
	}

	public void setDateOfRegistration(Date date) {
		this.dateOfRegistration = date;
	}
	
	public String getPictureLink() {
		return pictureLink;
	}

	public void setPictureLink(String pictureLink) {
		this.pictureLink = pictureLink;
	}
	
	public AccountResource getAccount() {
		return account;
	}

	public void setAccount(AccountResource account) {
		this.account = account;
	}
	
	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	
	public Profile toProfile() {
		Profile profile = new Profile();
		Account account = this.account.toAccount();
		profile.setId(rid);
		profile.setAccount(account);
		profile.setFirstname(firstname);
		profile.setLastname(lastname);
		profile.setEmail(email);
		profile.setPhoneNumber(phoneNumber);
		profile.setId(rid);
		profile.setPictureLink(pictureLink);
		profile.setWebsite(website);
		profile.setFacebook(facebook);
		profile.setAbout(about);
		
		return profile;
	}

}
