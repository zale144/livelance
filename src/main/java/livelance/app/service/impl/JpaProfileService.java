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

import livelance.app.model.Profile;
import livelance.app.model.list.ProfileList;
import livelance.app.repository.ProfileRepository;
import livelance.app.service.ProfileService;

@Service
@Transactional
public class JpaProfileService implements ProfileService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ProfileRepository profileRepository;
	
	@Override
	public ProfileList findAll() {
		logger.info("> findAll");
		ProfileList profiles = new ProfileList(profileRepository.findAll());
		logger.info("< findAll");
		return profiles;
	}

	@Override
	@Cacheable(value = "profiles", key = "#id")
	public Profile findOne(Long id) {
		logger.info("> findOne id:{}", id);
		Profile profile = profileRepository.findOne(id);
		if (profile == null) {
			logger.error("Profile not found.");
		}
		logger.info("< findOne id:{}", profile.getFirstname());
		return profile;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "profiles", key = "#id")
	public Profile delete(Long id) {
		logger.info("> delete id:{}", id);

		Profile found = findOne(id);
		if (found == null) {
			logger.error("Attempted to delete a Profile, but the entity does not exist.");
		} else {
			profileRepository.delete(found);
		}
		logger.info("< delete id:{}", id);
		return found;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "profiles", key = "#result.id")
	public Profile save(Profile profile) {
		logger.info("> save");
		if(profile == null) {
			return null;
		}
		Profile savedProfile = profileRepository.save(profile);
		if (savedProfile == null) {
			logger.error("Profile not saved.");
		}
		logger.info("< save");
		return savedProfile;
	}


	@Override
	@Cacheable(value = "profiles", key="#namePart1.concat('-').concat(#namePart2)")
	public ProfileList findByFirstNameAndLastName(String namePart1, String namePart2) {
		logger.info("> findByFirstNameAndLastName namePart1:{}, namePart2:{}", namePart1, namePart2);
		ProfileList profiles = new ProfileList
				(profileRepository.findByFirstNameAndLastName(namePart1, namePart2));
		logger.info("< findByFirstNameAndLastName namePart1:{}, namePart2:{}", namePart1, namePart2);
		return profiles;
	}

	@Override
	@Cacheable(value = "profiles", key = "#name")
	public ProfileList findByNameLike(String name) {
		logger.info("> findByNameLike name:{}", name);
		ProfileList profiles = new ProfileList(profileRepository.findByNameLike("%" + name + "%"));
		logger.info("< findByNameLike name:{}", name);
		return profiles;
	}

	@Override
    @CacheEvict(
            value = "profiles",
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }

}
