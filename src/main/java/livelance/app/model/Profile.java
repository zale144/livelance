package livelance.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Profile {

	@Id
	@GeneratedValue
	private Long id;
	
	@JsonBackReference
	@OneToOne
	private Account account;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.REMOVE, 
			fetch = FetchType.LAZY, mappedBy = "profile")
	private List<Deal> deals = new ArrayList<>();

	@NotNull
	private String firstname;

	@NotNull
	private String lastname;

	private String email;

	private String phoneNumber;
	
	private String pictureLink;
	
	private String website;
	
	private String facebook;
	
	private String about;

	private Date dateOfRegistration = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setDateOfRegistration(Date dateOfRegistration) {
		this.dateOfRegistration = dateOfRegistration;
	}

	public void addDeal(Deal newDeal) {
		if (newDeal != null && !deals.contains(newDeal)) {
			deals.add(newDeal);
		}
	}

	public void removeDeal(Deal toRemove) {
		if (toRemove != null && deals.contains(toRemove)) {
			deals.remove(toRemove);
		}
	}

	public List<Deal> getDeals() {
		return deals;
	}

	public void setDeals(List<Deal> deals) {
		this.deals = deals;
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getPictureLink() {
		return pictureLink;
	}

	public void setPictureLink(String pictureLink) {
		this.pictureLink = pictureLink;
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
}
