package SAMProxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
//https://cube.pos.secutix.com/tnco/apidocs/ExternalCampaignService_latest.html
@SpringBootApplication(scanBasePackages={"controllers", "service"})
public class SamProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamProxyApplication.class, args);
		
		//String soapEndpointUrl = "http://www.webservicex.net/uszip.asmx";
		
		
        //String soapAction = "http://www.webserviceX.NET/GetInfoByCity";
		String samEndoiuntURL = "https://cube.ws.secutix.com/tnco/external-remoting/com.secutix.service.campaign.v1_0.ExternalCampaignService.webservice?wsdl";
		String samMethod = "createOrUpdateTarget";
        
        callSoapWebService(samEndoiuntURL, samMethod);

	}
	
	 private static void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
	        SOAPPart soapPart = soapMessage.getSOAPPart();

	        String myNamespace = "v1";
	        String myNamespaceURI = "http://v1_0.ExternalCampaignService.service.secutix.com/";

	        //String myNamespace = "bla";
	        //String myNamespaceURI = "http://bla";

	        
	        // SOAP Envelope
	        SOAPEnvelope envelope = soapPart.getEnvelope();
	        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

	            /*
	            Constructed SOAP Request Message:
	            <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="http://www.webserviceX.NET">
	                <SOAP-ENV:Header/>
	                <SOAP-ENV:Body>
	                    <myNamespace:GetInfoByCity>
	                        <myNamespace:USCity>New York</myNamespace:USCity>
	                    </myNamespace:GetInfoByCity>
	                </SOAP-ENV:Body>
	            </SOAP-ENV:Envelope>
	            */

	        // SOAP Body
	        /*
	        SOAPBody soapBody = envelope.getBody();
	        SOAPElement soapBodyElem = soapBody.addChildElement("GetInfoByCity", myNamespace);
	        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("USCity", myNamespace);
	        soapBodyElem1.addTextNode("New York");
	        */
	        
	        SOAPHeader soapHeader = envelope.getHeader();
	        
	        SOAPBody soapBody = envelope.getBody();
	        SOAPElement soap_code = soapBody.addChildElement("code", myNamespace);
	        soap_code.addTextNode("ABCD1234");
	        SOAPElement soap_internalName = soapBody.addChildElement("internalName", myNamespace);
	        soap_internalName.addTextNode("myFirstTarget");
	        SOAPElement soap_contactNumbers = soapBody.addChildElement("contactNumbers", myNamespace);
	        soap_contactNumbers.addTextNode("sample contact number");
	        
	    }
	 	
	 /*
	  *  <ns2:Username>CUBE_B2C</ns2:Username> 
	 	 <ns2:Password>P@ssw0rd</ns2:Password>
	  */
	 	private static SOAPMessage getSOAPMessage_createOrUpdateTarget(String username, String password, String code, String internalName, String[] contactNumbers) throws IOException, SOAPException {
	 		String msg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://v1_0.ExternalCampaignService.service.secutix.com/\"\n" + 
	 				"xmlns:ns2=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" + 
	 				"   <soapenv:Header>\n" + 
	 				"      <ns2:Security soapenv:mustUnderstand=\"1\">\n" + 
	 				"         <ns2:UsernameToken>\n" + 
	 				"            <ns2:Username>" + username + "</ns2:Username>\n" + 
	 				"            <ns2:Password>" + password + "</ns2:Password>\n" + 
	 				"         </ns2:UsernameToken>\n" + 
	 				"      </ns2:Security>\n" + 
	 				"   </soapenv:Header>\n" + 
	 				"   <soapenv:Body>\n" + 
	 				"      <v1:createOrUpdateTarget>\n" + 
	 				"         <!--Optional:-->\n" + 
	 				"         <!--<requestId></requestId>-->\n" + 
	 				"         <!--Optional:-->\n" + 
	 				"         <!--<campaignTargetId></campaignTargetId>-->\n" + 
	 				"         <!--Optional:-->\n" + 
	 				"         <code>" + code + "</code>\n" +  //make sure code is not available yet
	 				"         <!--Optional:-->\n" + 
	 				"         <internalName>" + internalName + "</internalName>\n" + 
	 				"         <!--Zero or more repetitions:-->\n";
	 		
	 		for(String contactNumber: contactNumbers) {
	 			msg += "         <contactNumbers>" + contactNumber + "</contactNumbers>\n";
	 		}
	 		
	 		//msg +=  "         <contactNumbers>13</contactNumbers>\n" + 
	 		//		"         <contactNumbers>15</contactNumbers>\n"; 
	 		msg +=	"      </v1:createOrUpdateTarget>\n" + 
	 				"   </soapenv:Body>\n" + 
	 				"</soapenv:Envelope>\n" + 
	 				"";
	 		
	 		InputStream is = new ByteArrayInputStream(msg.getBytes());
	 		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null,is);
	 		return soapMessage;
	 	}
	 	
	 	private SOAPMessage getSOAPMessage_getCampaignTargetDetails(String username, String password, long campaignTargetId)  throws IOException, SOAPException {
	 		String msg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://v1_0.ExternalCampaignService.service.secutix.com/\"  xmlns:ns2=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" + 
	 				"   <soapenv:Header>\n" + 
	 				"      <ns2:Security>\n" + 
	 				"         <ns2:UsernameToken>\n" + 
	 				"            <ns2:Username>" + username + "</ns2:Username>\n" + 
	 				"            <ns2:Password>" + password + "</ns2:Password>\n" +  
	 				"         </ns2:UsernameToken>\n" + 
	 				"      </ns2:Security>\n" + 
	 				"   </soapenv:Header>\n" + 
	 				"   <soapenv:Body>\n" + 
	 				"      <v1:getCampaignTargetDetails>\n" + 
	 				//"      <campaignTargetId>528572592</campaignTargetId>\n" +
	 				"      <campaignTargetId>" + campaignTargetId + "</campaignTargetId>\n" +
	 				"       </v1:getCampaignTargetDetails>\n" + 
	 				"   </soapenv:Body>\n" + 
	 				"</soapenv:Envelope>\n";
	 		
	 		InputStream is = new ByteArrayInputStream(msg.getBytes());
	 		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null,is);
	 		return soapMessage;
	 	}
	 	
	    private static void callSoapWebService(String soapEndpointUrl, String soapAction) {
	        try {
	            // Create SOAP Connection
	            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
	            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

	            // Send SOAP Message to SOAP Server	            
	            String username = "CUBE_B2C";
	            String password = "P@ssw0rd";
	            String code = "Blabla_2";
	            String internalName = "InternalName_of_Blabla_2";
	            String[] contactNumbers = new String[] {"12", "13"};
	            SOAPMessage soapResponse = soapConnection.call(getSOAPMessage_createOrUpdateTarget(username, password, code, internalName, contactNumbers), soapEndpointUrl);
	            
	            // Print the SOAP Response
	            System.out.println("Response SOAP Message:");
	            soapResponse.writeTo(System.out);
	            System.out.println();
	            
	            soapConnection.close();
	        } catch (Exception e) {
	            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
	            e.printStackTrace();
	        }
	    }


	    private static String getSOAPMessageAsString(SOAPMessage soapMessage) {
	        try {

	           TransformerFactory tff = TransformerFactory.newInstance();
	           Transformer tf = tff.newTransformer();

	           // Set formatting
	          
	           tf.setOutputProperty(OutputKeys.INDENT, "yes");
	           tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
	                 "2");
	           
	           Source sc = soapMessage.getSOAPPart().getContent();

	           ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
	           StreamResult result = new StreamResult(streamOut);
	           tf.transform(sc, result);

	           String strMessage = streamOut.toString();
	           return strMessage;
	        } catch (Exception e) {
	           System.out.println("Exception in getSOAPMessageAsString "
	                 + e.getMessage());
	           return null;
	        }

	     }
	    
}
