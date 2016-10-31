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
import livelance.app.model.list.CategoryList;
import livelance.app.service.CategoryService;
import livelance.app.support.CategoryListResourceAsm;
import livelance.app.support.CategoryResourceAsm;
import livelance.app.web.controller.ApiCategoryController;
import livelance.app.web.resource.CategoryResource;
import livelance.test.AbstractControllerTest;

@Transactional
public class ApiCategoryControllerMocksTest extends AbstractControllerTest {

	@Mock
	private CategoryService categoryService;

	@InjectMocks
	private ApiCategoryController categoryController;
	
	@Spy
	CategoryResourceAsm asm;
	
	@Spy
	CategoryListResourceAsm asmList;
	
	Category entity;
	
	CategoryList entities;
	
	@Before
	public void setUp() {
		entity = new Category();
		entity.setId(1L);
		entity.setName("Trgovačke usluge");
		entity.setServices(new ArrayList<Service>());
		entities = new CategoryList(new ArrayList<Category>());
		entities.getCategories().add(entity);
		
		MockitoAnnotations.initMocks(this);
		setUp(categoryController);
	}
	
	@Test
	public void testGetAll() throws Exception {
		
		when(categoryService.findAll()).thenReturn(entities);
		
		String uri = "/api/categories";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(categoryService, times(1)).findAll();
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		
	}
	
	@Test
	public void testGetOne() throws Exception {
		Long id = new Long(1);
		
		when(categoryService.findOne(id)).thenReturn(entity);
		
		String uri = "/api/categories/{id}";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(categoryService, times(1)).findOne(id);
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value",
				content.trim().length() > 0);
	}
	
	@Test
	public void testGetOneNotFound() throws Exception {
		
		String uri = "/api/categories/{id}";
		Long id = Long.MAX_VALUE;
		
		when(categoryService.findOne(id)).thenReturn(null);
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(categoryService, times(1)).findOne(id);
		
		Assert.assertEquals("failure - expected HTTP status 404",404 ,status);
		Assert.assertTrue("failure - expected HTTP response body to be empty",
				content.trim().length() == 0);
	}
	
	@Test
	public void testDelete() throws Exception {
		Long id = new Long(1);
		
		String uri = "/api/categories/{id}";
		
		when(categoryService.delete(id)).thenReturn(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id).accept(
				MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Category deleted = categoryService.findOne(id);
		
		verify(categoryService, times(1)).delete(id);
		
		Assert.assertEquals("failure - expected HTTP status 204",204 ,status);
		Assert.assertTrue("failure - expected HTTP response without body", 
				content.trim().length() == 0);
		Assert.assertNull("failure - expected entity to be null", deleted);
	}
	
	@Test
	public void testSaveAdd() throws Exception {
		
		when(categoryService.save(any(Category.class))).thenReturn(entity);
		
		String uri = "/api/categories";
		String json = this.mapToJson(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(categoryService, times(1)).save(any(Category.class));
		
		CategoryResource newEntity = this.mapFromJson(content, CategoryResource.class);
		
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
		entity.setName("test");
		
		when(categoryService.save(any(Category.class))).thenReturn(entity);
		
		String uri = "/api/categories/{id}";
		String json = this.mapToJson(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(categoryService, times(1)).save(any(Category.class));
		
		CategoryResource newEntity = this.mapFromJson(content, CategoryResource.class);
		
		Assert.assertNotNull("failure - expected not null", newEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertEquals("failure - expected same id", entity.getId(), newEntity.getRid());
		Assert.assertEquals("failure - expected same value", "test", newEntity.getName());
	}
	
}
