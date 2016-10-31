package livelance.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import livelance.app.model.Category;
import livelance.app.model.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

	List<Service> findByNameLike(String name);

	Service findByName(String name);

	List<Service> findByCategory(Category category);

	List<Service> findByCategoryId(Long categoryId);

	Service findByIdAndCategoryId(Long id, Long categoryId);

	List<Service> findByCategoryName(String categoryName);
}
