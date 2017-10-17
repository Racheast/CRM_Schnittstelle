package service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import dto.MemberDto;
import dto.SegmentDto;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class RestService {
	private Logger logger;
	private RestTemplate template;
	private HttpHeaders headers;
	private Config config;
	
	public RestService(Config config) throws IOException {
		this.config = config;
		this.logger = Logger.getLogger(RestService.class);
		this.template = (new RestTemplateBuilder()).build();
		this.headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + config.getvMCKey());
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
	
	public ResponseEntity<JSONObject> addOrUpdateListMembers(ArrayList<MemberDto> members_to_add) throws URISyntaxException, NoSuchAlgorithmException, JsonProcessingException{
		logger.info("addOrUpdateListMembers: Adding " + members_to_add.size() + " members ...");
		String requestURL = config.getBase_url() + "batches"; //POST
		ObjectMapper mapper = new ObjectMapper();
		JSONArray  operations = new JSONArray();
		for(MemberDto member: members_to_add) {	
			JSONObject operation = new JSONObject();
			String subscriber_hash = getMD5Hash(member.getEmail_address().toLowerCase());
			String operation_path = "lists/" + config.getvMCList() + "/members/" + subscriber_hash;
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
		return response;
	}
	
	public ResponseEntity<JSONObject> getMemberFromList(String email_address) throws NoSuchAlgorithmException, URISyntaxException {
		logger.info("getMemberFromList: Getting member " + email_address + " ...");
		String subscriber_hash = getMD5Hash(email_address.toLowerCase());
		String requestURL = config.getBase_url() + "lists/" + config.getvMCList() + "/members/" + subscriber_hash;
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(headers,HttpMethod.GET,new URI(requestURL));
		logger.info("getMemberFromList: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<JSONObject> response = template.exchange(request, JSONObject.class);
		logger.info("getMemberFromList: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response;
	}
	
	public ResponseEntity<JSONObject> getBatch(String id) throws URISyntaxException {
		logger.info("getBatch: Getting batch with id " + id + " ...");
		String requestURL = config.getBase_url() + "batches/" + id;
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(headers,HttpMethod.GET,new URI(requestURL));
		logger.info("getBatch: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<JSONObject> response = template.exchange(request, JSONObject.class);
		logger.info("getBatch: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response;
	}
	
	public ResponseEntity<JSONObject> createSegment(SegmentDto segment) throws URISyntaxException{
		logger.info("createSegment: Creating a segment with the group name \"" + segment.getName() + "\" ...");
		String requestURL = config.getBase_url() + "lists/" + config.getvMCList() + "/segments";
		RequestEntity<SegmentDto> request = new RequestEntity<SegmentDto>(segment,headers,HttpMethod.POST,new URI(requestURL));
		logger.info("createSegment: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<JSONObject> response = template.exchange(request, JSONObject.class);
		logger.info("createSegment: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response;
	}
	 
	public ResponseEntity<JSONObject> addOrRemoveMembersFromSegment(String[] members_to_add, String[] members_to_remove, int segment_id) throws URISyntaxException{
		logger.info("addOrRemoveMembersFromSegment: Adding " + members_to_add.length + " and removing " + members_to_remove.length + " members from segment " + segment_id);
		String requestURL = config.getBase_url() + "lists/" + config.getvMCList() + "/segments/" + segment_id;
		JSONObject body = new JSONObject();
		body.put("members_to_add", members_to_add);
		body.put("members_to_remove", members_to_remove);
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(body,headers,HttpMethod.POST,new URI(requestURL));
		logger.info("addOrRemoveMembersFromSegment: " + request.getMethod() + " " + request.getUrl().toString());
		ResponseEntity<JSONObject> response = template.exchange(request, JSONObject.class);
		logger.info("addOrRemoveMembersFromSegment: " + request.getMethod() + " completed. Response status = " + response.getStatusCodeValue());
		return response;
	}
	/*
	private static ResponseEntity<JSONObject> addOrRemoveMembersFromSegment(String baseURL, String list_id, String segment_id, String apiKey, String[] members_to_add, String[] members_to_remove) throws URISyntaxException, JSONException{
		String requestURL = baseURL + "lists/" + list_id + "/segments/"+segment_id;
		logger.info("addOrRemoveMembersFromSegment() requestURL: " + requestURL);		
		JSONObject body = new JSONObject();
		body.put("members_to_add", members_to_add);
		body.put("members_to_remove", members_to_remove);

		logger.info("addOrRemoveMembersFromSegment(): body: " + body);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + apiKey);
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(body,headers,HttpMethod.POST,new URI(requestURL));

		return template.exchange(request, JSONObject.class);
	}
	
	private static ResponseEntity<JSONObject> getMembersOfSegment(String baseURL, String list_id, String segment_id, String apiKey) throws URISyntaxException{
		String requestURL = baseURL + "lists/" + list_id + "/segments/"+segment_id+"/members";
		logger.info("getMembersOfSegment() requestURL: " + requestURL);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + apiKey);
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(headers,HttpMethod.GET,new URI(requestURL));

		return template.exchange(request, JSONObject.class);
	}
	
	private static ResponseEntity<JSONObject> addNewListMember(String baseURL, String list_id, String apiKey, String emailAddress, String status) throws URISyntaxException {
		String requestURL = baseURL + "lists/" + list_id + "/members";
		logger.info("addNewListMember() requestURL: " + requestURL);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + apiKey);
				
		JSONObject body = new JSONObject();
		body.put("email_address", emailAddress);
		body.put("status", status);
		logger.info("addNewListMember(): body: " + body);
		
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(body,headers,HttpMethod.POST,new URI(requestURL));

		return template.exchange(request, JSONObject.class);
	}
	
	
	
	private static ResponseEntity<JSONObject> addOrUpdateListMember(String baseURL, String list_id, String apiKey, String emailAddress, String status_if_new) throws NoSuchAlgorithmException, URISyntaxException{
		String subscriber_hash = getMD5Hash(emailAddress.toLowerCase());
		String requestURL = baseURL + "lists/" + list_id + "/members/" + subscriber_hash;
		logger.info("addOrUpdateListMember() requestURL: " + requestURL);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + apiKey);
		
		JSONObject body = new JSONObject();
		body.put("email_address", emailAddress);
		body.put("status_if_new", status_if_new);		
		logger.info("addOrUpdateListMember(): body: " + body);
		
		MemberDto memberDto = new MemberDto(emailAddress, status_if_new, null,null);
		
		RequestEntity<MemberDto> request = new RequestEntity<MemberDto>(memberDto,headers,HttpMethod.PUT,new URI(requestURL));
		
		//RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(body,headers,HttpMethod.PUT,new URI(requestURL));
		
		return template.exchange(request, JSONObject.class);
	}
	
	private static ResponseEntity<JSONObject> getInformationAboutSegment(String baseURL, String list_id, String segment_id, String apiKey) throws URISyntaxException{
		String requestURL = baseURL + "lists/" + list_id + "/segments/" + segment_id;
		logger.info("getInformationAboutSegment() requestURL: " + requestURL);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + apiKey);
		
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(headers,HttpMethod.GET,new URI(requestURL));
		
		return template.exchange(request, JSONObject.class);
	}
	
	private static ResponseEntity<JSONObject> getMemberFromList(String baseURL, String list_id, String email_address, String apiKey) throws URISyntaxException, NoSuchAlgorithmException{
		String subscriber_hash = getMD5Hash(email_address.toLowerCase());
		String requestURL = baseURL + "lists/" + list_id + "/members/" + subscriber_hash;
		logger.info("getInformationAboutSegment() requestURL: " + requestURL);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + apiKey);
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(headers,HttpMethod.GET,new URI(requestURL));
		
		return template.exchange(request, JSONObject.class);
	}
	
	
	//not the actual method! just for learning&tryouts
	 
	private static ResponseEntity<JSONObject> addOrUpdateListMembers(String baseURL, String list_id, String apiKey, List<String> email_addresses) throws NoSuchAlgorithmException, URISyntaxException{
		String requestURL = baseURL + "/batches"; //POST
		//JSONObject[] operations = new JSONObject[email_addresses.size()];
		JSONArray  operations = new JSONArray();
		for(int i=0; i<email_addresses.size();i++) {
			JSONObject operation = new JSONObject();
			JSONObject body = new JSONObject();

			String email_address = email_addresses.get(i);
			String subscriber_hash = getMD5Hash(email_address.toLowerCase());
			//String operation_path =  File.separator + "lists" + File.separator + list_id + File.separator + "members" + File.separator + subscriber_hash;
			String operation_path = "lists/" + list_id + "/members/" + subscriber_hash;
			//String operation_path = "lists/8af007a903/members/f45ac0bda4f58f64bf3a71d60f4f8e87";
			body.put("email_address", email_address);
			body.put("status_if_new", "subscribed");
			
			operation.put("method", "PUT");
			operation.put("path", operation_path);
			operation.put("operation_id", "PUT " + email_address);
			operation.put("body", body.toJSONString());
			operations.add(operation);
		}
		
		
		
		JSONObject batch = new JSONObject();
		batch.put("operations", operations);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + apiKey);
		
		//System.out.println("addOrUpdateMEmbers: body: " + batch.toJSONString());
		
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(batch, headers,HttpMethod.POST,new URI(requestURL));
		
		return template.exchange(request, JSONObject.class);
	}
	
	private static ResponseEntity<JSONObject> getBatches(String baseURL, String apiKey) throws URISyntaxException{
		String requestURL = baseURL + "/batches"; 
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "apikey " + apiKey);
		RequestEntity<JSONObject> request = new RequestEntity<JSONObject>(headers,HttpMethod.GET,new URI(requestURL));
		return template.exchange(request, JSONObject.class);
	}
	 */
}
