package SAMProxy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.xml.soap.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
//https://cube.pos.secutix.com/tnco/apidocs/ExternalCampaignService_latest.html
@SpringBootApplication(scanBasePackages={"controllers", "service"})
public class SamSoapProxy {
	private static Logger logger;
	
	@Autowired
	public static void main(String[] args) {
		//SpringApplication.run(SamSoapProxy.class, args);
		//String soapEndpointUrl = "http://www.webservicex.net/uszip.asmx";
        //String soapAction = "http://www.webserviceX.NET/GetInfoByCity";
		
		logger = Logger.getLogger(SamSoapProxy.class);
		logger.info("Starting SamSoapProxy ...");
		ConfigurableApplicationContext context = SpringApplication.run(SamSoapProxy.class, args);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String command = br.readLine();
			while(!command.equals("exit")) {
				command=br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();			
		}
		
		context.stop();
		SpringApplication.exit(context);
		
		String samEndoiuntURL = "https://cube.ws.secutix.com/tnco/external-remoting/com.secutix.service.campaign.v1_0.ExternalCampaignService.webservice?wsdl";
		String samMethod = "createOrUpdateTarget";
        
       // callSoapWebService(samEndoiuntURL, samMethod);

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
	    
	    @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurerAdapter() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                try {
	                	String filepath = "." + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator +  "corstest.csv";
	                	//String filepath = "." + File.separator + "corstest.csv";
	                	CSVReader reader = new CSVReader(new FileReader(filepath));
	                	List<String[]> lines = reader.readAll();
	        			reader.close();
	        			for(String[] line : lines) {
	        				registry.addMapping(line[0]).allowedOrigins(Arrays.copyOfRange(line, 1, line.length)); 
	        			}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            }
	        };
	    }
}
