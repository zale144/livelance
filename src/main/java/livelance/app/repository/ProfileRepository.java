package livelance.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import livelance.app.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
	
	@Query("select h from Profile h where h.firstname = :namePart1 and h.lastname = :namePart2")
	List<Profile> findByFirstNameAndLastName(@Param("namePart1") String namePart1, @Param("namePart2") String namePart2);

	@Query("select h from Profile h where h.firstname like :name or h.lastname like :name")
	List<Profile> findByNameLike(@Param("name") String name);
}
