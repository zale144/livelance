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

import livelance.app.model.Category;
import livelance.app.model.Circle;
import livelance.app.model.Deal;
import livelance.app.model.Location;
import livelance.app.model.Profile;
import livelance.app.model.Service;
import livelance.app.model.list.DealList;
import livelance.app.service.DealService;
import livelance.app.service.LocationService;
import livelance.app.service.ServiceService;
import livelance.app.support.DealListResourceAsm;
import livelance.app.support.DealResourceAsm;
import livelance.app.web.controller.ApiDealController;
import livelance.app.web.resource.DealResource;
import livelance.test.AbstractControllerTest;

@Transactional
public class ApiDealControllerMocksTest extends AbstractControllerTest {

	@Mock
	private DealService dealService;
	
	@Mock
	private ServiceService serviceService;
	
	@Mock
	private LocationService locationService;
	
	@InjectMocks
	private ApiDealController dealController;
	
	@Spy
	DealResourceAsm asm;
	
	@Spy
	DealListResourceAsm asmList;
	
	DealList entitites;
	
	Deal entity;
	
	Category cat;
	
	Service serv;
	
	Location location;
	
	Profile pro;
	
	Circle circle;
	
	@Before
	public void setUp() {
		circle = new Circle(45.2532351449916,19.803570581372014, 1.0);
		
		cat = new Category();
		cat.setId(1L);
		cat.setName("Edukacija");
		
		serv = new Service();
		serv.setId(1L);
		serv.setName("English lessons");
		serv.setCategory(cat);
		serv.addDeal(entity);
		
		pro = new Profile();
		pro.setEmail("asdf@asd");
		pro.setFirstname("Jova");
		pro.setLastname("Jovic");
		pro.setId(1L);
		pro.setPhoneNumber("123456");

		location = new Location();
		location.setCountry("Serbia");
		location.setCity("Novi Sad");
		location.setStreet("Stojana Novakovica");
		location.setNumber("19");
		location.setLatitude(45.2532351449916);
		location.setLongitude(19.803570581372014);
		location.setDeal(entity);
		
		entity = new Deal();
		entity.setId(1L);
		entity.setDescription("koljem po kucama");
		entity.setServiceCost(885.0);
		entity.setService(serv);
		entity.setProfile(pro);
		entity.addLocation(location);
		
		entitites = new DealList(new ArrayList<Deal>());
		entitites.getDeals().add(entity);

		MockitoAnnotations.initMocks(this);
		setUp(dealController);
	}
	
	@Test
	public void testGetAll() throws Exception {
		
		when(dealService.findAll(any(Circle.class))).thenReturn(entitites);
		
		String uri = "/api/deals";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(dealService, times(1)).findAll(any(Circle.class));
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		
	}
	
	@Test
	public void testGetOne() throws Exception {
		Long id = new Long(1);
		
		when(dealService.findOne(id)).thenReturn(entity);
		
		String uri = "/api/deals/{id}";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(dealService, times(1)).findOne(id);
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value",
				content.trim().length() > 0);
	}
	
	@Test
	public void testGetOneNotFound() throws Exception {
		Long id = Long.MAX_VALUE;
		
		String uri = "/api/deals/{id}";
		
		when(dealService.findOne(id)).thenReturn(null);
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(dealService, times(1)).findOne(id);
		
		Assert.assertEquals("failure - expected HTTP status 404",404 ,status);
		Assert.assertTrue("failure - expected HTTP response body to be empty",
				content.trim().length() == 0);
	}
	
	@Test
	public void testDelete() throws Exception {
		Long id = new Long(1);
		
		String uri = "/api/deals/{id}";
		
		when(dealService.delete(id)).thenReturn(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id).accept(
				MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Deal deleted = dealService.findOne(id);
		
		verify(dealService, times(1)).delete(id);
		
		Assert.assertEquals("failure - expected HTTP status 204",204 ,status);
		Assert.assertTrue("failure - expected HTTP response without body", 
				content.trim().length() == 0);
		Assert.assertNull("failure - expected entity to be null", deleted);
	}
	
	@Test
	public void testSaveAdd() throws Exception {
		
		when(serviceService.findByName("English lessons")).thenReturn(serv);
		when(locationService.save(any(Location.class))).thenReturn(location);
		when(dealService.save(any(Deal.class))).thenReturn(entity);
		
		String uri = "/api/deals";
		String json = this.mapToJson(entity);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(dealService, times(1)).save(any(Deal.class));
		
		DealResource newEntity = this.mapFromJson(content, DealResource.class);
		
		Assert.assertNotNull("failure - expected not null", newEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 201", 201, status);
		Assert.assertNotNull("failure - expected id not null", newEntity.getRid());
		Assert.assertEquals("failure - expected same value", entity.getDescription(), newEntity.getDescription());
	}
	
	@Test
	public void testSaveEdit() throws Exception {
		
		Long id = new Long(1);
		entity.setDescription("Cheapest pedicure in town!");
		
		when(dealService.save(any(Deal.class))).thenReturn(entity);
		
		String uri = "/api/deals/{id}";
		String json = this.mapToJson(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(dealService, times(1)).save(any(Deal.class));
		
		DealResource editedEntity = this.mapFromJson(content, DealResource.class);
		
		Assert.assertNotNull("failure - expected not null", editedEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertEquals("failure - expected same id", entity.getId(), editedEntity.getRid());
		Assert.assertEquals("failure - expected same value", entity.getDescription(), editedEntity.getDescription());
	}
	
}
