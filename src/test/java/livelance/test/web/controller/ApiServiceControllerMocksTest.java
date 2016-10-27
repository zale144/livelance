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
import livelance.app.model.Service;
import livelance.app.model.list.ServiceList;
import livelance.app.service.CategoryService;
import livelance.app.service.ServiceService;
import livelance.app.support.ServiceListResourceAsm;
import livelance.app.support.ServiceResourceAsm;
import livelance.app.web.controller.ApiServiceController;
import livelance.app.web.resource.ServiceResource;
import livelance.test.AbstractControllerTest;

@Transactional
public class ApiServiceControllerMocksTest extends AbstractControllerTest {

	@Mock
	private ServiceService serviceService;
	
	@Mock
	private CategoryService categoryService;
	
	@InjectMocks
	private ApiServiceController serviceController;
	
	@Spy	
	ServiceResourceAsm asm;
	
	@Spy	
	ServiceListResourceAsm asmList;
	
	Service entity;
	ServiceList list;
	
	@Before
	public void setUp() {
		
		entity = new Service();
		entity.setId(1L);
		entity.setName("Casovi engleskog");
		Category parent = new Category();
		parent.setId(1L);
		parent.setName("Edukativne stvari");
		entity.setCategory(parent);
		
		list = new ServiceList(new ArrayList<Service>());
		list.getServices().add(entity);
		
		MockitoAnnotations.initMocks(this);
		
		setUp(serviceController);
	}
	
	@Test
	public void testGetAll() throws Exception {
		
		Long catId = new Long(1);
		
		when(serviceService.findAll(catId)).thenReturn(list);
		
		String uri = "/api/categories/{catId}/services";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, catId).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(serviceService, times(1)).findAll(catId);
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		
	}
	
	@Test
	public void testGetOne() throws Exception {
		Long id = new Long(1);
		Long catId = new Long(1);
		
		when(serviceService.findOne(id, catId)).thenReturn(entity);
		
		String uri = "/api/categories/{catId}/services/{id}";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, catId, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(serviceService, times(1)).findOne(id, catId);
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value",
				content.trim().length() > 0);
	}
	
	@Test
	public void testGetOneNotFound() throws Exception {
		Long id = Long.MAX_VALUE;
		Long catId = new Long(1);
		
		String uri = "/api/categories/{catId}/services/{id}";
		
		when(serviceService.findOne(id, catId)).thenReturn(null);
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, catId, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(serviceService, times(1)).findOne(id, catId);
		
		Assert.assertEquals("failure - expected HTTP status 404",404 ,status);
		Assert.assertTrue("failure - expected HTTP response body to be empty",
				content.trim().length() == 0);
	}
	
	@Test
	public void testDelete() throws Exception {
		Long id = new Long(1);
		Long catId = new Long(1);
		when(serviceService.delete(id, catId)).thenReturn(entity);
		
		String uri = "/api/categories/{catId}/services/{id}";
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, catId, id).accept(
				MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Service deleted = serviceService.findOne(id, catId);
		
		verify(serviceService, times(1)).delete(id, catId);
		
		Assert.assertEquals("failure - expected HTTP status 204",204 ,status);
		Assert.assertTrue("failure - expected HTTP response without body", 
				content.trim().length() == 0);
		Assert.assertNull("failure - expected entity to be null", deleted);
	}
	
	@Test
	public void testSaveAdd() throws Exception {
		
		Long catId = new Long(1);
		
		when(serviceService.save(any(Service.class))).thenReturn(entity);
		
		String uri = "/api/categories/{catId}/services";
		String json = this.mapToJson(entity);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri, catId).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(serviceService, times(1)).save(any(Service.class));
		
		ServiceResource newEntity = this.mapFromJson(content, ServiceResource.class);
		
		Assert.assertNotNull("failure - expected not null", newEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 201", 201, status);
		Assert.assertNotNull("failure - expected id not null", newEntity.getRid());
		Assert.assertEquals("failure - expected same value", entity.getName(), newEntity.getName());
	}
	
	@Test
	public void testSaveEdit() throws Exception {
		
		Long id = new Long(1);
		Long catId = new Long(1);
		entity.setName("Manikir i pedikir");
		
		when(serviceService.save(any(Service.class))).thenReturn(entity);
		
		String uri = "/api/categories/{catId}/services/{id}";
		String json = this.mapToJson(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, catId, id).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(serviceService, times(1)).save(any(Service.class));
		
		ServiceResource editedEntity = this.mapFromJson(content, ServiceResource.class);
		
		Assert.assertNotNull("failure - expected not null", editedEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertEquals("failure - expected same id", entity.getId(), editedEntity.getRid());
		Assert.assertEquals("failure - expected same value", entity.getName(), editedEntity.getName());
	}
	
}
