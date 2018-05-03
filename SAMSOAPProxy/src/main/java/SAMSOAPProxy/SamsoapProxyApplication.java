package SAMSOAPProxy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.opencsv.CSVReader;

//-DsamSoapCredentialsPath=C:\Users\Alex\Documents\SAMSOAPCredentials.csv

@PropertySource(
value = {
        "classpath:application.properties",
        "file:./configuration.properties"       
})
@SpringBootApplication(scanBasePackages = { "controller", "service", "util" })
public class SamsoapProxyApplication {
	private static Logger logger;
	@Value("${corsPath}")
	private String corsPath;
	
	@Autowired
	public static void main(String[] args) {
		logger = Logger.getLogger(SamsoapProxyApplication.class);
		logger.info("Starting SamSoapProxy ...");
		if(/*args.length != 2*/ true==false) {  
			logger.error("Wrong number of parameters!");
			logger.info("1. parameter: Path to cors.csv");
			logger.info("2. parameter: Port on which this SAMSOAPProxy should run.");
		}else {
			//WORKING CODE FOR JAR FILE
			//corsPath = args[0];
			//port = Integer.parseInt(args[1]);
			
			//WORKING CODE FOR TESTING IN ECLIPSE 
			//corsPath = "." + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "corstest.csv";
			//port=8090;
			
			ConfigurableApplicationContext context = SpringApplication.run(SamsoapProxyApplication.class, new String[0]);
	
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				String command = br.readLine();
				while (!command.equals("exit")) {
					command = br.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			context.stop();
			SpringApplication.exit(context);
		}
	}
	
	//sets all cors mappings of this application's rest endpoints according to the file specified in corsPath
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				try {
					//WORKING CODE FOR TESTING IN ECLIPSE
					//String filepath = "." + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "corstest.csv";
					CSVReader reader = new CSVReader(new FileReader(corsPath));  
					List<String[]> lines = reader.readAll();
					reader.close();
					for (String[] line : lines) {
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
	/*
	@Component
	public class ContainerCustomizerBean implements EmbeddedServletContainerCustomizer {
		
		@Override
		public void customize(ConfigurableEmbeddedServletContainer arg0) {
			arg0.setPort(port);	
		}
	}
	*/
	/*
	  	SSL Configuration of application.properties:
	 	server.ssl.key-alias=cert
		server.ssl.key-password=changeit
		server.ssl.key-store=classpath:keystore.jks
		server.ssl.key-store-provider=SUN
		server.ssl.key-store-type=JKS 
	 */
	
}
