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
import livelance.app.model.list.ServiceList;
import livelance.app.service.CategoryService;
import livelance.app.service.DealService;
import livelance.app.service.ProfileService;
import livelance.app.service.ServiceService;
import livelance.test.AbstractTest;

@Transactional
public class ServiceServiceTest extends AbstractTest {

	@Autowired
	ProfileService profileService;

	@Autowired
	ServiceService serviceService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	DealService dealService;

	Circle circle;
	
	@Before
	public void setUp() {
		serviceService.evictCache();
		circle = new Circle(45.2532351449916,19.803570581372014, 1.0);
	}

	@After
	public void tearDown() {
		serviceService.evictCache();
	}

	@Test // FIND ALL
	public void testFindAll() {
		Long catId = new Long(1); 
		ServiceList entities = serviceService.findAll(catId);

		Assert.assertNotNull("failure - expected not null", entities);
		Assert.assertEquals("failure - expected same size", 2, entities.getServices().size());
	}

	@Test // FIND ONE
	public void testFindOne() {
		Service entity = serviceService.findOne(3L, 2L);
		System.out.println(entity.getName());
		Deal deal = dealService.findByServiceId(entity.getId()).getDeals().get(0);
		// check main entity
		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected same name", "Plumbing", entity.getName());
		// check sub entity no1
		Assert.assertNotNull("failure - expected not null", entity.getDeals());
		Assert.assertSame("failure - expected same entity", deal.getDescription(),
				entity.getDeals().get(0).getDescription());

		// check parent entity in sub_entity_no1
		Assert.assertNotNull("failure - expected not null", entity.getCategory());
		Assert.assertEquals("failure - expected same category", "Technology", entity.getCategory().getName());

		// check sub_entity_no1 in sub_entity_no1
		Assert.assertNotNull("failure - expected not null", entity.getDeals().get(0).getLocations());
		Assert.assertEquals("failure - expected same size", 1, entity.getDeals().get(0).getLocations().size());

		// check sub_entity_no1 in sub_entity_no1
		Assert.assertNotNull("failure - expected not null", entity.getDeals().get(0).getRatings());
		Assert.assertEquals("failure - expected same size", 1, entity.getDeals().get(0).getRatings().size());
	}

	@Test // FIND ONE - NOT FOUND
	public void testFindOneNotFound() {
		Long catId = new Long(1); 
		Long id = Long.MAX_VALUE;
		Service entity = serviceService.findOne(id, catId);
		Assert.assertNull("failure - expected null", entity);
	}

	@Test // DELETE
	public void testDelete() {
		Long catId = new Long(1); 
		Deal deleted = dealService.findByServiceId(1L).getDeals().get(0);
		int before = serviceService.findAll(catId).getServices().size();
		int beforeCategories = categoryService.findAll().getCategories().size();
		int beforeCategoriesServices = categoryService.findOne(catId).getServices().size();
		int beforeDeals = dealService.findAll(circle).getDeals().size();
		int beforeProfiles = profileService.findAll().getProfiles().size();
		int beforeHireeDeals = profileService.findOne(1L).getDeals().size();

		Profile profile = profileService.findOne(1L);
		profile.removeDeal(deleted);
		profileService.save(profile);
		
		
		Category category = categoryService.findOne(catId);
		Service entity = serviceService.findOne(1L, catId);
		entity.removeDeal(deleted);
		dealService.delete(deleted.getId());
		category.removeService(entity);
		categoryService.save(category);
		serviceService.delete(1L, catId);
		
		
		int after = serviceService.findAll(catId).getServices().size();
		Service deletedEntity = serviceService.findOne(1L, catId);
		int afterCategories = categoryService.findAll().getCategories().size();
		int afterCategoriesServices = categoryService.findOne(catId).getServices().size();
		int afterDeals = dealService.findAll(circle).getDeals().size();
		int afterProfiles = profileService.findAll().getProfiles().size();
		int afterHireeDeals = profileService.findOne(1L).getDeals().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertNull("failure - expected null", deletedEntity);
		Assert.assertEquals("failure - expected smaller size", before - 1, after);
		Assert.assertEquals("failure - expected smaller size",
				beforeCategoriesServices - 1, afterCategoriesServices);
		Assert.assertEquals("failure - expected smaller size", beforeDeals - 1, afterDeals);
		Assert.assertEquals("failure - expected same size", beforeCategories, afterCategories);
		Assert.assertEquals("failure - expected same size", beforeProfiles, afterProfiles);
		Assert.assertEquals("failure - expected smaller size", beforeHireeDeals - 1, afterHireeDeals);
	}

