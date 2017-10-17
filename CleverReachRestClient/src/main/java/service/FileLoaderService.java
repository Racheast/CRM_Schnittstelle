package service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;

import Exception.FileLoaderServiceException;
import config.Config;
import dto.GroupDto;

public class FileLoaderService {
	private Logger logger;

	public FileLoaderService() {
		this.logger = Logger.getLogger(FileLoaderService.class);
	}
	
	public GroupDto loadGroupFromGroupFile(final String input_file_path) throws IOException, FileLoaderServiceException {
		logger.info("loadGroupFromGroupFile: Loading group from the input file at " + input_file_path + " ...");
		try {	
			CSVReader reader = new CSVReader(new FileReader(input_file_path));
		    List<String[]> lines = reader.readAll();
		    reader.close();
		    
		    if(lines.size() == 1 && lines.get(0) != null && lines.get(0).length >= 1 && lines.get(0)[0] != null) {
		    	String[] line = lines.get(0);
		    	GroupDto group = new GroupDto();
		    	group.setName(line[0]);
		    	logger.info("loadGroupFromGroupFile: Group with the name \"" + group.getName() + "\" has been loaded.");
		    	return group;
		    }else {
		    	throw new FileLoaderServiceException("Format of file at " + input_file_path + " is not correct. Group could not be loaded.");
		    }		    
		} catch (FileNotFoundException e) {
			throw new FileLoaderServiceException(e.getMessage());
		}
	}
	
	public String loadReceiversAsCSVFromGroupFile(final String input_file_path) throws IOException, FileLoaderServiceException {
		logger.info("loadReceiversAsCSVFromGroupFile: Loading receivers as CSV String from the input file at " + input_file_path + " ...");
		try {
			CSVReader reader = new CSVReader(new FileReader(input_file_path));
			List<String[]> lines = reader.readAll();
			reader.close();
		    String receivers = "email";
			if(lines != null && lines.size() == 1 && lines.get(0) != null && lines.get(0).length >= 1) {
		    	String[] line = lines.get(0);
		    	for(int i=2; i<line.length; i++) {
		    		String receiver = line[i];
		    		if(receiver != null && receiver.length()>0) {
		    			receivers += "\\n" + receiver;
		    		}else {
				    	throw new FileLoaderServiceException("Format of file at " + input_file_path + " is not correct. Receivers as CSV String could not be loaded.");
		    		}
		    	}
		    	logger.info("loadReceiversAsCSVFromGroupFile: " + (line.length - 2) + " receivers have been loaded.");
		    	return receivers;
		    }else {
		    	throw new FileLoaderServiceException("Format of file at " + input_file_path + " is not correct. Receivers as CSV String could not be loaded.");
		    }
		    
		}catch(FileNotFoundException e) {
			throw new FileLoaderServiceException(e.getMessage());
		}
	}
	
	public Config loadConfigFile(final String config_file_path) throws IOException, FileLoaderServiceException {
		logger.info("loadConfigFile: Loading config file from " + config_file_path + " ..."); 
		try {
			CSVReader reader = new CSVReader(new FileReader(config_file_path));
		    List<String[]> lines = reader.readAll();
		    reader.close();
		    if(lines != null && lines.size() == 2 && lines.get(1) != null && lines.get(1).length == 4) {
		    	String[] line = lines.get(1);
		    	if(line[0] != null && line[0].length()>0 && line[1] != null && line[1].length()>0 && line[2] != null && line[2].length()>0 && line[3] != null && line[3].length()>0) {
		    		String base_url = line[0];
			    	int client_id = Integer.parseInt(line[1]);
			    	String login = line[2];
			    	String password = line[3];
			    	
			    	Config config = new Config();
			    	config.setBase_url(base_url);
			    	config.setClient_id(client_id);
			    	config.setLogin(login);
			    	config.setPassword(password);
			    	logger.info("loadConfigFile: Config has been created.");
			    	return config;
		    	}else {
			    	throw new FileLoaderServiceException("Format of file at " + config_file_path + " is not correct. Config object could not be created!");
		    	}
		    	
		    }else {
		    	throw new FileLoaderServiceException("Format of file at " + config_file_path + " is not correct. Config object could not be created!");
		    }
		}catch(FileNotFoundException e) {
			throw new FileLoaderServiceException(e.getMessage());
		}
	}
}
