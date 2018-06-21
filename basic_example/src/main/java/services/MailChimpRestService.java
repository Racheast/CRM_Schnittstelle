package services;

import java.io.IOException;

import javax.print.attribute.standard.Media;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import dto.SegmentDto;

public class MailChimpRestService {
	public static void restTest() throws ClientProtocolException, IOException, JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name","SSEButton-Segment");
		jsonObject.put("static_segment", new String[1]);
		
		//System.out.println("JSON MAILCHIMP TEST doPost:" + doPost("{\"name\":\"TestSegment from SSEButton\", \"static_segment\" : []}"));
		//System.out.println("JSON MAILCHIMP TEST doGet:" + doGet());
		System.out.println("JSON MAILCHIMP TEST doPost:" + doPost());
	}
	
	/*
	 * THROWS BAD REQUEST 
	 */
	public static JSONObject doPost() {
		System.out.println("doPost called");
		SegmentDto segmentDto = new SegmentDto();
		segmentDto.setName("SegmentDto from SSEButton");
		segmentDto.setStatic_segment(new String[0]);
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(
				JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		
		WebResource resource = Client.create(/*new DefaultClientConfig()*/clientConfig).resource("https://us13.api.mailchimp.com/3.0/lists/2f13877de4/segments");
        
		WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON);
		builder.type(MediaType.APPLICATION_JSON);
		builder.header("Content-Type", "application/json;charset=UTF-80");
		builder.header(HttpHeaders.AUTHORIZATION, "apikey e0efd872132071dee9ba6c0eb6067e8d-us13");
		System.out.println("Posting to " + resource.getURI());
		return builder.post(JSONObject.class, segmentDto);
	}
	
	public static JSONObject doGet() {
		System.out.println("doGet called");
		WebResource resource = Client.create(new DefaultClientConfig()).resource("https://us13.api.mailchimp.com/3.0/lists/2f13877de4/segments");
        
		WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON);
		builder.type(MediaType.APPLICATION_JSON);
		builder.header("Content-Type", "application/json;charset=UTF-80");
		builder.header(HttpHeaders.AUTHORIZATION, "apikey e0efd872132071dee9ba6c0eb6067e8d-us13");
		System.out.println("Getting from " + resource.getURI());
		
		return builder.get(JSONObject.class);
	}
	
}