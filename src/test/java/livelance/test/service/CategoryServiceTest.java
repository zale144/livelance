package livelance.test.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import livelance.app.model.Category;
import livelance.app.model.Circle;
import livelance.app.model.Deal;
import livelance.app.model.Profile;
import livelance.app.model.Service;
import livelance.app.model.list.CategoryList;
import livelance.app.service.CategoryService;
import livelance.app.service.DealService;
import livelance.app.service.ProfileService;
import livelance.app.service.RatingService;
import livelance.app.service.ServiceService;
import livelance.test.AbstractTest;

@Transactional
public class CategoryServiceTest extends AbstractTest {

	@Autowired
	ProfileService profileService;

	@Autowired
	ServiceService serviceService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	DealService dealService;

	@Autowired
	RatingService ratingService;
	
	Circle circle;

	@Before
	public void setUp() {
		categoryService.evictCache();
		circle = new Circle(45.2532351449916,19.803570581372014, 1.0);
	}

	@After
	public void tearDown() {
		categoryService.evictCache();
	}

	@Test // FIND BY NAME
	public void testFindByName() {
		CategoryList entities = categoryService.findByName("Education");

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getCategories().size());
		Assert.assertEquals("failure - expected same name", "Education", entities.getCategories().get(0).getName());
	}

	@Test // FIND BY NAME LIKE
	public void testFindByNameLike() {
		CategoryList entities = categoryService.findByNameLike("edu");

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 1, entities.getCategories().size());
		Assert.assertEquals("failure - expected same name", "Education", entities.getCategories().get(0).getName());
	}

	@Test // FIND ALL
	public void testFindAll() {
		CategoryList entities = categoryService.findAll();

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 5, entities.getCategories().size());
		Assert.assertNotEquals("failure - expected same size", entities.getCategories().get(1), entities.getCategories().get(0));
	}

	@Test // FIND ONE
	public void testFindOne() {
		Category entity = categoryService.findOne(2L);

		// check main entity
		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected same name", "Technology", entity.getName());
		// check sub entity no1
		Assert.assertEquals("failure - expected same size", 4, entity.getServices().size());
	}

	@Test // FIND ONE - NOT FOUND
	public void testFindOneNotFound() {

		Long id = Long.MAX_VALUE;
		Category entity = categoryService.findOne(id);
		Assert.assertNull("failure - expected null", entity);
	}

	@Test // DELETE
	public void testDelete() {

		Category entity = categoryService.findOne(1L);
		Service service = serviceService.findOne(1L, 1L);
		Deal deal = dealService.findOne(1L);
		int numberOfDeals = dealService.findbyCategoryName(entity.getName(), circle).getDeals().size();
		int before = categoryService.findAll().getCategories().size();
		int beforeDeals = dealService.findAll(circle).getDeals().size();
		int beforeProfiles = profileService.findAll().getProfiles().size();

		Profile profile = profileService.findOne(1L);
		profile.removeDeal(deal);
		profileService.save(profile);
		dealService.delete(1L);
		service.removeDeal(deal);
		serviceService.delete(1L, 1L);
		categoryService.delete(1L);

		int after = categoryService.findAll().getCategories().size();
		Category deletedEntity = categoryService.findOne(1L);
		int afterServices = serviceService.findAll(1L).getServices().size();
		int afterDeals = dealService.findAll(circle).getDeals().size();
		int afterProfiles = profileService.findAll().getProfiles().size();
		int afterRatings = ratingService.findAll(deal.getId()).getRatings().size();
		int afterProfileDeals = profileService.findOne(1L).getDeals().size();
		Deal deletedDeal = dealService.findOne(1L);

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertNull("failure - expected null", deletedEntity);
		Assert.assertNull("failure - expected null", deletedDeal);
		Assert.assertEquals("failure - expected smaller size", before - 1, after);
		Assert.assertEquals("failure - expected smaller size", beforeDeals - numberOfDeals, afterDeals);
		Assert.assertEquals("failure - expected smaller size", 0, afterServices);
		Assert.assertEquals("failure - expected same size", beforeProfiles, afterProfiles);
		Assert.assertEquals("failure - expected smaller size", 0, afterRatings);
		Assert.assertEquals("failure - expected smaller size", 0, afterProfileDeals);
	}

	@Test // SAVE - ADD NEW
	public void testSaveAdd() {

		int beforeAdd = categoryService.findAll().getCategories().size();

		Category entity = new Category();
		entity.setName("Entertainment");
		Category savedEntity = categoryService.save(entity);

		int afterAdd = categoryService.findAll().getCategories().size();
		Category foundEntity = categoryService.findOne(savedEntity.getId());

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertNotNull("failure - expected id attribute not null", foundEntity.getId());
		Assert.assertEquals("failure - expected greater size", beforeAdd + 1, afterAdd);
		Assert.assertEquals("failure - expected same name", "Entertainment", foundEntity.getName());

	}

	@Test // SAVE - EDIT EXISTING
	public void testSaveEdit() {
		
		Long id = 3L;

		Category editEntity = categoryService.findOne(id);
		int beforeEdit = categoryService.findAll().getCategories().size();
		int beforeEditServices = serviceService.findAll(1L).getServices().size();
		int beforeEditDeals = dealService.findAll(circle).getDeals().size();
		int beforeEditProfiles = profileService.findAll().getProfiles().size();

		editEntity.setName("Edukacija");

		Category entity = categoryService.save(editEntity);

		int afterEdit = categoryService.findAll().getCategories().size();
		int afterEditServices = serviceService.findAll(1L).getServices().size();
		int afterEditDeals = dealService.findAll(circle).getDeals().size();
		int afterEditProfiles = profileService.findAll().getProfiles().size();
		String name = categoryService.findByName("edukacija").getCategories().get(0).getName();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected same size", beforeEdit, afterEdit);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
		Assert.assertEquals("failure - expected same name", "Edukacija", name);
		Assert.assertEquals("failure - expected same size", beforeEditServices, afterEditServices);
		Assert.assertEquals("failure - expected same size", beforeEditDeals, afterEditDeals);
		Assert.assertEquals("failure - expected same size", beforeEditProfiles, afterEditProfiles);
	}
}
