package controllers;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import config.Config;
import dto.MemberDto;
import dto.MembersToAddAndRemoveDto;
import dto.SegmentDto;
import net.minidev.json.JSONObject;
import service.RestService;

import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class MailChimpRestController {
	@Autowired
	private RestService restService;
	
	//@CrossOrigin(origins = "http://localhost:4848")
	@RequestMapping(value="/createSegment", method=RequestMethod.POST/*,produces = MediaType.APPLICATION_JSON_UTF8_VALUE*/)
	public JSONObject createSegment( @RequestParam String vApiPrefix, @RequestParam String baseURL, @RequestParam String vMCKey, @RequestParam String vMCList, @RequestBody SegmentDto segment) throws URISyntaxException {
		Config config = new Config(baseURL,vMCKey,vApiPrefix,vMCList);
		return restService.createSegment(config,segment);
	}
	
	//@CrossOrigin(origins = "http://localhost:4848")
	@RequestMapping(value="/addOrRemoveMembersFromSegment", method=RequestMethod.POST)
	public JSONObject addOrRemoveMembersFromSegment(@RequestParam String vApiPrefix, @RequestParam String baseURL, @RequestParam String vMCKey, @RequestParam String vMCList, @RequestParam String segment_id, @RequestBody MembersToAddAndRemoveDto membersToAddAndRemove) throws URISyntaxException{
		Config config = new Config(baseURL,vMCKey,vApiPrefix,vMCList);

		if(isInteger(segment_id)) {
			return restService.addOrRemoveMembersFromSegment(config, membersToAddAndRemove.getMembers_to_add(), new String[0], Integer.parseInt(segment_id));
		}else {
			JSONObject error = new JSONObject();
			error.put("Error", "The parameter segment_id could not be parsed to an integer. Check whether segment_id represents an integer value.");
			return new JSONObject();
			//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}
	}
	
	//@CrossOrigin(origins = "http://localhost:4848")
	@RequestMapping(value="/addOrUpdateListMembers", method=RequestMethod.POST)
	public JSONObject addOrUpdateListMembers(@RequestParam String vApiPrefix, @RequestParam String baseURL, @RequestParam String vMCKey, @RequestParam String vMCList, @RequestBody MemberDto[] members) throws NoSuchAlgorithmException, JsonProcessingException, URISyntaxException{
		Config config = new Config(baseURL,vMCKey,vApiPrefix,vMCList);
		return restService.addOrUpdateListMembers(config, members);
	}
	
	//@CrossOrigin(origins = "http://localhost:4848")
	@RequestMapping(value="/getBatch", method=RequestMethod.GET)
	public JSONObject getBatch(@RequestParam String vApiPrefix, @RequestParam String baseURL, @RequestParam String vMCKey, @RequestParam String vMCList, @RequestParam String batch_id) throws NoSuchAlgorithmException, JsonProcessingException, URISyntaxException{
		Config config = new Config(baseURL,vMCKey,vApiPrefix,vMCList);
		return restService.getBatch(config, batch_id);
	}
	
	//@CrossOrigin(origins = "http://localhost:4848")
	@RequestMapping(value="/getMemberFromList", method=RequestMethod.GET)
	public JSONObject getMemberFromList(@RequestParam String vApiPrefix, @RequestParam String baseURL, @RequestParam String vMCKey, @RequestParam String vMCList, @RequestParam String email_address) throws NoSuchAlgorithmException, JsonProcessingException, URISyntaxException{
		Config config = new Config(baseURL,vMCKey,vApiPrefix,vMCList);
		return restService.getMemberFromList(config, email_address);
	}
	
	
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

}