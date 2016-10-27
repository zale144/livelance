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

import livelance.app.model.Account;
import livelance.app.model.Profile;
import livelance.app.model.list.ProfileList;
import livelance.app.service.AccountService;
import livelance.app.service.ProfileService;
import livelance.app.support.ProfileListResourceAsm;
import livelance.app.support.ProfileResourceAsm;
import livelance.app.web.controller.ApiProfileController;
import livelance.app.web.resource.ProfileResource;
import livelance.test.AbstractControllerTest;

@Transactional
public class ApiProfileControllerMocksTest extends AbstractControllerTest {

	@Mock
	private ProfileService profileService;
	
	@Mock
	private AccountService accountService;

	@InjectMocks
	private ApiProfileController profileController;
	
	@Spy
	ProfileResourceAsm asm;
	
	@Spy
	ProfileListResourceAsm asmList;
	
	ProfileList entities;
	
	Profile entity;
	
	Account account;
	
	@Before
	public void setUp() {
		
		account = new Account();
		
		entity = new Profile();
		entity.setId(1L);
		entity.setFirstname("Aleksandar");
		entity.setLastname("Sukovic");
		entity.setEmail("aleksandar@gmail.com");
		entity.setPhoneNumber("065021");
		
		entities = new ProfileList(new ArrayList<Profile>());
		entities.getProfiles().add(entity);
		
		MockitoAnnotations.initMocks(this);
		setUp(profileController);
	}
	
	@Test // GET ALL
	public void testGetAll() throws Exception {
		
		when(profileService.findAll()).thenReturn(entities);
		
		String uri = "/api/profiles";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(profileService, times(1)).findAll();
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		
	}
	
	@Test // GET ONE
	public void testGetOne() throws Exception {
		Long id = new Long(1);
		
		when(profileService.findOne(id)).thenReturn(entity);
		
		String uri = "/api/profiles/{id}";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(profileService, times(1)).findOne(id);
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value",
				content.trim().length() > 0);
	}
	
	@Test  // GET ONE NOT FOUND
	public void testGetOneNotFound() throws Exception {
		
		String uri = "/api/profiles/{id}";
		Long id = Long.MAX_VALUE;
		
		when(profileService.findOne(id)).thenReturn(null);
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(profileService, times(1)).findOne(id);
		
		Assert.assertEquals("failure - expected HTTP status 404",404 ,status);
		Assert.assertTrue("failure - expected HTTP response body to be empty",
				content.trim().length() == 0);
	}
	
	@Test  // GET BY NAME LIKE
	public void testGetByNameLike() throws Exception {
		String name = "jen";
		
		when(profileService.findByNameLike(name)).thenReturn(entities);
		
		String uri = "/api/profiles?name={name}";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, name).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(profileService, times(1)).findByNameLike(name);
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value",
				content.trim().length() > 0);
	}
	
	@Test  // GET BY FIRST AND LAST NAME
	public void testGetByFirstAndLastName() throws Exception {
		String name = "jen sukovic";
		
		when(profileService.findByFirstNameAndLastName("jen", "sukovic")).thenReturn(entities);
		
		String uri = "/api/profiles?name={name}";
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, name).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(profileService, times(1)).findByFirstNameAndLastName("jen", "sukovic");
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value",
				content.trim().length() > 0);
	}
	
	@Test // DELETE
	public void testDelete() throws Exception {
		Long id = new Long(1);
		
		String uri = "/api/profiles/{id}";
		
		when(profileService.delete(id)).thenReturn(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id).accept(
				MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Profile deleted = profileService.findOne(id);
		
		verify(profileService, times(1)).delete(id);
		
		Assert.assertEquals("failure - expected HTTP status 204",204 ,status);
		Assert.assertTrue("failure - expected HTTP response without body", 
				content.trim().length() == 0);
		Assert.assertNull("failure - expected entity to be null", deleted);
	}
	
	@Test  // ADD 
	public void testSaveAdd() throws Exception {
		
		when(profileService.save(any(Profile.class))).thenReturn(entity);
		
		String uri = "/api/profiles";
		String json = this.mapToJson(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(profileService, times(1)).save(any(Profile.class));
		
		ProfileResource newEntity = this.mapFromJson(content, ProfileResource.class);
		
		Assert.assertNotNull("failure - expected not null", newEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 201", 201, status);
		Assert.assertNotNull("failure - expected id not null", newEntity.getId());
		Assert.assertEquals("failure - expected same value", entity.getEmail(), newEntity.getEmail());
	}
	
	@Test
	public void testSaveEdit() throws Exception {
		
		Long id = new Long(1);
		entity.setFirstname("Radnaskela");
		
		when(profileService.save(any(Profile.class))).thenReturn(entity);
		when(accountService.save(any(Account.class))).thenReturn(account);
		when(accountService.findOne(any(Long.class))).thenReturn(account);
		
		String uri = "/api/profiles/{id}";
		String json = this.mapToJson(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(profileService, times(1)).save(any(Profile.class));
		
		ProfileResource newEntity = this.mapFromJson(content, ProfileResource.class);
		
		Assert.assertNotNull("failure - expected not null", newEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertEquals("failure - expected same id", entity.getId(), newEntity.getRid());
		Assert.assertEquals("failure - expected same value", "Radnaskela", newEntity.getFirstname());
	}
		
}
