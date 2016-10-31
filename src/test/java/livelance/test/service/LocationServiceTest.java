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
import livelance.app.model.list.LocationList;
import livelance.app.service.AccountService;
import livelance.app.service.DealService;
import livelance.app.service.LocationService;
import livelance.app.service.ServiceService;
import livelance.test.AbstractTest;

@Transactional
public class LocationServiceTest extends AbstractTest {

	@Autowired
	AccountService accountService;

	@Autowired
	ServiceService serviceService;

	@Autowired
	LocationService locationService;

	@Autowired
	DealService dealService;

	Circle circle;

	@Before
	public void setUp() {
		locationService.evictCache();
		circle = new Circle(45.2532351449916,19.803570581372014, 1.0);
	}

	@After
	public void tearDown() {
		locationService.evictCache();
	}

	@Test // FIND ALL
	public void testFindAll() {
		LocationList entities = locationService.findAll(1L);

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getLocations().size());
	}

	@Test // FIND ONE
	public void testFindOne() {
		Location entity = locationService.findOne(3L, 3L);
		// check main entity
		Assert.assertNotNull("failure - expected not null", entity);
		// check sub entity no1
		Assert.assertNotNull("failure - expected not null", entity.getDeal());
	}
	
	@Test // FIND ONE - NOT FOUND
	public void testFindOneNotFound() {

		Long id = Long.MAX_VALUE;
		Location entity = locationService.findOne(id, 3L);
		Assert.assertNull("failure - expected null", entity);

	}

	@Test // SAVE - ADD NEW
	public void testSaveAdd() {
		Deal deal = dealService.findOne(1L);
		int beforeAddDealsLocations = deal.getLocations().size();
		int beforeAdd = locationService.findAll(1L).getLocations().size();
		int beforeAddDeals = dealService.findAll(circle).getDeals().size();

		Location newEntity = new Location();
		newEntity.setCity("Beograd");
		newEntity.setCountry("Srbia");
		newEntity.setStreet("Kneza Milosa");
		newEntity.setNumber("42");
		newEntity.setLatitude(45.249271);
		newEntity.setLongitude(19.799630);
		newEntity.setDeal(deal);

		Location entity = locationService.save(newEntity);
		deal.addLocation(entity);
		dealService.save(deal);

		int afterAdd = locationService.findAll(1L).getLocations().size();
		int afterAddDealsLocations = dealService.findOne(1L).getLocations().size();
		int afterAddDeals = dealService.findAll(circle).getDeals().size();

		Assert.assertNotNull("failure - expected not null", entity.getDeal());
		Assert.assertNotNull("failure - expected id attribute not null", entity.getId());
		Assert.assertEquals("failure - expected greater size", beforeAdd + 1, afterAdd);
		Assert.assertEquals("failure - expected greater size", beforeAddDealsLocations + 1, afterAddDealsLocations);
		Assert.assertEquals("failure - expected same size", beforeAddDeals, afterAddDeals);

	}

	@Test // SAVE - EDIT EXISTING
	public void testSaveEdit() {
		
		Deal deal = dealService.findOne(2L);
		Location editEntity = locationService.findAll(deal.getId()).getLocations().get(0);
		int beforeEditDealsLocations = deal.getLocations().size();
		int beforeEdit = locationService.findAll(2L).getLocations().size();
		int beforeEditDeals = dealService.findAll(circle).getDeals().size();

		editEntity.setCountry("Serbia");
		editEntity.setCity("Paragovo");
		editEntity.setStreet("Seljačkih buna");
		editEntity.setNumber("1c");
		editEntity.setLatitude(45.249271);
		editEntity.setLongitude(19.799630);
		Long id = editEntity.getId();
		Location entity = locationService.save(editEntity);

		int afterEdit = locationService.findAll(2L).getLocations().size();
		String editLocationStreet = locationService.findAll(deal.getId()).getLocations().get(0).getStreet();
		int afterEditDeals = dealService.findAll(circle).getDeals().size();
		int afterEditDealsLocations = dealService.findOne(2L).getLocations().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
		Assert.assertEquals("failure - expected same size", beforeEdit, afterEdit);
		Assert.assertEquals("failure - expected same size", beforeEditDealsLocations, afterEditDealsLocations);
		Assert.assertEquals("failure - expected same street", "Seljačkih buna", editLocationStreet);
		Assert.assertEquals("failure - expected same size", beforeEditDeals, afterEditDeals);
	}

	@Test // DELETE
	public void testDelete() {
		
		Deal deal = dealService.findOne(1L);
		int beforeDeleteDealsLocations = deal.getLocations().size();
//		int before = locationService.findAll(1L).getLocations().size();
		int beforeDeals = dealService.findAll(circle).getDeals().size();
//		int beforeAccounts = accountService.findAll().getAccounts().size();

		Location entity = locationService.findOne(1L, 1L);
		deal.removeLocation(entity);
		dealService.save(deal);
		locationService.delete(1L, 1L);

//		int after = locationService.findAll(1L).getLocations().size();
		Location deletedEntity = locationService.findOne(1L, 1L);
		int afterDeleteDealsLocations = dealService.findOne(1L).getLocations().size();
		int afterDeals = dealService.findAll(circle).getDeals().size();
//		int afterAccounts = accountService.findAll().getAccounts().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertNull("failure - expected null", deletedEntity);
//		Assert.assertEquals("failure - expected smaller size", before - 1, after);
		Assert.assertEquals("failure - expected smaller size", beforeDeleteDealsLocations - 1,
				afterDeleteDealsLocations);
		Assert.assertEquals("failure - expected smaller size", beforeDeals - 1, afterDeals); // DEALS CAN ONLY BE FOUND IF THEY HAVE A LOCATION
//		Assert.assertEquals("failure - expected same size", beforeAccounts, afterAccounts);
	}

}
