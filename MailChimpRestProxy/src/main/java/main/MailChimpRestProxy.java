package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.catalina.filters.RemoteAddrFilter;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.opencsv.CSVReader;

import service.AppBean;
import util.Config;

@PropertySource(value = { "classpath:application.properties", "file:./configuration.properties" })
@SpringBootApplication(scanBasePackages={"controllers", "service"})
public class MailChimpRestProxy {
	private static Logger logger;
	
	@Value("${corsPath}")
	private String corsPath;
	@Value("${allowedIpAddress}")
	private String allowedIpAddress;
	
	@Autowired
	public static void main(String[] args) throws JSONException {
		logger = Logger.getLogger(MailChimpRestProxy.class);
		logger.info("Starting MailChimpRestProxy ...");
		ConfigurableApplicationContext context = SpringApplication.run(MailChimpRestProxy.class, args);
		
		
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Please enter the version of the MailChimp Rest-Api (e.g. 3.0) ...");
			String command = br.readLine();
			final String apiVersion = command;
			
			System.out.println("Please enter the api key (you can find it in your MailChimp account settings under Extras/API keys) ...");
			command = br.readLine();
			final String apiKey = command;
			
			System.out.println("Instantiating the configuration ...");
			Config config = new Config(apiVersion,apiKey);
			System.out.println("Config instantiated: " + config.toString());
			
			beanFactory.registerSingleton(config.getClass().getCanonicalName(), config);
			
			while(!command.equals("exit")) {
				command=br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();			
		}
		
		context.stop();
		SpringApplication.exit(context);
		
	}
	
	
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                try {
                	//for testing
                	//String filepath = "." + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator +  "corstest.csv";
                	//String filepath = "." + File.separator + "corstest.csv";
                	CSVReader reader = new CSVReader(new FileReader(corsPath));
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
	
	@Bean
	public FilterRegistrationBean remoteAddressFilter() {

	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    RemoteAddrFilter filter = new RemoteAddrFilter();
	    //filter.setAllow("0:0:0:0:0:0:0:1");
	    filter.setAllow(allowedIpAddress);
	    //filter.setDenyStatus(404);   //default deny response http status
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.addUrlPatterns("/*");
	    
	    return filterRegistrationBean;

	}
	
}
