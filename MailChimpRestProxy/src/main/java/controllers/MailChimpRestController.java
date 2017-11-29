package controllers;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import config.Config;
import dto.SegmentDto;
import service.RestService;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailChimpRestController {
	@Autowired
	RestService restService;
	
	@CrossOrigin(origins = "http://localhost:4848")
	@RequestMapping(value="/createSegment",method=RequestMethod.GET)
	public String createSegment( @RequestParam String vApiPrefix, @RequestParam String baseURL, @RequestParam String vMCKey, @RequestParam String vMCList, @RequestBody SegmentDto segment) throws URISyntaxException {
		Config config = new Config(baseURL,vMCKey,vApiPrefix,vMCList);
		System.out.println("createSegment called!");
		return config.toString();
		//return restService.createSegment(config,segment).getBody().getAsNumber("id").intValue();
	}
	
	

}