package livelance.test.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import livelance.app.model.Deal;
import livelance.app.model.Rating;
import livelance.app.model.list.RatingList;
import livelance.app.service.DealService;
import livelance.app.service.RatingService;
import livelance.app.support.RatingListResourceAsm;
import livelance.app.support.RatingResourceAsm;
import livelance.app.web.controller.ApiRatingController;
import livelance.app.web.resource.RatingResource;
import livelance.test.AbstractControllerTest;

@Transactional
public class ApiRatingControllerMocksTest extends AbstractControllerTest {

	@Mock
	private RatingService ratingService;
	
	@Mock
	private DealService dealService;
	
	@InjectMocks
	private ApiRatingController ratingController;
	
	@Spy
	RatingResourceAsm asm;
	
	@Spy
	RatingListResourceAsm asmList;
	
	RatingList list;
	
	Rating entity;
	
	Deal deal;
	
	@Before
	public void setUp() {
		deal = new Deal();
		deal.setId(1L);
		entity = new Rating();
		entity.setDeal(deal);
		entity.setId(1L);
		entity.setComment("moze to i bolje");
		entity.setCustomerName("Nerad");
		entity.setRating(2);
		
		list = new RatingList(new ArrayList<Rating>());
		list.getRatings().add(entity);
		
		MockitoAnnotations.initMocks(this);
		setUp(ratingController);
	}
	
	@Test
	public void testGetAll() throws Exception {
		Long dealId = new Long(1);
		
		when(ratingService.findAll(dealId)).thenReturn(list);
		
		String uri = "/api/deals/{dealId}/ratings";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, dealId).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(ratingService, times(1)).findAll(dealId);
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		
	}
	
	@Test
	public void testGetOne() throws Exception {
		Long id = new Long(1);
		Long dealId = new Long(1);
		
		when(ratingService.findOne(id, dealId)).thenReturn(entity);
		
		String uri = "/api/deals/{dealId}/ratings/{id}";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, dealId, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(ratingService, times(1)).findOne(id, dealId);
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value",
				content.trim().length() > 0);
	}
	
	@Test
	public void testGetOneNotFound() throws Exception {
		Long id = Long.MAX_VALUE;
		Long dealId = new Long(1);
		
		String uri = "/api/deals/{dealId}/ratings/{id}";
		
		when(ratingService.findOne(id, dealId)).thenReturn(null);
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, dealId, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(ratingService, times(1)).findOne(id, dealId);
		
		Assert.assertEquals("failure - expected HTTP status 404",404 ,status);
		Assert.assertTrue("failure - expected HTTP response body to be empty",
				content.trim().length() == 0);
	}
	
	@Test
	public void testDelete() throws Exception {
		Long id = new Long(1);
		Long dealId = new Long(1);
		
		String uri = "/api/deals/{dealId}/ratings/{id}";
		
		when(ratingService.delete(id, dealId)).thenReturn(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, dealId, id).accept(
				MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Rating deleted = ratingService.findOne(id, dealId);
		
		verify(ratingService, times(1)).delete(id, dealId);
		
		Assert.assertEquals("failure - expected HTTP status 204",204 ,status);
		Assert.assertTrue("failure - expected HTTP response without body", 
				content.trim().length() == 0);
		Assert.assertNull("failure - expected entity to be null", deleted);
	}
	
	@Test
	public void testSaveAdd() throws Exception {
		
		Long dealId = new Long(1);
		
		when(ratingService.save(any(Rating.class))).thenReturn(entity);
		when(dealService.findOne(dealId)).thenReturn(deal);
		
		String uri = "/api/deals/{dealId}/ratings";
		String json = this.mapToJson(entity);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri, dealId).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(ratingService, times(1)).save(any(Rating.class));
		
		RatingResource newEntity = this.mapFromJson(content, RatingResource.class);
		
		Assert.assertNotNull("failure - expected not null", newEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 201", 201, status);
		Assert.assertNotNull("failure - expected id not null", newEntity.getRid());
		Assert.assertEquals("failure - expected same value", entity.getComment(), newEntity.getComment());
	}
	
	@Test
	public void testSaveEdit() throws Exception {
		
		Long id = new Long(1);
		Long dealId = new Long(1);
		entity.setComment("test");
		
		when(ratingService.save(any(Rating.class))).thenReturn(entity);
		
		String uri = "/api/deals/{dealId}/ratings/{id}";
		String json = this.mapToJson(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, dealId, id).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(ratingService, times(1)).save(any(Rating.class));
		
		RatingResource editedEntity = this.mapFromJson(content, RatingResource.class);
		
		Assert.assertNotNull("failure - expected not null", editedEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertEquals("failure - expected same id", entity.getId(), editedEntity.getRid());
		Assert.assertEquals("failure - expected same value", entity.getComment(), editedEntity.getComment());
	}
	
}
