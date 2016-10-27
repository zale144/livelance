package livelance.test;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import livelance.app.web.controller.ApiAccountController;
import livelance.app.web.controller.ApiCategoryController;
import livelance.app.web.controller.ApiDealController;
import livelance.app.web.controller.ApiLocationController;
import livelance.app.web.controller.ApiProfileController;
import livelance.app.web.controller.ApiRatingController;
import livelance.app.web.controller.ApiServiceController;

@WebAppConfiguration
public abstract class AbstractControllerTest extends AbstractTest {

	protected MockMvc mvc;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	protected void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	// ACCOUNT CONTROLLER STANDALONE SETUP
	protected void setUp(ApiAccountController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	// PROFILE CONTROLLER STANDALONE SETUP
	protected void setUp(ApiProfileController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	// SERVICE CONTROLLER STANDALONE SETUP
	protected void setUp(ApiServiceController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	// DEAL CONTROLLER STANDALONE SETUP
	protected void setUp(ApiDealController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	// CATEGORY CONTROLLER STANDALONE SETUP
	protected void setUp(ApiCategoryController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	// RATING CONTROLLER STANDALONE SETUP
	protected void setUp(ApiRatingController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	// LOCATION CONTROLLER STANDALONE SETUP
	protected void setUp(ApiLocationController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, clazz);
	}
}
