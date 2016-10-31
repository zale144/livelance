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

import livelance.app.model.Location;
import livelance.app.model.list.LocationList;
import livelance.app.repository.LocationRepository;
import livelance.app.service.LocationService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class JpaLocationService implements LocationService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	LocationRepository locationRepository;

	@Override
	public LocationList findAll(Long dealId) {
		logger.info("> findAll");
		LocationList locations = new LocationList(locationRepository.findByDealId(dealId));
		logger.info("< findAll street0:{}", locations.getLocations().get(0).getStreet());
		return locations;
	}

	@Override
	@Cacheable(value = "locations", key = "#id")
	public Location findOne(Long id, Long dealId) {
		logger.info("> findOne id:{}", id);
		Location location = locationRepository.findByIdAndDealId(id, dealId);
		if (location == null) {
			logger.error("Location not found.");
		}
		logger.info("< findOne id:{}", id);
		return location;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "locations", key = "#result.id")
	public Location save(Location location) {
		logger.info("> save country:{}, city:{}, street:{}, number:{}", 
				location.getCountry(), location.getCity(), location.getStreet(), location.getNumber());
//		if (location == null) {
//			return null;
//		}
		Location savedLocation = locationRepository.save(location);
		if (savedLocation == null) {
			logger.error("Location not saved.");
		}
		logger.info("< save country:{}, city:{}, street:{}, number:{}", 
			savedLocation.getCountry(), savedLocation.getCity(), 
			savedLocation.getStreet(), savedLocation.getNumber());
		return savedLocation;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "locations", key = "#id")
	public Location delete(Long id, Long dealId) {
		logger.info("> delete id:{}", id);

		Location found = findOne(id, dealId);
		if (found == null) {
			logger.error("Attempted to delete a Location, but the entity does not exist.");
		} else {
			locationRepository.delete(found);
		}
		logger.info("< delete id:{}", id);
		return found;
	}

	@Override
	@CacheEvict(value = "locations", allEntries = true)
	public void evictCache() {
		logger.info("> evictCache");
		logger.info("< evictCache");
	}
}
