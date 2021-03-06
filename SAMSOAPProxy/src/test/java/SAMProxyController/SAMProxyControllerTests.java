package SAMProxyController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.validation.Valid;

import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import javax.validation.ConstraintViolation;

import controller.SAMProxyController;
import dto.CampaignTargetDto;
import junit.framework.Assert;
import net.minidev.json.JSONObject;
import service.SAMService;
import util.SOAPToolsForSAM;
import util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class, SAMProxyController.class, SAMService.class, SOAPToolsForSAM.class })
@SpringBootApplication
public class SAMProxyControllerTests {
	private MockMvc mockMvc;
	private LocalValidatorFactoryBean localValidatorFactory;
	
	@Autowired
	protected WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		localValidatorFactory = new LocalValidatorFactoryBean();
		localValidatorFactory.setProviderClass(HibernateValidator.class);
		localValidatorFactory.afterPropertiesSet();

	}
	
	
	@Test
	public void postCampaignTargetDto_invalidAttributes() throws IOException, Exception {
		CampaignTargetDto dto = new CampaignTargetDto();
		dto.setCode("1234!"); // invalid character (!)
		dto.setInternalName("AbcdefghijAbcdefghij"); // too long string
		dto.setContactNumbers(new String[1000000]);

		mockMvc.perform(post("/createOrUpdateTarget").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dto))).andExpect(status().isBadRequest());
	}

	@Test
	public void testValidation() {
		CampaignTargetDto dto = new CampaignTargetDto();
		dto.setCode("1234!"); // invalid character (!)
		dto.setInternalName(
				"AbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghijAbcdefghij");
		dto.setContactNumbers(new String[1000000]);

		Set<ConstraintViolation<CampaignTargetDto>> constraintViolations = localValidatorFactory.validate(dto);

		Iterator<ConstraintViolation<CampaignTargetDto>> iter = constraintViolations.iterator();
		while (iter.hasNext()) {
			ConstraintViolation<CampaignTargetDto> violation = iter.next();
			System.out.println("VIOLATION: property path: " + violation.getPropertyPath() + ", message: " + violation.getMessage());
		}

		Assert.assertTrue("Message", constraintViolations.size() == 3);
	}
	
	@Test
	public void postCampaignTargetDto_existingCode() throws IOException, Exception {
		System.out.println("Starting postCampaignTargetDto (test)");
		String username = "CUBE_B2C";
		String password = "P@ssw0rd";
		String soapEndpointURL = "https://cube.ws.secutix.com/tnco/external-remoting/com.secutix.service.campaign.v1_0.ExternalCampaignService.webservice?wsdl";

		CampaignTargetDto dto = new CampaignTargetDto();
		dto.setCode("TestT3");
		dto.setInternalName("Sample target for testing purposes.");
		dto.setContactNumbers(new String[1]);
		
		MvcResult mvcResult = mockMvc.perform(post("/createOrUpdateTarget?soapEndpointURL=" + soapEndpointURL + "&username=" + username
				+ "&password=" + password).contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(dto)))
				.andExpect(status().isBadRequest()).andReturn();
		
		System.out.println("mvcResult: " + mvcResult.getResponse().getContentAsString());
	}
	/*
	 * final ProductModel productModel = new ProductModel(); 26
	 * productModel.setLongName("A long name with\t a Tab character"); 27
	 * Set<ConstraintViolation<ProductModel>> constraintViolations =
	 * localValidatorFactory.validate(productModel); 28
	 * Assert.assertTrue("Expected validation error not found",
	 * constraintViolations.size() == 1); 29
	 * 
	 * 
	 */

	/*
	 * 
	 * @RequestMapping(value="/createOrUpdateTarget", method=RequestMethod.POST)
	 * public JSONObject createOrUpdateTarget(@RequestBody @Valid CampaignTargetDto
	 * campaignTargetDto) {
	 * 
	 * return null; }
	 * 
	 * 
	 * @Test public void testSayHelloWorld() throws Exception {
	 * this.mockMvc.perform(get("/").accept(MediaType.parseMediaType(
	 * "application/json;charset=UTF-8"))) .andExpect(status().isOk())
	 * .andExpect(content().contentType("application/json"));
	 * 
	 * }
	 * 
	 * 
	 */
}
