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

import livelance.app.model.Circle;
import livelance.app.model.list.ServiceList;
import livelance.app.repository.ServiceRepository;
import livelance.app.service.ServiceService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class JpaServiceService implements ServiceService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ServiceRepository serviceRepository;
	
	@Override
	public ServiceList findAll(Long catId) {
		logger.info("> findAll");
		ServiceList services = new ServiceList(serviceRepository.findByCategoryId(catId));
		logger.info("< findAll");
		return services;
	}

	@Override
	@Cacheable(value = "services", key = "#id.concat('-').concat(#catId)")
	public livelance.app.model.Service findOne(Long id, Long catId) {
		logger.info("> findOne id:{} catId:{}", id, catId);
		livelance.app.model.Service service = serviceRepository.findByIdAndCategoryId(id, catId);
		if (service == null) {
			logger.error("Service not found.");
		}
		logger.info("< findOne id:{} catId:{}", id, catId);
		return service;
	}
	
	@Override
	@Cacheable(value = "services", key = "#name")
	public livelance.app.model.Service findByName(String name) {
		logger.info("> findByName name:{}", name);
		livelance.app.model.Service service = serviceRepository.findByName(name);
		if (service == null) {
			logger.error("Service not found.");
		}
		logger.info("< findByName name:{}", name);
		return service;
	}

	@Override
	@Cacheable(value = "services", key = "#categoryName")
	public ServiceList findbyCategoryName(String categoryName, Circle circle) {
		logger.info("> findByCategoryName categoryName:{}", categoryName);
		ServiceList services = new ServiceList(serviceRepository.findByCategoryName(categoryName));
		logger.info("< findByCategoryName categoryName:{}", categoryName);
		return services;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "services", key = "#result.id")
	public livelance.app.model.Service save(livelance.app.model.Service entity) {
		logger.info("> save");
		if(entity == null) {
			return null;
		}
		
		livelance.app.model.Service savedService = serviceRepository.save(entity);
		if (savedService == null) {
			logger.error("Service not saved.");
		}
		
		logger.info("< save");
		return savedService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "services", key = "#id.concat('-').concat(#catId)")
	public livelance.app.model.Service delete(Long id, Long catId) {
		logger.info("> delete id:{}", id);

		livelance.app.model.Service found = findOne(id, catId);
		if (found == null) {
			logger.error("Attempted to delete a Service, but the entity does not exist.");
		} else {
			serviceRepository.delete(found);
		}
		logger.info("< delete id:{}", id);
		return found;
	}

	 @Override
	    @CacheEvict(
	            value = "services",
	            allEntries = true)
	    public void evictCache() {
	        logger.info("> evictCache");
	        logger.info("< evictCache");
	    }
}
