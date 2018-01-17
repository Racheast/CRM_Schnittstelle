package controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.CampaignTargetDto;
import dto.ErrorDto;
import dto.ErrorsResponseDto;

import org.json.XML;

import net.minidev.json.JSONObject;
import service.SAMService;

//@EnableWebMvc
@RestController
public class SAMProxyController {
	
	@Autowired
	private SAMService samService;
	

	@RequestMapping(value="/createOrUpdateTarget", method=RequestMethod.POST)
	public JSONObject createOrUpdateTarget(@RequestParam String soapEndpointURL, @RequestParam String username, @RequestParam String password, @RequestBody @Valid CampaignTargetDto campaignTargetDto, BindingResult result) throws UnsupportedOperationException, SOAPException, IOException, JSONException {
		//SAMService samService = new SAMService();
		System.out.println("Controller: createOrUpdateTarget() called.");
		System.out.println("Controller: campaignTargetDto: " + campaignTargetDto);
		System.out.println("Controller: samService=" + samService);
		SOAPMessage soapResponse = samService.createOrUpdateTarget(soapEndpointURL, username, password, campaignTargetDto);
		
		if (result.hasErrors()) {
			return buildErrorsResponseDtoAsJSONObject(result.getAllErrors());
		}else {
			return soapMessage_to_JSONObject(soapResponse);
		}
	}
	
	@RequestMapping(value="/getCampaignTargetDetails", method=RequestMethod.GET)
	public Object getCampaignTargetDetails() {
		//TODO
		return null;
	}
	
	private JSONObject soapMessage_to_JSONObject(SOAPMessage soapMessage) throws SOAPException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapMessage.writeTo(out);
		String soapResponseXMLString = new String(out.toByteArray());
		String jsonString=org.json.XML.toJSONObject(soapResponseXMLString).toString();
		return new ObjectMapper().readValue(jsonString, JSONObject.class);
	}
	
	private ErrorsResponseDto buildErrorsResponseDto(List<ObjectError> objectErrors) {
		ErrorsResponseDto errorsResponseDto = new ErrorsResponseDto();
		ArrayList<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
		for(ObjectError e: objectErrors) {
			ErrorDto errorDto = new ErrorDto();
			errorDto.setObjectName(e.getObjectName());
			errorDto.setField(((FieldError) e).getField());
			errorDto.setDefaultMessage(e.getDefaultMessage());
			errorDtos.add(errorDto);
		}
		errorsResponseDto.setErrors(errorDtos);
		return errorsResponseDto;
	}
	
	private JSONObject buildErrorsResponseDtoAsJSONObject(List<ObjectError> objectErrors) throws IOException {
		ErrorsResponseDto errorResponseDto = buildErrorsResponseDto(objectErrors);
		String jsonString = new ObjectMapper().writeValueAsString(errorResponseDto);
		return new ObjectMapper().readValue(jsonString, JSONObject.class);
	}

	
}