	@Test // SAVE - ADD NEW
	public void testSaveAdd() {
		Long catId = new Long(3); 
		int beforeAdd = serviceService.findAll(catId).getServices().size();
		int beforeAddCategoryServices = categoryService.findOne(3L).getServices().size();
		int beforeAddCategories = categoryService.findAll().getCategories().size();

		Service newEntity = new Service();
		Category category3 = categoryService.findOne(catId);
		newEntity.setName("Striptiz za džabe");
		newEntity.setCategory(category3);
		categoryService.save(category3);
		Service savedEntity = serviceService.save(newEntity);

		int afterAdd = serviceService.findAll(catId).getServices().size();
		String newName = serviceService.findOne(savedEntity.getId(), catId).getName();
		int afterAddCategoryServices = categoryService.findOne(3L).getServices().size();
		int afterAddCategories = categoryService.findAll().getCategories().size();

		Assert.assertNotNull("failure - expected not null", savedEntity);
		Assert.assertNotNull("failure - expected id attribute not null", savedEntity.getId());
		Assert.assertNotNull("failure - expected category id attribute not null",
				savedEntity.getCategory().getId());
		Assert.assertEquals("failure - expected greater size", beforeAdd + 1, afterAdd);
		Assert.assertEquals("failure - expected same size", beforeAddCategoryServices + 1, afterAddCategoryServices);
		Assert.assertEquals("failure - expected same size", beforeAddCategories, afterAddCategories);
		Assert.assertEquals("failure - expected same name", "Striptiz za džabe", newName);

	}

	@Test // SAVE - EDIT EXISTING
	public void testSaveEdit() {
		Long catId = new Long(1); 
		Long id = new Long(1); 
		Service editEntity = serviceService.findOne(id, catId);
		int beforeEdit = serviceService.findAll(catId).getServices().size();
		int beforeEditCategoryServices = categoryService.findOne(1L).getServices().size();
		int beforeEditDeals = dealService.findAll(circle).getDeals().size();
		int beforeEditCategories = categoryService.findAll().getCategories().size();

		editEntity.setName("Elektroinstalacije");   
		Service entity = serviceService.save(editEntity);

		int afterEdit = serviceService.findAll(catId).getServices().size();
		int afterEditCategoryServices = categoryService.findOne(1L).getServices().size();
		String name = serviceService.findOne(id, catId).getName();
		int afterEditDeals = dealService.findAll(circle).getDeals().size();
		String nameAfterEditDealsServices = dealService.findByServiceId(editEntity.getId())
				.getDeals().get(0).getService().getName();
		int afterEditCategories = categoryService.findAll().getCategories().size();

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
		Assert.assertNotNull("failure - expected category id attribute not null",
				entity.getCategory().getId());
		Assert.assertEquals("failure - expected same size", beforeEdit, afterEdit);
		Assert.assertEquals("failure - expected same size", beforeEditCategoryServices, afterEditCategoryServices);
		Assert.assertEquals("failure - expected same name", "Elektroinstalacije", name);
		Assert.assertEquals("failure - expected same size", beforeEditDeals, afterEditDeals);
		Assert.assertEquals("failure - expected same size", "Elektroinstalacije", nameAfterEditDealsServices);
		Assert.assertEquals("failure - expected same size", beforeEditCategories, afterEditCategories);
	}
}
