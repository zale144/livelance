package livelance.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import livelance.app.model.Deal;
import livelance.app.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

	List<Rating> findByDeal(Deal deal);

	List<Rating> findByDealId(Long dealId);

	Rating findByIdAndDealId(Long id, Long dealId);

}
