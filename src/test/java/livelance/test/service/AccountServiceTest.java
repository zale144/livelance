package livelance.test.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import livelance.app.model.Account;
import livelance.app.model.Circle;
import livelance.app.model.Deal;
import livelance.app.model.Service;
import livelance.app.model.list.AccountList;
import livelance.app.service.AccountService;
import livelance.app.service.DealService;
import livelance.app.service.ServiceService;
import livelance.test.AbstractTest;

@Transactional
public class AccountServiceTest extends AbstractTest {

	@Autowired
	AccountService accountService;

	@Autowired
	ServiceService serviceService;

	@Autowired
	DealService dealService;

	Circle circle;

	@Before
	public void setUp() {
		accountService.evictCache();
		circle = new Circle(45.2532351449916,19.803570581372014, 1.0);
	}

	@After
	public void tearDown() {
		accountService.evictCache();
	}


//	@Test // FIND ALL
//	public void testFindAll() {
	
	//TODO Security needs to be bypassed first!
	
//		AccountList entities = accountService.findAll();
//
//		Assert.assertNotNull("failure - expected not null", entities);
//		Assert.assertEquals("failure - expected same size", 3, entities.getAccounts().size());
//		Assert.assertNotEquals("failure - expected not same size", 
//				entities.getAccounts().get(1), entities.getAccounts().get(0));
//	}

	@Test // FIND ONE
	public void testFindOne() {
		Account entity = accountService.findOne(1L);
		// check main entity
		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected same user", "jane", entity.getUsername());

		Assert.assertNotNull("failure - expected not null", entity.getProfile());
	}

	@Test // DELETE
	public void testDelete() {
		
//		int before = accountService.findAll().getAccounts().size();
		int beforeDeals = dealService.findAll(circle).getDeals().size();
		int beforeServices = serviceService.findAll(2L).getServices().size();
		int beforeServiceDeals = serviceService.findOne(3L, 2L).getDeals().size();

		Deal deleted = dealService.delete(2L);
		Service service = serviceService.findOne(3L, 2L);
		service.removeDeal(deleted);
		serviceService.save(service);
		Account entity = accountService.delete(2L);

//		int after = accountService.findAll().getAccounts().size();
		Account deletedEntity = accountService.findOne(2L);
		int afterDeals = dealService.findAll(circle).getDeals().size();
		int afterServices = serviceService.findAll(2L).getServices().size();
		int afterServiceDeals = serviceService.findOne(3L, 2L).getDeals().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertNull("failure - expected null", deletedEntity);
//		Assert.assertEquals("failure - expected smaller size", before - 1, after);
		Assert.assertEquals("failure - expected smaller size", beforeDeals - 2, afterDeals);
		Assert.assertEquals("failure - expected same size", beforeServices, afterServices);
		Assert.assertEquals("failure - expected smaller size", beforeServiceDeals - 1, afterServiceDeals);
	}

	@Test // SAVE - ADD NEW
	public void testSaveAdd() {

		Account newEntity = new Account();
		newEntity.setUsername("fili");
		newEntity.setPassword("$2a$04$iaCt6D1GR2QhQw8wZYyi3Orrzw7m0JwGTLJiz7hGomCtU8R5faeEe");

//		int beforeAdd = accountService.findAll().getAccounts().size();
		int beforeAddDeals = dealService.findAll(circle).getDeals().size();
		int beforeAddServices = serviceService.findAll(2L).getServices().size();

		Account entity = accountService.save(newEntity);

//		int afterAdd = accountService.findAll().getAccounts().size();
		String newName = accountService.findByUsername("fili").getUsername();
		int afterAddDeals = dealService.findAll(circle).getDeals().size();
		int afterAddServices = serviceService.findAll(2L).getServices().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertNotNull("failure - expected id attribute not null", entity.getId());
//		Assert.assertEquals("failure - expected greater size", beforeAdd + 1, afterAdd);
		Assert.assertEquals("failure - expected same name", "fili", newName);
		Assert.assertEquals("failure - expected same size", beforeAddDeals, afterAddDeals);
		Assert.assertEquals("failure - expected same size", beforeAddServices, afterAddServices);

	}

	@Test // SAVE - EDIT EXISTING
	public void testSaveEdit() {

//		int beforeEdit = accountService.findAll().getAccounts().size();
		int beforeEditDeals = dealService.findAll(circle).getDeals().size();
		int beforeEditServices = serviceService.findAll(2L).getServices().size();

		Account editEntity = accountService.findOne(2L);
		editEntity.setUsername("jovica");
		editEntity.setPassword("pastazazube");
		
		Long id = editEntity.getId();

		Account entity = accountService.save(editEntity);

//		int afterEdit = accountService.findAll().getAccounts().size();
		int afterEditDeals = dealService.findAll(circle).getDeals().size();
		int afterEditServices = serviceService.findAll(2L).getServices().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
//		Assert.assertEquals("failure - expected same size",sbeforeEdit, afterEdit);
		Assert.assertEquals("failure - expected same size", beforeEditDeals, afterEditDeals);
		Assert.assertEquals("failure - expected same size", beforeEditServices, afterEditServices);
	}
}
