package controller;

import java.io.IOException;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dto.CampaignTargetDto;
import model.SAMSOAPCredentials;
import dto.SAMErrorDto;
import net.minidev.json.JSONObject;
import service.SAMService;
import util.SAMSOAPCredentialsLoader;
import util.SOAPToolsForSAM;

@RestController
public class SAMProxyController {
	
	@Autowired
	private SAMService samService;
	
	@Value("${samSoapCredentialsPath}")
	private String samSoapCredentialsPath;
	
	private HashMap<String, SAMSOAPCredentials> allSamSoapCredentials;
	
	@PostConstruct
	private void initAllSamSoapCredentials() throws IOException {
		this.allSamSoapCredentials = SAMSOAPCredentialsLoader.getAllSamSoapCredentials(samSoapCredentialsPath);
	}
	
	@RequestMapping(value="/createOrUpdateTarget", method=RequestMethod.POST)
	public ResponseEntity<JSONObject> createOrUpdateTarget(/*@RequestParam String soapEndpointURL, @RequestParam String username, @RequestParam String password,*/ @RequestParam String institutionCode, @RequestBody @Valid CampaignTargetDto campaignTargetDto, BindingResult result) throws UnsupportedOperationException, SOAPException, IOException, JSONException {
		
		if (result.hasErrors()) {
			ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(SOAPToolsForSAM.buildErrorsResponseDtoAsJSONObject(result.getAllErrors(), null),HttpStatus.BAD_REQUEST);
			return response;
		}else {
			try {	
				SAMSOAPCredentials samSoapCredentials = allSamSoapCredentials.get(institutionCode);
				SOAPMessage soapResponse = samService.createOrUpdateTarget(samSoapCredentials.getSoapEndpointURL(), samSoapCredentials.getUsername(), samSoapCredentials.getPassword(), campaignTargetDto);
			
				if(SOAPToolsForSAM.getStatusCode(soapResponse).equals("success")) {
					return new ResponseEntity<JSONObject>(SOAPToolsForSAM.soapMessage_to_JSONObject(soapResponse), HttpStatus.OK);
				}else {
					SAMErrorDto samError = new SAMErrorDto();
					samError.setStatusCode(SOAPToolsForSAM.getStatusCode(soapResponse));
					samError.setStatusDetail(SOAPToolsForSAM.getStatusDetail(soapResponse));
					return new ResponseEntity<JSONObject>(SOAPToolsForSAM.buildErrorsResponseDtoAsJSONObject(null, samError), HttpStatus.BAD_REQUEST);
				}
			}catch (Exception e) {
				SAMErrorDto samError = new SAMErrorDto();
				samError.setStatusCode("exception");
				samError.setStatusDetail("An exception was caught in the SAMSOAPProxy.\n" + e.getMessage());
				return new ResponseEntity<JSONObject>(SOAPToolsForSAM.buildErrorsResponseDtoAsJSONObject(null, samError), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

}
