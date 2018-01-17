package SAMSOAPProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
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

//https://cube.pos.secutix.com/tnco/apidocs/ExternalCampaignService_latest.html
@SpringBootApplication(scanBasePackages = { "controller", "service", "util" })
public class SamsoapProxyApplication {
	private static Logger logger;

	@Autowired
	public static void main(String[] args) {
		logger = Logger.getLogger(SamsoapProxyApplication.class);
		logger.info("Starting SamSoapProxy ...");
		ConfigurableApplicationContext context = SpringApplication.run(SamsoapProxyApplication.class, args);

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

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				try {
					String filepath = "." + File.separator + "src" + File.separator + "main" + File.separator
							+ "resources" + File.separator + "corstest.csv";
					CSVReader reader = new CSVReader(new FileReader(filepath));
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
}
