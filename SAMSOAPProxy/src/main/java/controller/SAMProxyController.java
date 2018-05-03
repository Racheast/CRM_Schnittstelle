package controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CampaignTargetDto;
import dto.ValidationErrorDto;
import model.SAMSOAPCredentials;
import dto.ErrorsResponseDto;
import dto.SAMErrorDto;
import net.minidev.json.JSONObject;
import service.SAMService;
import util.SAMSOAPCredentialsLoader;
import util.SOAPToolsForSAM;

@RestController
public class SAMProxyController {
	
	@Autowired
	private SAMService samService;
	
	@Autowired
	private SOAPToolsForSAM soapToolsForSAM;
	
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
			ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(buildErrorsResponseDtoAsJSONObject(result.getAllErrors(), null),HttpStatus.BAD_REQUEST);
			return response;
		}else {
			try {	
				SAMSOAPCredentials samSoapCredentials = allSamSoapCredentials.get(institutionCode);
				SOAPMessage soapResponse = samService.createOrUpdateTarget(samSoapCredentials.getSoapEndpointURL(), samSoapCredentials.getUsername(), samSoapCredentials.getPassword(), campaignTargetDto);
			
				if(soapToolsForSAM.getStatusCode(soapResponse).equals("success")) {
					return new ResponseEntity<JSONObject>(soapMessage_to_JSONObject(soapResponse), HttpStatus.OK);
				}else {
					SAMErrorDto samError = new SAMErrorDto();
					samError.setStatusCode(soapToolsForSAM.getStatusCode(soapResponse));
					samError.setStatusDetail(soapToolsForSAM.getStatusDetail(soapResponse));
					return new ResponseEntity<JSONObject>(buildErrorsResponseDtoAsJSONObject(null, samError), HttpStatus.BAD_REQUEST);
				}
			}catch (Exception e) {
				SAMErrorDto samError = new SAMErrorDto();
				samError.setStatusCode("exception");
				samError.setStatusDetail("An exception was caught in the SAMSOAPProxy.\n" + e.getMessage());
				return new ResponseEntity<JSONObject>(buildErrorsResponseDtoAsJSONObject(null, samError), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	private JSONObject soapMessage_to_JSONObject(SOAPMessage soapMessage) throws SOAPException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapMessage.writeTo(out);
		String soapResponseXMLString = new String(out.toByteArray());
		String jsonString=org.json.XML.toJSONObject(soapResponseXMLString).toString();
		return new ObjectMapper().readValue(jsonString, JSONObject.class);
	}
	
	private ErrorsResponseDto buildErrorsResponseDto(List<ObjectError> validationErrors, SAMErrorDto samError) {
		ErrorsResponseDto errorsResponseDto = new ErrorsResponseDto();
		if(validationErrors != null) {
			ArrayList<ValidationErrorDto> validationErrorDtos = new ArrayList<ValidationErrorDto>();
			for(ObjectError e: validationErrors) {
				ValidationErrorDto validationErrorDto = new ValidationErrorDto();
				validationErrorDto.setObjectName(e.getObjectName());
				validationErrorDto.setField(((FieldError) e).getField());
				validationErrorDto.setDefaultMessage(e.getDefaultMessage());
				validationErrorDtos.add(validationErrorDto);
			}
			errorsResponseDto.setValidationErrors(validationErrorDtos);
		}
		if(samError != null) {
			errorsResponseDto.setSamError(samError);
		}
		return errorsResponseDto;
	}
	
	private JSONObject buildErrorsResponseDtoAsJSONObject(List<ObjectError> validationErrors, SAMErrorDto samError) throws IOException {
		ErrorsResponseDto errorResponseDto = buildErrorsResponseDto(validationErrors, samError);
		String jsonString = new ObjectMapper().writeValueAsString(errorResponseDto);
		return new ObjectMapper().readValue(jsonString, JSONObject.class);
	}
	
}
