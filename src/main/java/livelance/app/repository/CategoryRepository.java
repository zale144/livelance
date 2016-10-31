package livelance.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import livelance.app.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByName(String name);

	List<Category> findByNameLike(String name);

}
