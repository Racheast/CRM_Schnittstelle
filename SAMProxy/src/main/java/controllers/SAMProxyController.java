package controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dto.CampaignTargetDto;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
//import net.minidev.json.JSONObject;
import service.SAMService;

@RestController
public class SAMProxyController {
	
	//@Autowired
	//private SAMService samService;
	
	public SAMProxyController() {
		
	}
	
	@RequestMapping(value="/createOrUpdateTarget", method=RequestMethod.POST)
	public JSONObject createOrUpdateTarget(/*@RequestParam String soapEndpointURL, @RequestParam String username, @RequestParam String password,*/ @RequestBody CampaignTargetDto campaignTargetDto/*, BindingResult result*/) throws UnsupportedOperationException, SOAPException, IOException, JSONException {
		SAMService samService = new SAMService();
		System.out.println("Controller: createOrUpdateTarget() called.");
		System.out.println("Controller: campaignTargetDto: " + campaignTargetDto);
		//For testing purposes
		String username = "CUBE_B2C";
		String password = "P@ssw0rd";
		String code = "Blabla_2";
		String internalName = "InternalName_of_Blabla_2";
		String[] contactNumbers = new String[] { "12", "13" };
		String soapEndoiuntURL = "https://cube.ws.secutix.com/tnco/external-remoting/com.secutix.service.campaign.v1_0.ExternalCampaignService.webservice?wsdl";

		System.out.println("Controller: samService=" + samService);
		SOAPMessage soapResponse = samService.createOrUpdateTarget(soapEndoiuntURL, username, password, campaignTargetDto);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapResponse.writeTo(out);
		String soapResponseXMLString = new String(out.toByteArray());
		JSONObject jsonResponse = XML.toJSONObject(soapResponseXMLString);
		/*
		if (result.hasErrors()) {
			System.out.println("CONTORLLER: BindingResult result has Errors!");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError e: errors) {
				System.out.println("Object name: " + e.getObjectName() + ", code: " + e.getCode() + "defaultMsg: " + e.getDefaultMessage());
			}
		}
		*/
		return jsonResponse;
	}
	
	@RequestMapping(value="/getCampaignTargetDetails", method=RequestMethod.GET)
	public Object getCampaignTargetDetails() {
		
		return null;
	}
}
