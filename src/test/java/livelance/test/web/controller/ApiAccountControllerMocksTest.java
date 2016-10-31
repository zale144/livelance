package livelance.test.web.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import livelance.app.model.Account;
import livelance.app.model.Deal;
import livelance.app.model.Profile;
import livelance.app.model.Role;
import livelance.app.model.list.AccountList;
import livelance.app.service.AccountService;
import livelance.app.service.ProfileService;
import livelance.app.service.RoleService;
import livelance.app.support.AccountListResourceAsm;
import livelance.app.support.AccountResourceAsm;
import livelance.app.web.controller.ApiAccountController;
import livelance.app.web.resource.AccountResource;
import livelance.test.AbstractControllerTest;

@Transactional
public class ApiAccountControllerMocksTest extends AbstractControllerTest {

	@Mock
	private AccountService accountService;
	
	@Mock
	private ProfileService profileService;
	
	@Mock
	private RoleService roleService;

	@InjectMocks
	private ApiAccountController accountController;
	
    private ArgumentCaptor<Account> accountCaptor;
    
    @Spy
	AccountResourceAsm asm;  
	
	@Spy
	AccountListResourceAsm asmList;
	
	@Mock
	PasswordEncoder encoder;
	
	AccountList entities;
	
	Account entity;
	
	Profile profile;
	
	Role role;
	
	@Before
	public void setUp() {
		role = new Role(4L, "User", "ROLE_USER");
		List<Account> accounts = new ArrayList<Account>();
		entity = new Account();
		entity.setId(1L);
		entity.setUsername("zale144");
		entity.setPassword("123456");
		entity.setCredentialsexpired(false);
		entity.setEnabled(true);
		entity.setExpired(false);
		entity.setLocked(false);
		profile = new Profile();
		profile.setId(1L);
		profile.setDateOfRegistration(new Date());
		profile.setDeals(new ArrayList<Deal>());
		profile.setEmail("aleksandar.sukovic@gmail.com");
		profile.setFirstname("Aleksandar");
		profile.setLastname("Sukovic");
		profile.setPhoneNumber("156518");
		profile.setAbout("stuff about me");
		profile.setAccount(entity);
		entity.setProfile(profile);
		entity.setRoles(new HashSet<Role>());
		accounts.add(entity);
		entities = new AccountList(accounts);
		accountCaptor = ArgumentCaptor.forClass(Account.class);
		MockitoAnnotations.initMocks(this);
		setUp(accountController);
	}
	
	@Test
	public void testGetAll() throws Exception {
		
		when(accountService.findAll()).thenReturn(entities);

		@SuppressWarnings("unchecked")
		MvcResult result = mvc.perform(get("/api/accounts"))
        .andExpect(jsonPath("$..username",
                hasItems(endsWith("zale144"))))
        .andExpect(status().isOk()).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(accountService, times(1)).findAll();
		
		Assert.assertEquals("failure - expected HTTP status 200",200 ,status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		
	}
	
	@Test
	public void testGetOne() throws Exception {
		
		Long id = new Long(1);
		
		String uri = "/api/profiles/{id}";
		when(accountService.findOne(id)).thenReturn(entity);
		
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.get(uri, id).accept(
						MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(accountService, times(0)).findOne(id);
		
		Assert.assertEquals("failure - expected HTTP status 404",404 ,status);
		Assert.assertTrue("failure - expected HTTP response body to be empty",
				content.trim().length() == 0);
	}
	
	@Test
	public void testDelete() throws Exception {
		Long id = new Long(1);
		String username = "zale144";
		
		String uri = "/api/accounts/{username}";
		
		when(accountService.findByUsername(username)).thenReturn(entity);
		when(accountService.delete(id)).thenReturn(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, username).accept(
				MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Account deleted = accountService.findOne(id);
		
		verify(accountService, times(1)).delete(id);
		
		Assert.assertEquals("failure - expected HTTP status 204",204 ,status);
		Assert.assertTrue("failure - expected HTTP response without body", 
				content.trim().length() == 0);
		Assert.assertNull("failure - expected entity to be null", deleted);
	}
	
	@Test  // REGISTRATION
	public void testSaveAdd() throws Exception {
		
		 when(accountService.save(any(Account.class))).thenReturn(entity);
		 when(roleService.getRole(4L)).thenReturn(role);
		 when(profileService.save(any(Profile.class))).thenReturn(profile);
		 when(encoder.encode("123456")).thenReturn("1a2s3d4f5g");

	        mvc.perform(post("/api/accounts")
	                .content("{\"username\":\"zale144\",\"password\":\"123456\""
	                		+ ",\"passwordConfirm\":\"123456\",\"profile\":"
	                		+ "{ \"about\":\"stuff about me\","
	                		+ "\"email\":\"aleksandar.sukovic@gmail.com\","
	                		+ "\"firstname\":\"Aleksandar\","
	                		+ "\"lastname\":\"Sukovic\","
	                		+ "\"phoneNumber\":\"156518\"}}")
	                .contentType(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isCreated())
	                .andExpect(jsonPath("$.username", is(entity.getUsername())));

	        verify(accountService, times(2)).save(accountCaptor.capture());
	        
	        String password = accountCaptor.getValue().getPassword();
	        assertEquals("123456", password);
	}
	
	@Test
	public void testSaveEdit() throws Exception {
		
		Long id = new Long(1);
		entity.setUsername("Radnaskela");
		
		when(accountService.save(any(Account.class))).thenReturn(entity);
		
		String uri = "/api/accounts/{id}";
		String json = this.mapToJson(entity);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id).contentType(
				MediaType.APPLICATION_JSON).accept(
						MediaType.APPLICATION_JSON).content(json)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(accountService, times(1)).save(any(Account.class));
		
		AccountResource newEntity = this.mapFromJson(content, AccountResource.class);
		
		Assert.assertNotNull("failure - expected not null", newEntity);
		Assert.assertTrue("failure - expected HTTP response body to have a value", 
				content.trim().length() > 0);
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertEquals("failure - expected same id", entity.getId(), newEntity.getRid());
		Assert.assertEquals("failure - expected same value", "Radnaskela", newEntity.getUsername());
	}
}
