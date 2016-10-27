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

import livelance.app.model.Rating;
import livelance.app.model.list.RatingList;
import livelance.app.repository.RatingRepository;
import livelance.app.service.RatingService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class JpaRatingService implements RatingService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RatingRepository ratingRepository;

	@Override
	public RatingList findAll(Long dealId) {
		logger.info("> findAll");
		 RatingList list = new RatingList(ratingRepository.findByDealId(dealId));
		logger.info("< findAll");
		return list;
	}

	@Override
	@Cacheable(value = "ratings", key = "#id")
	public Rating findOne(Long id, Long dealId) {
		logger.info("> findOne id:{}", id);
		Rating rating = ratingRepository.findByIdAndDealId(id, dealId);
		if (rating == null) {
			logger.error("Rating not found.");
		}
		logger.info("< findOne id:{}", id);
		return rating;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "ratings", key = "#result.id")
	public Rating save(Rating rating) {
		logger.info("> save");
		if(rating == null) {
			return null;
		}
		Rating savedRating = ratingRepository.save(rating);
		if (savedRating == null) {
			logger.error("Rating not saved.");
		}
		logger.info("< save");
		return savedRating;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "ratings", key = "#id")
	public Rating delete(Long id, Long dealId) {
		logger.info("> delete id:{}", id);

		Rating found = findOne(id, dealId);
		if (found == null) {
			logger.error("Attempted to delete a Rating, but the entity does not exist.");
		} else {
			ratingRepository.delete(found);
		}
		logger.info("< delete id:{}", id);
		return found;
	}

	@Override
	@CacheEvict(value = "ratings", allEntries = true)
	public void evictCache() {
		logger.info("> evictCache");
		logger.info("< evictCache");
	}
}
