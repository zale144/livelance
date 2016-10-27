package livelance.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import livelance.app.model.Category;
import livelance.app.model.list.CategoryList;
import livelance.app.repository.CategoryRepository;
import livelance.app.service.CategoryService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class JpaCategoryService implements CategoryService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public CategoryList findAll() {
		logger.info("> findAll");
		CategoryList categories = new CategoryList(categoryRepository.findAll());
		logger.info("< findAll");
		return categories;
	}

	@Override
	@Cacheable(value = "categories", key = "#id")
	public Category findOne(Long id) {
		logger.info("> findOne id:{}", id);
		Category category = categoryRepository.findOne(id);
		if (category == null) {
			logger.error("Category not found.");
		}
		logger.info("< findOne id:{}", id);
		return category;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "categories", key = "#result.id")
	public Category save(Category category) {
		logger.info("> save");
		if(category == null) {
			return null;
		}
		Category savedCategory = categoryRepository.save(category);
		logger.info("< save");
		return savedCategory;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "categories", key = "#id")
	public Category delete(Long id) {
		logger.info("> delete id:{}", id);

		Category found = findOne(id);
		if (found == null) {
			logger.error("Attempted to delete a Category, but the entity does not exist.");
		} else {
			categoryRepository.delete(found);
		}
		logger.info("< delete id:{}", id);
		return found;
	}

	@Override
	@Cacheable(value = "categories", key = "#name")
	public CategoryList findByName(String name) {
		logger.info("> findByName name:{}", name);
		CategoryList categories = new CategoryList(categoryRepository.findByName(name));
		logger.info("< findByName deal:{}", name);
		return categories;
	}

	@Override
	@Cacheable(value = "categories", key = "#name")
	public CategoryList findByNameLike(String name) {
		logger.info("> findByNameLike name:{}", name);

		CategoryList categories = new CategoryList(categoryRepository.findByNameLike("%" + name + "%"));

		logger.info("< findByNameLike deal:{}", name);
		return categories;
	}
	
	@Override
    @CacheEvict(
            value = "categories",
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }
}
