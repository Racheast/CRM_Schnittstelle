package service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SAMService {

	private Logger logger;

	public SAMService() throws IOException {
		this.logger = Logger.getLogger(SAMService.class);
	}
	
	public SOAPMessage createOrUpdateTarget(URL soapEndpointURL, String username, String password, String code, String internalName, String[] contactNumbers) throws UnsupportedOperationException, SOAPException, IOException {
		SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage soapRequest = getSOAPMessage_createOrUpdateTarget(username, password, code, internalName, contactNumbers);
		SOAPMessage soapResponse = soapConnection.call(soapRequest, soapEndpointURL);
		soapConnection.close();
		return soapResponse;
	}
	
	public SOAPMessage getCampaignTargetDetails(URL soapEndpointURL, String username, String password, long campaignTargetId) throws UnsupportedOperationException, SOAPException, IOException {
		SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage soapRequest = getSOAPMessage_getCampaignTargetDetails(username, password, campaignTargetId);
		SOAPMessage soapResponse = soapConnection.call(soapRequest, soapEndpointURL);
		soapConnection.close();
		return soapResponse;
	}
	
	private void callSoapWebService(String soapEndpointUrl, String method) {
		try {
			// Create SOAP Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			// Send SOAP Message to SOAP Server
			String username = "CUBE_B2C";
			String password = "P@ssw0rd";
			String code = "Blabla_2";
			String internalName = "InternalName_of_Blabla_2";
			String[] contactNumbers = new String[] { "12", "13" };
			SOAPMessage soapResponse = soapConnection.call(
					getSOAPMessage_createOrUpdateTarget(username, password, code, internalName, contactNumbers),
					soapEndpointUrl);

			// Print the SOAP Response
			System.out.println("Response SOAP Message:");
			soapResponse.writeTo(System.out);
			System.out.println();

			soapConnection.close();
		} catch (Exception e) {
			System.err.println(
					"\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			e.printStackTrace();
		}
	}

	private SOAPMessage getSOAPMessage_createOrUpdateTarget(String username, String password, String code,
			String internalName, String[] contactNumbers) throws IOException, SOAPException {
		String msg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://v1_0.ExternalCampaignService.service.secutix.com/\"\n"
				+ "xmlns:ns2=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n"
				+ "   <soapenv:Header>\n" + "      <ns2:Security soapenv:mustUnderstand=\"1\">\n"
				+ "         <ns2:UsernameToken>\n" + "            <ns2:Username>" + username + "</ns2:Username>\n"
				+ "            <ns2:Password>" + password + "</ns2:Password>\n" + "         </ns2:UsernameToken>\n"
				+ "      </ns2:Security>\n" + "   </soapenv:Header>\n" + "   <soapenv:Body>\n"
				+ "      <v1:createOrUpdateTarget>\n" + "         <!--Optional:-->\n"
				+ "         <!--<requestId></requestId>-->\n" + "         <!--Optional:-->\n"
				+ "         <!--<campaignTargetId></campaignTargetId>-->\n" + "         <!--Optional:-->\n"
				+ "         <code>" + code + "</code>\n" + // make sure code is not available yet
				"         <!--Optional:-->\n" + "         <internalName>" + internalName + "</internalName>\n"
				+ "         <!--Zero or more repetitions:-->\n";

		for (String contactNumber : contactNumbers) {
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

}
