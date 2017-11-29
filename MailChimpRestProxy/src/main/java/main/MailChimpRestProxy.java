package main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import config.Config;
import dto.MemberDto;
import dto.SegmentDto;
import exception.FileLoaderServiceException;
import service.ArgumentsValidator;
import service.ArgumentsValidator.Result;
import service.FileLoaderService;
import service.RestService;


//@ComponentScan({"controllers"})
@SpringBootApplication(scanBasePackages={"controllers", "service"})
public class MailChimpRestProxy {
	private static Logger logger;
	private static Timer timer;
	private static ArrayList<String> errors;
	/*
	 * args[0]: operation to perform
	 * args[1]: config file path
	 * args[2]: input file path
	 * args[3]: max duration in ms [optional]
	 */
	@Autowired
	public static void main(String[] args) throws JSONException {
		logger = Logger.getLogger(MailChimpRestProxy.class);
		logger.info("Starting MailChimpRestProxy ...");
		ConfigurableApplicationContext context = SpringApplication.run(MailChimpRestProxy.class, args);
		/*
		timer = new Timer();
		errors = new ArrayList<String>();
		
		ConfigurableApplicationContext context = SpringApplication.run(MailChimpRestProxy.class, args);
		FileLoaderService fileLoaderService = new FileLoaderService();
		
		//for test purposes only
		args = getTestArgs("create_segment");
		
		Result validationResult = ArgumentsValidator.checkArgs(args);
		
		if(validationResult.isCorrect()) {
			
				try {
					final String operation_type = args[0];
					final String config_file_path = args[1];
					final String input_file_path = args[2];
					
					Config config = fileLoaderService.loadConfigFile(config_file_path);
		
					RestService restService = new RestService(config);
					
					if(args.length == 4) {
						startTimeout(timer, context, Integer.parseInt(args[3]));
					}
					
					if(operation_type.equals("update_members")){
						ArrayList<MemberDto> loadedMembers = fileLoaderService.loadMembersFile(input_file_path);
						restService.addOrUpdateListMembers(loadedMembers);
					}else if(operation_type.equals("create_segment")) {
						SegmentDto segment = fileLoaderService.loadSegmentFromSegmentFile(input_file_path);
						String[] members_to_add = fileLoaderService.loadEmailsFromSegmentFile(input_file_path);
						int segment_id = restService.createSegment(segment).getBody().getAsNumber("id").intValue();
						restService.addOrRemoveMembersFromSegment(members_to_add, new String[0], segment_id);
					}else {
						logger.error("Operation \"" + operation_type + "\" undefined. Please use the following operations: ");
						HashMap<String, String> commands = getCommands();
						Set<String> keys = commands.keySet();
						for(String key: keys) {
							logger.error(key + ": " + commands.get(key));
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (FileLoaderServiceException e) {
					logger.error(e.getMessage());
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}else {
			logger.error(validationResult.getErrors().size() + " argument validation error(s) occured!");
			for(String e:validationResult.getErrors()) {
				logger.error(e);
			}
		}
		logger.info("Exiting MailChimpRestClient ...");
		
		timer.cancel();
		timer.purge();
		
		context.stop();
		SpringApplication.exit(context);
		*/
	}
	
	/*
	 * map-key: command name
	 * map-value: command description
	 */
	private static HashMap<String, String> getCommands(){
		HashMap<String, String> commands = new HashMap<String, String>();
		commands.put("update_members", "Create or updates all members according to the input file.");
		commands.put("create_segment", "Creates a new segment with a given group name and given members according to the input file.");
		return commands;
	}
	
	private static String[] getTestArgs(String operation_type) {
		String[] args = new String[4];
		if(operation_type.equals("create_segment")) {
			args[0] = operation_type;
			args[1] =  "." + File.separator + "csv-test" + File.separator + "Config_Segmente.csv";
			args[2] = "." + File.separator + "csv-test" + File.separator + "Segmente.csv";
			args[3] = "10000";
		}else if(operation_type.equals("update_members")) {
			args[0] = operation_type;
			args[1] = "." + File.separator + "csv-test" + File.separator + "Config_KundenUpdate.csv";
			args[2] = "." + File.separator + "csv-test" + File.separator + "Kunden.csv";
		}
		return args;
	}
	
	private static void startTimeout(Timer timer, ConfigurableApplicationContext context, int duration) {
		timer.scheduleAtFixedRate(new TimerTask(){
			int counter = 0;  //ms counter
			@Override
			public void run() {
				counter++;

				if(counter == duration)	{  
					logger.warn("Application runtime reached the specified maximal duration limit of " + duration + " ms. Forcefully closing the application ...");
					if(context.isActive()) {
						logger.info("Exiting MailChimpRestClient ...");
						context.stop();
						SpringApplication.exit(context);
					}
					timer.cancel();
					timer.purge();
					System.exit(0);
					return;
				}
				
			}
			
		}, 0, 1);
	}
	
	
	
}
