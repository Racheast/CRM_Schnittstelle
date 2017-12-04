package controllers;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import config.Config;
import dto.MembersToAddAndRemoveDto;
import dto.SegmentDto;
import net.minidev.json.JSONObject;
import service.RestService;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailChimpRestController {
	@Autowired
	RestService restService;
	
	@CrossOrigin(origins = "http://localhost:4848")
	@RequestMapping(value="/createSegment", method=RequestMethod.POST/*,produces = MediaType.APPLICATION_JSON_UTF8_VALUE*/)
	public ResponseEntity<JSONObject> createSegment( @RequestParam String vApiPrefix, @RequestParam String baseURL, @RequestParam String vMCKey, @RequestParam String vMCList, @RequestBody SegmentDto segment) throws URISyntaxException {
		Config config = new Config(baseURL,vMCKey,vApiPrefix,vMCList);
		System.out.println("createSegment called!");
		System.out.println("segment received: " + segment.toString());
		return restService.createSegment(config,segment);
	}
	
	@CrossOrigin(origins = "http://localhost:4848")
	@RequestMapping(value="/addOrRemoveMembersFromSegment", method=RequestMethod.POST)
	public ResponseEntity<JSONObject> addOrRemoveMembersFromSegment(@RequestParam String vApiPrefix, @RequestParam String baseURL, @RequestParam String vMCKey, @RequestParam String vMCList, @RequestParam String segment_id, @RequestBody MembersToAddAndRemoveDto membersToAddAndRemove) throws URISyntaxException{
		Config config = new Config(baseURL,vMCKey,vApiPrefix,vMCList);
		System.out.println("addOrRemoveMembersFromSegment() called!");
		System.out.println("Is Integer: " + segment_id + "? " + isInteger(segment_id));
		if(isInteger(segment_id)) {
			return restService.addOrRemoveMembersFromSegment(config, membersToAddAndRemove.getMembers_to_add(), membersToAddAndRemove.getMembers_to_remove(), Integer.parseInt(segment_id));
		}else {
			//ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(HttpStatus.BAD_REQUEST);
			//return response;
			JSONObject error = new JSONObject();
			error.put("Error", "The parameter segment_id could not be parsed to an integer. Check whether segment_id represents an integer value.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}
		
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