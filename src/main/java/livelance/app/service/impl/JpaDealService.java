package livelance.app.service.impl;

import java.util.List;

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
import livelance.app.model.Deal;
import livelance.app.model.list.DealList;
import livelance.app.repository.DealRepository;
import livelance.app.service.DealService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class JpaDealService implements DealService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DealRepository dealRepository;
	
	@Override
	public DealList findAll(Circle circle) {
		logger.info("> findAll");
		DealList deals = new DealList(dealRepository.findAll(
				circle.getLatitude(), circle.getLongitude(), circle.getRadius()));
		logger.info("< findAll");
		return deals;
	}

	@Override
	@Cacheable(value = "deals", key = "#id")
	public Deal findOne(Long id) {
		logger.info("> findOne id:{}", id);
		Deal deal = dealRepository.findOne(id);
		if (deal == null) {
			logger.error("Deal not found.");
		}
		logger.info("< findOne id:{}", id);
		return deal;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "deals", key = "#result.id")
	public Deal save(Deal deal) {
		logger.info("> save");
		if(deal == null) {
			return null;
		}
		Deal savedDeal = dealRepository.save(deal);
		if (savedDeal == null) {
			logger.error("Deal not saved.");
		}
		logger.info("< save");
		return savedDeal;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "deals", key = "#id")
	public Deal delete(Long id) {
		logger.info("> delete id:{}", id);
		
		Deal found = findOne(id);
		if (found == null) {
			logger.error("Attempted to delete a Deal, but the entity does not exist.");
		} else {
			dealRepository.delete(found);
		}
		logger.info("< delete id:{}", id);
		return found;
	}
	
	@Override
	public void delete(List<Deal> deals) {
		for(Deal deal : deals) {
			this.delete(deal.getId());
		}
	}

	@Override
	@Cacheable(value = "deals", key = "#profileId")
	public DealList findByProfileId(Long profileId) {

		DealList deals = new DealList(dealRepository.findByProfileId(profileId));
		
		return deals;
	}

	@Override
	@Cacheable(value = "deals", key = "#service")
	public DealList findByServiceId(Long serviceId) {
		logger.info("> findByServiceId serviceId:{}", serviceId);

		DealList deals = new DealList(dealRepository.findByServiceId(serviceId));

		logger.info("< findByService serviceId:{}", serviceId);
		return deals;
	}
	
	@Override
	public DealList findByServiceName(String serviceName, Circle circle) {
		logger.info("> findByServiceName serviceName:{}", serviceName);

		DealList deals = new DealList(dealRepository.findByServiceName(serviceName, 
				circle.getLatitude(), circle.getLongitude(), circle.getRadius()));

		logger.info("< findByServiceName serviceName:{}", serviceName);
		return deals;
	}
	
	@Override
	public DealList findbyCategoryName(String categoryName, Circle circle) {
		logger.info("> findbyCategoryName categoryName:{}", categoryName);

		DealList deals = new DealList(dealRepository.findByCategoryName(categoryName, 
				circle.getLatitude(), circle.getLongitude(), circle.getRadius()));

		logger.info("< findbyCategoryName categoryName:{}", categoryName);
		return deals;
	}

	@Override
	@Cacheable(value = "deals", key = "#description")
	public DealList findByDescription(String description) {
		logger.info("> findByDescription description:{}", description);

		DealList deals = new DealList(dealRepository.findByDescription(description));

		logger.info("< findByDescription description:{}", description);
		return deals;
	}

	@Override
	@Cacheable(value = "deals", key = "#search")
	public DealList findBySearchTerm(String search, Circle circle) {
		logger.info("> findBySearchTerm search:{}", search);

		DealList deals = new DealList(dealRepository.findByDescriptionLikeOrServiceLikeOrCategoryLike("%" + search + "%",
				circle.getLatitude(), circle.getLongitude(), circle.getRadius()));

		logger.info("< findBySearchTerm search:{}", search);
		return deals;
	}
	
	@Override
    @CacheEvict(
            value = "deals",
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }

}
