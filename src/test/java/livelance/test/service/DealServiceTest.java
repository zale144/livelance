package livelance.test.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import livelance.app.model.Circle;
import livelance.app.model.Deal;
import livelance.app.model.Location;
import livelance.app.model.Profile;
import livelance.app.model.Service;
import livelance.app.model.list.DealList;
import livelance.app.service.DealService;
import livelance.app.service.LocationService;
import livelance.app.service.ProfileService;
import livelance.app.service.RatingService;
import livelance.app.service.ServiceService;
import livelance.test.AbstractTest;

@Transactional
public class DealServiceTest extends AbstractTest {

	@Autowired
	ProfileService profileService;

	@Autowired
	ServiceService serviceService;

	@Autowired
	LocationService locationService;

	@Autowired
	RatingService ratingService;

	@Autowired
	DealService dealService;

	Circle circle;

	@Before
	public void setUp() {
		dealService.evictCache();
		circle = new Circle(45.2532351449916,19.803570581372014, 1.0);
	}

	@After
	public void tearDown() {
		dealService.evictCache();
	}

	@Test // FIND BY SEARCH TERM
	public void testFindByDescriptionLike() {
		DealList entities = dealService.findBySearchTerm("ielts", circle);

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getDeals().size());
		Assert.assertEquals("failure - expected same name", "Prepare for your IELTS certification!",
				entities.getDeals().get(0).getDescription());
	}

	@Test // FIND ALL
	public void testFindAll() {
		DealList entities = dealService.findAll(circle);

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertTrue("failure - expected size greater than 0", entities.getDeals().size() > 0);
		Assert.assertNotEquals("failure - expected not same size",
				entities.getDeals().get(1), entities.getDeals().get(0));
	}

	@Test // FIND ONE
	public void testFindOne() {
		Deal entity = dealService.findOne(3L);
		// check main entity
		Assert.assertNotNull("failure - expected not null", entity);
		// check sub entity no1
		Assert.assertNotNull("failure - expected not null", entity.getProfile());

		// check sub_entity_no1
		Assert.assertNotNull("failure - expected not null", entity.getLocations());
		Assert.assertEquals("failure - expected same size", 1, entity.getLocations().size());

		// check sub_entity_no2
		Assert.assertNotNull("failure - expected not null", entity.getService());
		Assert.assertEquals("failure - expected same name", "English lessons", entity.getService().getName());

		// check sub_entity_no3
		Assert.assertNotNull("failure - expected not null", entity.getRatings());
		Assert.assertEquals("failure - expected same size", 1, entity.getRatings().size());
	}

	@Test // FIND ONE - NOT FOUND
	public void testFindOneNotFound() {

		Long id = Long.MAX_VALUE;
		Deal entity = dealService.findOne(id);
		Assert.assertNull("failure - expected null", entity);
	}

	@Test // SAVE - ADD NEW
	public void testSaveAdd() {
		
		int beforeAdd = dealService.findAll(circle).getDeals().size();
		int beforeAddUsers = profileService.findAll().getProfiles().size();
		int beforeAddServices = serviceService.findAll(1L).getServices().size();
		int beforeAddServicesDeals = serviceService.findOne(1L, 1L).getDeals().size();
		int beforeAddUsersDeals = serviceService.findOne(1L, 1L).getDeals().size();
		
		Service service = serviceService.findOne(1L, 1L);
		Profile profile = profileService.findOne(1L);
		
		Deal newEntity = new Deal();
		newEntity.setService(service);
		newEntity.setServiceCost(885.0);
		newEntity.setDescription("Cheap massage");
		newEntity.setLongDescription("Very professional, but affordable massage.");
		newEntity.setProfile(profile);
		Deal entity = dealService.save(newEntity);
		
		Location location = new Location();
		location.setCountry("Serbia");
		location.setCity("Beograd");
		location.setStreet("Kneza Milosa");
		location.setNumber("421");
		location.setLatitude(45.249271);
		location.setLongitude(19.799630);
		location.setDeal(entity);
		locationService.save(location);
		profile.addDeal(entity);
		profileService.save(profile);
		service.addDeal(entity);
		serviceService.save(service);
		Deal savedEntity = dealService.save(entity);
		
		Deal foundEntity = dealService.findOne(savedEntity.getId());
		int afterAdd = dealService.findAll(circle).getDeals().size();
		int afterAddLocations = locationService.findAll(savedEntity.getId()).getLocations().size();
		int afterAddUsers = profileService.findAll().getProfiles().size();
		int afterAddServices = serviceService.findAll(1L).getServices().size();
		int afterAddServicesDeals = serviceService.findOne(1L, 1L).getDeals().size();
		int afterAddUsersDeals = serviceService.findOne(1L, 1L).getDeals().size();

		Assert.assertNotNull("failure - expected not null", savedEntity);
		Assert.assertNotNull("failure - expected not null", foundEntity);
		Assert.assertTrue("failure - expected not null", foundEntity.getLongDescription().length() > 0);
		Assert.assertNotNull("failure - expected not null", savedEntity.getProfile());
		Assert.assertNotNull("failure - expected id attribute not null", savedEntity.getId());
		Assert.assertNotNull("failure - expected not null", savedEntity.getService());
		Assert.assertEquals("failure - expected greater size", beforeAdd + 1, afterAdd);
		Assert.assertEquals("failure - expected greater size", 
				beforeAddServicesDeals + 1, afterAddServicesDeals);
		Assert.assertEquals("failure - expected greater size", 
				beforeAddUsersDeals + 1, afterAddUsersDeals);
		Assert.assertEquals("failure - expected same size", 1, afterAddLocations);
		Assert.assertEquals("failure - expected same size", beforeAddUsers, afterAddUsers);
		Assert.assertEquals("failure - expected same size", beforeAddServices, afterAddServices);

	}

	@Test // SAVE - EDIT EXISTING
	public void testSaveEdit() {

		Deal entity;
		int beforeEdit = dealService.findAll(circle).getDeals().size();
		int beforeEditLocations = locationService.findAll(1L).getLocations().size();
		int beforeEditUsers = profileService.findAll().getProfiles().size();
		int beforeEditServices = serviceService.findAll(1L).getServices().size();

		Profile user = profileService.findOne(1L);
		Deal editEntity = dealService.findByProfileId(user.getId()).getDeals().get(0);
		Long id = editEntity.getId();

		Location newLocation = new Location();
		newLocation.setCountry("Serbia");
		newLocation.setCity("Paragovo");
		newLocation.setStreet("Seljackih buna");
		newLocation.setNumber("1c");
		newLocation.setLatitude(45.249271);
		newLocation.setLongitude(19.799630);
		newLocation.setDeal(editEntity);
		Location saved = locationService.save(newLocation);
		editEntity.addLocation(saved);
		entity = dealService.save(editEntity);
		profileService.save(user);

		editEntity.setDescription("Od sutra na novoj lokaciji");
		entity = dealService.save(editEntity);
		profileService.save(user);

		Location removedLocation = locationService.findAll(editEntity.getId()).getLocations().get(0);
		editEntity.removeLocation(removedLocation);
		Long locationId = removedLocation.getId();
		entity = dealService.save(editEntity);
		profileService.save(user);
		locationService.delete(locationId, 1L);

		int afterEdit = dealService.findAll(circle).getDeals().size();
		String editDescription = dealService.findOne(1L).getDescription();
		String editLocationStreet = locationService.findAll(editEntity.getId()).getLocations().get(0).getStreet();
		int afterEditLocations = locationService.findAll(1L).getLocations().size();
		int afterEditUsers = profileService.findAll().getProfiles().size();
		int afterEditServices = serviceService.findAll(1L).getServices().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
		Assert.assertEquals("failure - expected same size", beforeEdit, afterEdit);
		Assert.assertEquals("failure - expected same description", "Od sutra na novoj lokaciji", editDescription);
		Assert.assertEquals("failure - expected same street", "Seljackih buna", editLocationStreet);
		Assert.assertEquals("failure - expected same size", beforeEditLocations, afterEditLocations);
		Assert.assertEquals("failure - expected same size", beforeEditUsers, afterEditUsers);
		Assert.assertEquals("failure - expected same size", beforeEditServices, afterEditServices);
	}

	@Test // DELETE
	public void testDelete() {
		
		Profile h2 = profileService.findOne(1L);
		Deal entity = dealService.findByProfileId(h2.getId()).getDeals().get(0);
		int before = dealService.findAll(circle).getDeals().size();
		int beforeUsers = profileService.findAll().getProfiles().size();
		int beforeServices = serviceService.findAll(1L).getServices().size();
		int beforeUserDeals = profileService.findOne(1L).getDeals().size();
		int beforeServiceDeals = serviceService.findOne(1L, 1L).getDeals().size();
		int beforeRatings = ratingService.findAll(entity.getId()).getRatings().size();

		Service s2 = serviceService.findOne(1L, 1L);
		h2.removeDeal(entity);
		s2.removeDeal(entity);
		entity = dealService.delete(1L);
		profileService.save(h2);
		serviceService.save(s2);

		int after = dealService.findAll(circle).getDeals().size();
		Deal deletedEntity = dealService.findOne(1L);
		int afterUsers = profileService.findAll().getProfiles().size();
		int afterServices = serviceService.findAll(1L).getServices().size();
		int afterUserDeals = profileService.findOne(1L).getDeals().size();
		int afterServiceDeals = serviceService.findOne(1L, 1L).getDeals().size();
		int afterRatings = ratingService.findAll(entity.getId()).getRatings().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertNull("failure - expected null", deletedEntity);
		Assert.assertEquals("failure - expected smaller size", before - 1, after);
		Assert.assertEquals("failure - expected same size", beforeUsers, afterUsers);
		Assert.assertEquals("failure - expected smaller size", beforeUserDeals - 1, afterUserDeals);
		Assert.assertEquals("failure - expected smaller size", beforeServiceDeals - 1, afterServiceDeals);
		Assert.assertEquals("failure - expected same size", beforeServices, afterServices);
		Assert.assertEquals("failure - expected smaller size", beforeRatings - 1, afterRatings);
	}

}
