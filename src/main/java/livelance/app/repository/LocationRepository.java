package livelance.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import livelance.app.model.Deal;
import livelance.app.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	List<Location> findByDeal(Deal deal);

	Location findByIdAndDealId(Long id, Long dealId);

	List<Location> findByDealId(Long dealId);

}
