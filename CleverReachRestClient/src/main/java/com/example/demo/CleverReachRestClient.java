package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import Exception.CleverReachRestServiceException;
import Exception.FileLoaderServiceException;
import config.Config;
import dto.GroupDto;
import dto.LoginDto;
import service.FileLoaderService;
import service.RestService;

@SpringBootApplication
public class CleverReachRestClient {
	private static Logger logger;
	public static void main(String[] args) {
		logger = Logger.getLogger(CleverReachRestClient.class);
		ConfigurableApplicationContext context = SpringApplication.run(CleverReachRestClient.class, args);
		FileLoaderService fileLoaderService = new FileLoaderService();

		//args = getTestArgs("login_create_group_import_receivers");
		if(args.length == 3 && args[0] != null && args[0].length() > 0 && args[1] != null && args[1].length() > 0 && args[2] != null && args[2].length() > 0) {
			try {
				final String operation_type = args[0];
				final String config_file_path = args[1];
				final String input_file_path = args[2];
				Config config = fileLoaderService.loadConfigFile(config_file_path);
				RestService restService = new RestService(config);

				if(operation_type.equals("login_create_group_import_receivers")) {  //combined task of login, create_group and import_receivers
					LoginDto login = new LoginDto(config.getClient_id(), config.getLogin(), config.getPassword());
					String a_token = restService.login(login);
					config.setA_token(a_token);
					
					GroupDto group = fileLoaderService.loadGroupFromGroupFile(input_file_path);
					group = restService.createGroup(group);

					String receivers = fileLoaderService.loadReceiversAsCSVFromGroupFile(input_file_path);
					restService.importReceiversCSV(receivers, group.getId());
				}else {
					logger.error("Operation \"" + operation_type + "\" undefined. Please use the following operations: ");
					HashMap<String, String> commands = getCommands();
					Set<String> keys = commands.keySet();
					for(String key: keys) {
						logger.error(key + ": " + commands.get(key));
					}
				}
	
				
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CleverReachRestServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileLoaderServiceException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		}else {
			logger.error("Unsupported input arguments. Please use the following input arguments: \n <operation type> <config file path> <input file path>");
		}
		logger.info("Exiting CleverReachRestClient ...");
		
		context.stop();
		SpringApplication.exit(context);
		
	}
	
	private static HashMap<String, String> getCommands(){
		HashMap<String, String> commands = new HashMap<String, String>();
		commands.put("login_create_group_import_receivers", "Log in with credetials from the config file, create group and add receivers to this group according to the input file.");
		return commands;
	}
	
	private static String[] getTestArgs(String operation_type) {
		String[] args = new String[3];
		if(operation_type.equals("login_create_group_import_receivers")) {
			args[0] = operation_type;
			args[1] = "." + File.separator + "csv-test" + File.separator + "Config_Gruppen.csv";
			args[2] = "." + File.separator + "csv-test" + File.separator + "Receivers.csv";
		}
		return args;
	}
}
