package service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Exception.CleverReachRestServiceException;
import config.Config;
import dto.ErrorDto;
import dto.GroupDto;
import dto.LoginDto;
import net.minidev.json.JSONObject;

public class RestService {
	private Logger logger;
	private RestTemplate template;
	private HttpHeaders headers;
	private Config config;
	/*
	 * 	private static final String CLIENT_ID="123146";
	private static final String LOGIN="yvonne.kaschke@kulturplanner.com";
	private static final String PASSWORD="aVCkA3tS";
	 */
	
	public RestService(Config config) {
		this.config = config;
		this.logger = Logger.getLogger(RestService.class);
		this.template = (new RestTemplateBuilder()).build();
		this.headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}
	
	public String login(LoginDto login) throws URISyntaxException, JsonParseException, JsonMappingException, IOException, CleverReachRestServiceException {
		logger.info("login: Logging in " + login.getLogin() + " ...");
		String request_url = config.getBase_url() + "v2/login.json";
		RequestEntity<LoginDto> request=new RequestEntity<LoginDto>(login,headers,HttpMethod.POST,new URI(request_url));
		logger.info("login: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<String> response=null;	
		response=template.exchange(request, String.class);
		logger.info("login: Login was successful. Returning a_token ... ");
		return response.getBody().substring(1, response.getBody().length()-1);	
	}
	
	/*
	 * precondition: Config.getA_token returns a valid a_token.
	 */
	public GroupDto createGroup(GroupDto group) throws URISyntaxException{
		logger.info("createGroup: Creating group with the name \"" + group.getName() + "\" ...");
		String request_url = config.getBase_url() + "v2/groups.json?token=" + config.getA_token();
		RequestEntity<GroupDto> request=new RequestEntity<GroupDto>(group,headers,HttpMethod.POST,new URI(request_url));
		logger.info("createGroup: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<GroupDto> response=template.exchange(request, GroupDto.class);
		logger.info("createGroup: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response.getBody();
	}
	
	/*
	 * precondition: Config.getA_token returns a valid a_token.
	 */
	public Boolean importReceiversCSV(String receivers, int group_id) throws URISyntaxException{
		logger.info("importReceiversCSV: Adding receivers-CSV-String to the group " + group_id + " ...");
		String request_url = config.getBase_url() +"v1/groups.json/" + group_id + "/importcsv?token="+config.getA_token();		
		JSONObject body = new JSONObject();
		body.put("data", receivers);
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(body,headers,HttpMethod.POST,new URI(request_url));
		logger.info("importReceiversCSV: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<Boolean> response = template.exchange(request, Boolean.class);
		logger.info("importReceiversCSV: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response.getBody();
	}
	
}
