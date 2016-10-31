package livelance.app.model.list;

import java.util.ArrayList;
import java.util.List;

import livelance.app.model.Rating;

public class RatingList {
    private List<Rating> ratings = new ArrayList<Rating>();

    public RatingList(List<Rating> list) {
        this.ratings = list;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRating(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
