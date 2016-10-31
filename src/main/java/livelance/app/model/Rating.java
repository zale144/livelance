package livelance.app.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Rating {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Deal deal;

	private String customerName;

	private int rating;

	private String comment;

	private Date timeOfRating = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Deal getDeal() {
		return deal;
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		if (customerName == null) {
			this.customerName = "Anoniman";
		} else {
			this.customerName = customerName;
		}
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		if(rating > 0 || rating <= 5) {
			this.rating = rating;
		}
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getTimeOfRating() {
		return timeOfRating;
	}

}
