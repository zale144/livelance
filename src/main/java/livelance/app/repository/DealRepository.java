package livelance.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import livelance.app.model.Deal;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

	List<Deal> findByProfileId(Long id);

	List<Deal> findByDescription(String description);

	List<Deal> findByServiceId(Long id);

	@Query(value = "select  d.id, d.description, d.long_description, d.profile_id, d.service_id, d.service_cost "
			+ " from deal d inner join location l on l.deal_id = d.id "
			+ "where l.latitude > :latitude - :radius and l.latitude < :latitude + :radius and "
			+ "l.longitude > :longitude - :radius*1.9 and "
			+ "l.longitude < :longitude + :radius*1.9 "
			+ "group by deal_id;", nativeQuery = true)	
	List<Deal> findAll(@Param("latitude") Double latitude,
					   @Param("longitude") Double longitude,
					   @Param("radius") Double radius);

	@Query(value = "select  d.id, d.description, d.long_description, d.profile_id, d.service_id, d.service_cost "
			+ " from deal d "
			+ "inner join location l on l.deal_id = d.id "
			+ "inner join service s on s.id = d.service_id "
			+ "inner join category c on c.id = s.category_id "
			+ "where c.name = :categoryName and "
			+ "l.latitude > :latitude - :radius and l.latitude < :latitude + :radius and "
			+ "l.longitude > :longitude - :radius*1.9 and "
			+ "l.longitude < :longitude + :radius*1.9 "
			+ "group by deal_id;", nativeQuery = true)	
	List<Deal> findByCategoryName(
						@Param("categoryName") String categoryName,
						@Param("latitude") Double latitude,
						@Param("longitude") Double longitude,
						@Param("radius") Double radius);

	List<Deal> findByDescriptionLike(String description);
	
	@Query(value = "select  d.id, d.description, d.long_description, d.profile_id, d.service_id, d.service_cost "
			+ " from deal d "
			+ "inner join location l on l.deal_id = d.id "
			+ "inner join service s on s.id = d.service_id "
			+ "where l.latitude > :latitude - :radius and l.latitude < :latitude + :radius and "
			+ "l.longitude > :longitude - :radius*1.9 and "
			+ "l.longitude < :longitude + :radius*1.9 and "
			+ "s.name = :serviceName "
			+ "group by deal_id;", nativeQuery = true)
	List<Deal> findByServiceName(
						@Param("serviceName") String serviceName,
						@Param("latitude") Double latitude,
						@Param("longitude") Double longitude,
						@Param("radius") Double radius);
	
	@Query(value = "select  d.id, d.description, d.long_description, d.profile_id, d.service_id, d.service_cost "
			+ " from deal d "
			+ "inner join location l on l.deal_id = d.id "
			+ "inner join service s on s.id = d.service_id "
			+ "inner join category c on c.id = s.category_id "
			+ "where l.latitude > :latitude - :radius and l.latitude < :latitude + :radius and "
			+ "l.longitude > :longitude - :radius*1.9 and "
			+ "l.longitude < :longitude + :radius*1.9 and "
			+ "(c.name like :search or "
			+ "s.name like :search or "
			+ "d.description like :search )"
			+ "group by deal_id;", nativeQuery = true)	
	List<Deal> findByDescriptionLikeOrServiceLikeOrCategoryLike(
						@Param("search") String search,
						@Param("latitude") Double latitude,
						@Param("longitude") Double longitude,
						@Param("radius") Double radius);
}
