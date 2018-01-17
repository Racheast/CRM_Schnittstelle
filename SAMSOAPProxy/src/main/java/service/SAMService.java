package service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dto.CampaignTargetDto;
import util.SOAPToolsForSAM;

@Service
public class SAMService {

	private Logger logger;
	
	@Autowired
	private SOAPToolsForSAM soapToolsForSAM;
	
	public SAMService() {
		this.logger = Logger.getLogger(SAMService.class);
	}

	public SOAPMessage createOrUpdateTarget(String soapEndpointURL, String username, String password,
			CampaignTargetDto campaignTargetDto) throws UnsupportedOperationException, SOAPException, IOException {
		logger.info("Calling createOrUpdateTarget() by user " + username + " for CampaignTarget "
				+ campaignTargetDto.getCode() + " ...");
		SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage soapRequest = getSOAPMessage_createOrUpdateTarget(username, password, campaignTargetDto);
		SOAPMessage soapResponse = soapConnection.call(soapRequest, soapEndpointURL);
		logger.info("SOAPResponse received. statusCode: " + soapToolsForSAM.getStatusCode(soapResponse) + ", statusDetail: " + soapToolsForSAM.getStatusDetail(soapResponse));
		soapConnection.close();
		return soapResponse;
	}

	public SOAPMessage getCampaignTargetDetails(String soapEndpointURL, String username, String password,
			long campaignTargetId) throws UnsupportedOperationException, SOAPException, IOException {
		logger.info("Calling getCampaignTargetDetails() by user " + username + " for campaignTargetId "
				+ campaignTargetId + " ...");
		SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage soapRequest = getSOAPMessage_getCampaignTargetDetails(username, password, campaignTargetId);
		SOAPMessage soapResponse = soapConnection.call(soapRequest, soapEndpointURL);
		logger.info("SOAPResponse received. statusCode: " + soapToolsForSAM.getStatusCode(soapResponse) + ", statusDetail: " + soapToolsForSAM.getStatusDetail(soapResponse));
		soapConnection.close();
		return soapResponse;
	}

	private SOAPMessage getSOAPMessage_createOrUpdateTarget(String username, String password,
			CampaignTargetDto campaignTargetDto) throws IOException, SOAPException {
		String msg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://v1_0.ExternalCampaignService.service.secutix.com/\"\n"
				+ "xmlns:ns2=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n"
				+ "   <soapenv:Header>\n" + "      <ns2:Security soapenv:mustUnderstand=\"1\">\n"
				+ "         <ns2:UsernameToken>\n" + "            <ns2:Username>" + username + "</ns2:Username>\n"
				+ "            <ns2:Password>" + password + "</ns2:Password>\n" + "         </ns2:UsernameToken>\n"
				+ "      </ns2:Security>\n" + "   </soapenv:Header>\n" + "   <soapenv:Body>\n"
				+ "      <v1:createOrUpdateTarget>\n" + "         <!--Optional:-->\n"
				+ "         <!--<requestId></requestId>-->\n" + "         <!--Optional:-->\n"
				+ "         <!--<campaignTargetId></campaignTargetId>-->\n" + "         <!--Optional:-->\n"
				+ "         <code>" + campaignTargetDto.getCode() + "</code>\n" + // make sure code is not available yet
				"         <!--Optional:-->\n" + "         <internalName>" + campaignTargetDto.getInternalName()
				+ "</internalName>\n" + "         <!--Zero or more repetitions:-->\n";

		for (String contactNumber : campaignTargetDto.getContactNumbers()) {
			msg += "         <contactNumbers>" + contactNumber + "</contactNumbers>\n";
		}

		// msg += " <contactNumbers>13</contactNumbers>\n" +
		// " <contactNumbers>15</contactNumbers>\n";
		msg += "      </v1:createOrUpdateTarget>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>\n" + "";

		InputStream is = new ByteArrayInputStream(msg.getBytes());
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, is);
		return soapMessage;
	}

	private SOAPMessage getSOAPMessage_getCampaignTargetDetails(String username, String password, long campaignTargetId)
			throws IOException, SOAPException {
		String msg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://v1_0.ExternalCampaignService.service.secutix.com/\"  xmlns:ns2=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n"
				+ "   <soapenv:Header>\n" + "      <ns2:Security>\n" + "         <ns2:UsernameToken>\n"
				+ "            <ns2:Username>" + username + "</ns2:Username>\n" + "            <ns2:Password>"
				+ password + "</ns2:Password>\n" + "         </ns2:UsernameToken>\n" + "      </ns2:Security>\n"
				+ "   </soapenv:Header>\n" + "   <soapenv:Body>\n" + "      <v1:getCampaignTargetDetails>\n" +
				// " <campaignTargetId>528572592</campaignTargetId>\n" +
				"      <campaignTargetId>" + campaignTargetId + "</campaignTargetId>\n"
				+ "       </v1:getCampaignTargetDetails>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>\n";

		InputStream is = new ByteArrayInputStream(msg.getBytes());
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, is);
		return soapMessage;
	}
	
	/*
	private static String getSOAPMessageAsString(SOAPMessage soapMessage) {
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
	
	private String getStatusCode(SOAPMessage soapMessage) throws SOAPException {
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
	
	private String getStatusDetail(SOAPMessage soapMessage) throws SOAPException {
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
	*/
}
