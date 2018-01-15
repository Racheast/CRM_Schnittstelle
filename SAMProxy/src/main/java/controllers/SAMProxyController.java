package controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dto.CampaignTargetDto;
import net.minidev.json.JSONObject;
import service.SAMService;

@RestController
public class SAMProxyController {
	
	@Autowired
	private SAMService samService;
	
	@RequestMapping(value="/createOrUpdateTarget", method=RequestMethod.POST)
	public JSONObject createOrUpdateTarget(@RequestBody @Valid CampaignTargetDto campaignTargetDto/*, BindingResult result*/) {
		System.out.println("CONTROLLER: createOrUpdateTarget() called!");
		/*
		if (result.hasErrors()) {
			System.out.println("CONTORLLER: BindingResult result has Errors!");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError e: errors) {
				System.out.println("Object name: " + e.getObjectName() + ", code: " + e.getCode() + "defaultMsg: " + e.getDefaultMessage());
			}
		}
		*/
		return null;
	}
	
	@RequestMapping(value="/getCampaignTargetDetails", method=RequestMethod.GET)
	public Object getCampaignTargetDetails() {
		
		return null;
	}
}
