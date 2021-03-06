package service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.MemberDto;
import dto.SegmentDto;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import util.Config;

import org.springframework.stereotype.Service;


@Service
public class RestService {
	
	private Logger logger;
	/*
	@Value("${apiprefix}")
	private String apiprefix;
	
	@Value("${apikey}")
	private String apikey;
	
	@Value("${listid}")
	private String listid;
	*/
	
	@Autowired
	private ApplicationContext context;
	
	public RestService() throws IOException {
		this.logger = Logger.getLogger(RestService.class);
	}
	
	private String getMD5Hash(String s) throws NoSuchAlgorithmException  {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(s.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}
	
	public JSONObject addOrUpdateListMembers(MemberDto[] members, String list_id) throws URISyntaxException, NoSuchAlgorithmException, JsonProcessingException{
		logger.info("addOrUpdateListMembers: Adding " + members.length + " members ...");
		
		HttpHeaders headers = getHttpHeaders(getConfig());
		RestTemplate template = (new RestTemplateBuilder()).build();
		
		String requestURL = getConfig().getUrl() + "batches"; //POST
		ObjectMapper mapper = new ObjectMapper();
		JSONArray  operations = new JSONArray();
		for(MemberDto member: members) {	
			JSONObject operation = new JSONObject();
			String subscriber_hash = getMD5Hash(member.getEmail_address().toLowerCase());
			String operation_path = "lists/" + list_id + "/members/" + subscriber_hash;
			operation.put("method", "PUT");
			operation.put("path", operation_path);
			operation.put("operation_id", "PUT " + member.getEmail_address());
			operation.put("body", mapper.writeValueAsString(member));
			operations.add(operation);
		}
				
		JSONObject batch = new JSONObject();
		batch.put("operations", operations);
				
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(batch, headers,HttpMethod.POST,new URI(requestURL));
		logger.info("addOrUpdateListMembers: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<JSONObject> response = template.exchange(request, JSONObject.class); 
		logger.info("addOrUpdateListMembers: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response.getBody();
	}
	
	public JSONObject getMemberFromList(String email_address, String list_id) throws NoSuchAlgorithmException, URISyntaxException {
		logger.info("getMemberFromList: Getting member " + email_address + " ...");
		
		HttpHeaders headers = getHttpHeaders(getConfig());
		RestTemplate template = (new RestTemplateBuilder()).build();
		
		String subscriber_hash = getMD5Hash(email_address.toLowerCase());
		String requestURL = getConfig().getUrl() + "lists/" + list_id + "/members/" + subscriber_hash;
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(headers,HttpMethod.GET,new URI(requestURL));
		logger.info("getMemberFromList: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<JSONObject> response = template.exchange(request, JSONObject.class);
		logger.info("getMemberFromList: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response.getBody();
	}
	
	public JSONObject getBatch(int id) throws URISyntaxException {
		logger.info("getBatch: Getting batch with id " + id + " ...");
		
		HttpHeaders headers = getHttpHeaders(getConfig());
		RestTemplate template = (new RestTemplateBuilder()).build();
		
		String requestURL = getConfig().getUrl() + "batches/" + id;
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(headers,HttpMethod.GET,new URI(requestURL));
		logger.info("getBatch: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<JSONObject> response = template.exchange(request, JSONObject.class);
		logger.info("getBatch: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response.getBody();
	}
	
	public JSONObject createSegment(SegmentDto segment, String list_id) throws URISyntaxException{
		logger.info("createSegment: Creating a segment with the group name \"" + segment.getName() + "\" ...");
		
		HttpHeaders headers = getHttpHeaders(getConfig());
		RestTemplate template = (new RestTemplateBuilder()).build();
		String requestURL = getConfig().getUrl() + "lists/" + list_id + "/segments";
		RequestEntity<SegmentDto> request = new RequestEntity<SegmentDto>(segment,headers,HttpMethod.POST,new URI(requestURL));
		logger.info("createSegment: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<JSONObject> response = template.exchange(request, JSONObject.class);
		logger.info("createSegment: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response.getBody();
	}
	 
	public JSONObject addOrRemoveMembersFromSegment(String[] members_to_add, String[] members_to_remove, int segment_id, String list_id) throws URISyntaxException{
		logger.info("addOrRemoveMembersFromSegment: Adding " + members_to_add.length + " and removing " + members_to_remove.length + " members from segment " + segment_id);
		
		HttpHeaders headers = getHttpHeaders(getConfig());
		RestTemplate template = (new RestTemplateBuilder()).build();
		String requestURL = getConfig().getUrl() + "lists/" + list_id + "/segments/" + segment_id;
		JSONObject body = new JSONObject();
		body.put("members_to_add", members_to_add);
		body.put("members_to_remove", members_to_remove);
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(body,headers,HttpMethod.POST,new URI(requestURL));
		logger.info("addOrRemoveMembersFromSegment: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<JSONObject> response = template.exchange(request, JSONObject.class);
		logger.info("addOrRemoveMembersFromSegment: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response.getBody();
	}
	
	private HttpHeaders getHttpHeaders(Config config) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + config.getApiKey());
		return headers;
	}
	
	private Config getConfig() {
		return context.getBean(Config.class);
	}
	
}
