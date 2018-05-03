package util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;

import com.opencsv.CSVReader;

import model.SAMSOAPCredentials;

public class SAMSOAPCredentialsLoader {
	public static HashMap<String,SAMSOAPCredentials> getAllSamSoapCredentials(String path) throws IOException {
		HashMap<String, SAMSOAPCredentials> allSamSoapCredentials = new HashMap<String, SAMSOAPCredentials>();
		
		String encodedFileContent= new String(Files.readAllBytes(Paths.get(path)));
		InputStream decodedInputStream = new ByteArrayInputStream(Base64.decodeBase64(encodedFileContent));
		CSVReader reader = new CSVReader(new InputStreamReader(decodedInputStream));
		List<String[]> lines = reader.readAll();
		reader.close();
		
		for (String[] line : lines) {
			if(!allSamSoapCredentials.containsKey(line[0])) {
				SAMSOAPCredentials samSoapCredentials = new SAMSOAPCredentials(line[0], line[1], line[2], line[3]);
				allSamSoapCredentials.put(line[0], samSoapCredentials);
			}
		}
		
		return allSamSoapCredentials;
	}
}
