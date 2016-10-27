package livelance.test.service;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import livelance.app.model.Account;
import livelance.app.model.Circle;
import livelance.app.model.Deal;
import livelance.app.model.Profile;
import livelance.app.model.Service;
import livelance.app.model.list.ProfileList;
import livelance.app.service.AccountService;
import livelance.app.service.DealService;
import livelance.app.service.ProfileService;
import livelance.app.service.ServiceService;
import livelance.test.AbstractTest;

@Transactional
public class ProfileServiceTest extends AbstractTest {

	@Autowired
	AccountService accountService;
	
	@Autowired
	ProfileService profileService;

	@Autowired
	ServiceService serviceService;

	@Autowired
	DealService dealService;
	
	Circle circle;

	@Before
	public void setUp() {
		profileService.evictCache();
		circle = new Circle(45.2532351449916,19.803570581372014, 1.0);
	}

	@After
	public void tearDown() {
		profileService.evictCache();
	}

	@Test // FIND BY NAME LIKE
	public void testFindByNameLike() {
		ProfileList entities = profileService.findByNameLike("Janice");

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getProfiles().size());
		Assert.assertEquals("failure - expected same name", "Janice", entities.getProfiles().get(0).getFirstname());

		entities = profileService.findByNameLike("Jones");

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getProfiles().size());
		Assert.assertEquals("failure - expected same name", "Janice", entities.getProfiles().get(0).getFirstname());

		entities = profileService.findByNameLike("jan");

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getProfiles().size());
		Assert.assertEquals("failure - expected same name", "Janice", entities.getProfiles().get(0).getFirstname());

		entities = profileService.findByNameLike("jone");

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getProfiles().size());
		Assert.assertEquals("failure - expected same name", "Janice", entities.getProfiles().get(0).getFirstname());
	}

	@Test // FIND ALL
	public void testFindAll() {
		ProfileList entities = profileService.findAll();

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertTrue("failure - expected size to be greater than 0", 
				entities.getProfiles().size() > 0);
		Assert.assertNotEquals("failure - expected same size",
				entities.getProfiles().get(1), entities.getProfiles().get(0));
	}

	@Test // FIND ONE
	public void testFindOne() {
		Profile entity = profileService.findOne(1L);
		// check main entity
		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected same user", "Janice", entity.getFirstname());
		// check sub entity no1
		Assert.assertNotNull("failure - expected not null", entity.getDeals());
		Assert.assertEquals("failure - expected same size", 1, entity.getDeals().size());

		// check parent entity in sub_entity_no1
		Assert.assertNotNull("failure - expected not null", entity.getDeals().get(0).getProfile());

		// check sub_entity_no1 in sub_entity_no1
		Assert.assertNotNull("failure - expected not null", entity.getDeals().get(0).getLocations());
		Assert.assertEquals("failure - expected same size", 1, entity.getDeals().get(0).getLocations().size());

		// check sub_entity_no2 in sub_entity_no1
		Assert.assertNotNull("failure - expected not null", entity.getDeals().get(0).getService());
		Assert.assertEquals("failure - expected same name", "Massage", entity.getDeals().get(0).getService().getName());

		// check sub_entity_no1 in sub_entity_no1
		Assert.assertNotNull("failure - expected not null", entity.getDeals().get(0).getRatings());
		Assert.assertEquals("failure - expected same size", 1, entity.getDeals().get(0).getRatings().size());
	}

	@Test // FIND BY FIRST AND LAST NAME
	public void testFindByFirstNameAndLastName() {
		ProfileList entities = profileService.findByFirstNameAndLastName("Janice", "Jones");

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getProfiles().size());
		Assert.assertEquals("failure - expected same name", "Janice", entities.getProfiles().get(0).getFirstname());

	}

	@Test // DELETE
	public void testDelete() {
		
		int before = profileService.findAll().getProfiles().size();
		int beforeDeals = dealService.findAll(circle).getDeals().size();
		int beforeServices = serviceService.findAll(2L).getServices().size();
		int beforeServiceDeals = serviceService.findOne(3L, 2L).getDeals().size();

		Deal deleted = dealService.delete(2L);
		Service service = serviceService.findOne(3L, 2L);
		service.removeDeal(deleted);
		serviceService.save(service);
		Profile entity = profileService.delete(2L);

		int after = profileService.findAll().getProfiles().size();
		int afterDeals = dealService.findAll(circle).getDeals().size();
		int afterServices = serviceService.findAll(2L).getServices().size();
		int afterServiceDeals = serviceService.findOne(3L, 2L).getDeals().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected smaller size", before - 1, after);
		Assert.assertEquals("failure - expected smaller size", beforeDeals - 2, afterDeals);
		Assert.assertEquals("failure - expected same size", beforeServices, afterServices);
		Assert.assertEquals("failure - expected smaller size", beforeServiceDeals - 1, afterServiceDeals);
	}

	@Test // SAVE - ADD NEW
	public void testSaveAdd() {

        Account account = accountService.findOne(1L);
		Profile newEntity = new Profile();
		newEntity.setFirstname("Filip");
		newEntity.setLastname("Visnjic");
		newEntity.setEmail("filip@example.com");
		newEntity.setPhoneNumber("062/423456");
		newEntity.setDateOfRegistration(new Date());
		newEntity.setAccount(account);

		int beforeAdd = profileService.findAll().getProfiles().size();
		int beforeAddDeals = dealService.findAll(circle).getDeals().size();

		Profile entity = profileService.save(newEntity);

		int afterAdd = profileService.findAll().getProfiles().size();
		String newName = profileService.findByFirstNameAndLastName("Filip", "Visnjic").getProfiles().get(0).getFirstname();
		int afterAddDeals = dealService.findAll(circle).getDeals().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertNotNull("failure - expected not null", entity.getDateOfRegistration());
		Assert.assertNotNull("failure - expected id attribute not null", entity.getId());
		Assert.assertEquals("failure - expected greater size", beforeAdd + 1, afterAdd);
		Assert.assertEquals("failure - expected same name", "Filip", newName);
		Assert.assertEquals("failure - expected same size", beforeAddDeals, afterAddDeals);

	}

	@Test // SAVE - EDIT EXISTING
	public void testSaveEdit() {

		int beforeEdit = profileService.findAll().getProfiles().size();
		int beforeEditDeals = dealService.findAll(circle).getDeals().size();
		int beforeEditServices = serviceService.findAll(2L).getServices().size();

		Profile editEntity = profileService.findOne(2L);
		Deal newDeal = dealService.findOne(2L);
		Deal toRemove = dealService.findOne(1L);
		editEntity.addDeal(newDeal);
		editEntity.removeDeal(toRemove);
		editEntity.setFirstname("Jovica");
		editEntity.setEmail("jovanovic@example.com");
		editEntity.setPhoneNumber("062/523456");
		
		Long id = editEntity.getId();

		Profile entity = profileService.save(editEntity);

		int afterEdit = profileService.findAll().getProfiles().size();
		int afterEditDeals = dealService.findAll(circle).getDeals().size();
		int afterEditServices = serviceService.findAll(2L).getServices().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
		Assert.assertEquals("failure - expected id attribute match", "Jovica", entity.getFirstname());
		Assert.assertEquals("failure - expected same size", beforeEdit, afterEdit);
		Assert.assertEquals("failure - expected same size", beforeEditDeals, afterEditDeals);
		Assert.assertEquals("failure - expected same size", beforeEditServices, afterEditServices);
	}
}
