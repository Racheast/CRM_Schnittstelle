package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import service.SAMService;

@RestController
public class SAMProxyController {
	
	@Autowired
	private SAMService samService;
	
	@RequestMapping(value="/createOrUpdateTarget", method=RequestMethod.POST)
	public Object createOrUpdateTarget() {
		
		return null;
	}
	
	@RequestMapping(value="/getCampaignTargetDetails", method=RequestMethod.GET)
	public Object getCampaignTargetDetails() {
		
		return null;
	}
}
