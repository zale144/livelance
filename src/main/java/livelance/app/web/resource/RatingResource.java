package livelance.app.web.resource;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import livelance.app.model.Rating;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="rid")
public class RatingResource extends ResourceSupport {

	private Long rid;
	private String customerName;
	private int rating;
	private String comment;
	private Date timeOfRating;

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
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
	
	public void setTimeOfRating(Date timeOfRating) {
		this.timeOfRating = timeOfRating;
	}
	
	public Rating toRating() {
		Rating rating = new Rating();
		rating.setComment(comment);
		rating.setCustomerName(customerName);
		rating.setRating(this.rating);
		return rating;
	}
}
