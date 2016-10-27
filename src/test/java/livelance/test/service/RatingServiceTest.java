package livelance.test.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import livelance.app.model.Circle;
import livelance.app.model.Deal;
import livelance.app.model.Rating;
import livelance.app.model.list.RatingList;
import livelance.app.service.AccountService;
import livelance.app.service.DealService;
import livelance.app.service.RatingService;
import livelance.app.service.ServiceService;
import livelance.test.AbstractTest;

@Transactional
public class RatingServiceTest extends AbstractTest {

	@Autowired
	RatingService ratingService;

	@Autowired
	AccountService accountService;

	@Autowired
	ServiceService serviceService;

	@Autowired
	DealService dealService;

	Circle circle;

	@Before
	public void setUp() {
		ratingService.evictCache();
		circle = new Circle(45.2532351449916,19.803570581372014, 1.0);
	}

	@After
	public void tearDown() {
		ratingService.evictCache();
	}

	@Test // FIND ALL
	public void testFindAll() {
		RatingList entities = ratingService.findAll(1L);

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getRatings().size());
	}

	@Test // FIND ONE
	public void testFindOne() {
		Rating entity = ratingService.findOne(3L, 3L);
		// check main entity
		Assert.assertNotNull("failure - expected not null", entity);
		// check sub entity no1
		Assert.assertNotNull("failure - expected not null", entity.getDeal());
	}
	
	@Test // FIND ONE - NOT FOUND
	public void testFindOneNotFound() {

		Long id = Long.MAX_VALUE;
		Rating entity = ratingService.findOne(id, 1L);
		Assert.assertNull("failure - expected null", entity);
	}

	@Test // SAVE - ADD NEW
	public void testSaveAdd() {
		Deal deal = dealService.findOne(1L);
		int beforeAddDealsRatings = dealService.findOne(1L).getRatings().size();
		int beforeAdd = ratingService.findAll(1L).getRatings().size();
		int beforeAddDeals = dealService.findAll(circle).getDeals().size();

		Rating newEntity1 = new Rating();
		newEntity1.setComment("onako..");
		newEntity1.setCustomerName("Cica");
		newEntity1.setRating(3);
		newEntity1.setDeal(deal);
		Rating entity = ratingService.save(newEntity1);
		deal.addRating(entity);
		
		Rating newEntity2 = new Rating();
		newEntity2.setComment("Vrhunska usluga");
		newEntity2.setCustomerName("Mali bora");
		newEntity2.setRating(5);
		newEntity2.setDeal(deal);
		Rating entity2 = ratingService.save(newEntity2);
		deal.addRating(entity2);
			
		dealService.save(deal);
		
		int afterAdd = ratingService.findAll(1L).getRatings().size();
		int afterAddDeals = dealService.findAll(circle).getDeals().size();
		int afterAddDealsRatings = dealService.findOne(1L).getRatings().size();
		double averageRating = dealService.findOne(1L).getAverageRating();
		
		Assert.assertNotNull("failure - expected not null", entity.getDeal());
		Assert.assertNotNull("failure - expected id attribute not null", entity.getId());
		Assert.assertEquals("failure - expected greater size", beforeAdd + 2, afterAdd);
		Assert.assertEquals("failure - expected greater size", beforeAddDealsRatings + 2, afterAddDealsRatings);
		Assert.assertEquals("failure - expected same size", beforeAddDeals, afterAddDeals);
		Assert.assertEquals("failure - expected same average rating", 4.3, averageRating, 0.04);

	}

	@Test // SAVE - EDIT EXISTING
	public void testSaveEdit() {
		
		Deal deal = dealService.findOne(1L);
		Rating editEntity = ratingService.findAll(deal.getId()).getRatings().get(0);
		int beforeEditDealsRatings = deal.getRatings().size();
		int beforeEdit = ratingService.findAll(1L).getRatings().size();
		int beforeEditDeals = dealService.findAll(circle).getDeals().size();

		editEntity.setComment("PREVARA!");
		editEntity.setRating(1);
		Long id = editEntity.getId();
		Rating entity = ratingService.save(editEntity);

		int afterEdit = ratingService.findAll(1L).getRatings().size();
		String editComment = ratingService.findAll(deal.getId()).getRatings().get(0).getComment();
		int afterEditDeals = dealService.findAll(circle).getDeals().size();
		int afterEditDealsRatings = dealService.findOne(1L).getRatings().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
		Assert.assertEquals("failure - expected same size", beforeEdit, afterEdit);
		Assert.assertEquals("failure - expected same size", beforeEditDealsRatings, afterEditDealsRatings);
		Assert.assertEquals("failure - expected same comment", "PREVARA!", editComment);
		Assert.assertEquals("failure - expected same size", beforeEditDeals, afterEditDeals);
	}

	@Test // DELETE
	public void testDelete() {
		
		Deal deal = dealService.findOne(1L);
		int beforeDeleteDealsRatings = dealService.findOne(1L).getRatings().size();
		int before = ratingService.findAll(1L).getRatings().size();
		int beforeDeals = dealService.findAll(circle).getDeals().size();
//		int beforeAccounts = accountService.findAll().getAccounts().size();

		Rating entity = ratingService.findOne(1L, 1L);
		deal.removeRating(entity);
		dealService.save(deal);
		ratingService.delete(1L, 1L);

		int after = ratingService.findAll(1L).getRatings().size();
		Rating deletedEntity = ratingService.findOne(1L, 1L);
		int afterDeleteDealsRatings = dealService.findOne(1L).getRatings().size();
		int afterDeals = dealService.findAll(circle).getDeals().size();
//		int afterAccounts = accountService.findAll().getAccounts().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertNull("failure - expected null", deletedEntity);
		Assert.assertEquals("failure - expected smaller size", before - 1, after);
		Assert.assertEquals("failure - expected smaller size", beforeDeleteDealsRatings - 1,
				afterDeleteDealsRatings);	
		Assert.assertEquals("failure - expected same size", beforeDeals, afterDeals);
//		Assert.assertEquals("failure - expected same size", beforeAccounts, afterAccounts);
	}

}
