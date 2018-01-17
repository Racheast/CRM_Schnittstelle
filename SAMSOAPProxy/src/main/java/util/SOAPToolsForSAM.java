package util;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;

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
	
	public String getStatusCode(SOAPMessage soapMessage) throws SOAPException {
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
	
	public String getStatusDetail(SOAPMessage soapMessage) throws SOAPException {
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
}
