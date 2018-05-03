package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ErrorsResponseDto;
import dto.SAMErrorDto;
import dto.ValidationErrorDto;
import net.minidev.json.JSONObject;

@Service
public class SOAPToolsForSAM {
	
	public SOAPToolsForSAM() {
		
	}
	
	public static String getSOAPMessageAsString(SOAPMessage soapMessage) {
		try {

			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();

			// Set formatting

			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			Source sc = soapMessage.getSOAPPart().getContent();

			ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(streamOut);
			tf.transform(sc, result);

			String strMessage = streamOut.toString();
			return strMessage;
		} catch (Exception e) {
			System.out.println("Exception in getSOAPMessageAsString " + e.getMessage());
			return null;
		}

	}
	
	public static String getStatusCode(SOAPMessage soapMessage) throws SOAPException {
		Iterator<SOAPBodyElement> iterator = soapMessage.getSOAPBody().getChildElements();
		if(iterator.hasNext()) {
			Iterator<SOAPBodyElement> iterator2 = iterator.next().getChildElements();
			if (iterator2.hasNext()) {
				Name name = soapMessage.getSOAPPart().getEnvelope().createName("statusCode");
				Iterator<SOAPBodyElement> iterator3 = iterator2.next().getChildElements(name);
				if (iterator3.hasNext()) {
					return iterator3.next().getTextContent();
				}
			}
		}
		return "[statusCode could not be found in the SOAPResponse]";
	}
	
	public static String getStatusDetail(SOAPMessage soapMessage) throws SOAPException {
		Iterator<SOAPBodyElement> iterator = soapMessage.getSOAPBody().getChildElements();
		if(iterator.hasNext()) {
			Iterator<SOAPBodyElement> iterator2 = iterator.next().getChildElements();
			if (iterator2.hasNext()) {
				Name name = soapMessage.getSOAPPart().getEnvelope().createName("statusDetail");
				Iterator<SOAPBodyElement> iterator3 = iterator2.next().getChildElements(name);
				if (iterator3.hasNext()) {
					return iterator3.next().getTextContent();
				}
			}
		}
		return "[statusDetail could not be found in the SOAPResponse]";
	}
	
	public static JSONObject soapMessage_to_JSONObject(SOAPMessage soapMessage) throws SOAPException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapMessage.writeTo(out);
		String soapResponseXMLString = new String(out.toByteArray());
		String jsonString=org.json.XML.toJSONObject(soapResponseXMLString).toString();
		return new ObjectMapper().readValue(jsonString, JSONObject.class);
	}
	
	public static ErrorsResponseDto buildErrorsResponseDto(List<ObjectError> validationErrors, SAMErrorDto samError) {
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
	
	public static JSONObject buildErrorsResponseDtoAsJSONObject(List<ObjectError> validationErrors, SAMErrorDto samError) throws IOException {
		ErrorsResponseDto errorResponseDto = buildErrorsResponseDto(validationErrors, samError);
		String jsonString = new ObjectMapper().writeValueAsString(errorResponseDto);
		return new ObjectMapper().readValue(jsonString, JSONObject.class);
	}
	
}
