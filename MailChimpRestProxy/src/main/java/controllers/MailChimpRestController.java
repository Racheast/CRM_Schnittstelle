package controllers;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dto.MemberDto;
import dto.MembersToAddAndRemoveDto;
import dto.SegmentDto;
import net.minidev.json.JSONObject;
import service.AppBean;
import service.RestService;
import util.Config;

import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class MailChimpRestController {
	@Autowired
	private RestService restService;
	
	@RequestMapping(value="/createSegment", method=RequestMethod.POST/*,produces = MediaType.APPLICATION_JSON_UTF8_VALUE*/)
	public JSONObject createSegment(@RequestParam String list_id, @RequestBody SegmentDto segment) throws URISyntaxException {
		return restService.createSegment(segment, list_id);
	}
	
	@RequestMapping(value="/addOrRemoveMembersFromSegment", method=RequestMethod.POST)
	public JSONObject addOrRemoveMembersFromSegment(@RequestParam String list_id, @RequestParam String segment_id, @RequestBody MembersToAddAndRemoveDto membersToAddAndRemove) throws URISyntaxException{
		if(isInteger(segment_id)) {
			return restService.addOrRemoveMembersFromSegment(membersToAddAndRemove.getMembers_to_add(), new String[0], Integer.parseInt(segment_id), list_id);
		}else {
			JSONObject error = new JSONObject();
			error.put("Error", "The parameter segment_id could not be parsed to an integer. Check whether segment_id represents an integer value.");
			return error;
			//return new JSONObject();
			//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}
	}
	
	@RequestMapping(value="/addOrUpdateListMembers", method=RequestMethod.POST)
	public JSONObject addOrUpdateListMembers(@RequestParam String list_id, @RequestBody MemberDto[] members) throws NoSuchAlgorithmException, JsonProcessingException, URISyntaxException{
		return restService.addOrUpdateListMembers(members, list_id);
	}
	
	@RequestMapping(value="/getBatch", method=RequestMethod.GET)
	public JSONObject getBatch(@RequestParam String batch_id) throws NoSuchAlgorithmException, JsonProcessingException, URISyntaxException{
		return restService.getBatch(Integer.parseInt(batch_id));
	}
	
	@RequestMapping(value="/getMemberFromList", method=RequestMethod.GET)
	public JSONObject getMemberFromList(@RequestParam String list_id, @RequestParam String email_address) throws NoSuchAlgorithmException, JsonProcessingException, URISyntaxException{
		return restService.getMemberFromList(email_address, list_id);
	}
	
	
	private boolean isInteger(String s) {
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