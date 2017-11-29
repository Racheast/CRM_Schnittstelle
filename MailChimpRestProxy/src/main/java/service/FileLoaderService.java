package service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;

import config.Config;
import dto.MemberDto;
import dto.SegmentDto;
import enumeration.SubscriptionStatus;
import exception.FileLoaderServiceException;

public class FileLoaderService {
	private Logger logger;

	public FileLoaderService() {
		this.logger = Logger.getLogger(FileLoaderService.class);
	}

	public ArrayList<MemberDto> loadMembersFile(final String input_file_path)
			throws IOException, FileLoaderServiceException {
		logger.info("loadMembersFile: Loading members from the input file at " + input_file_path + " ...");
		ArrayList<MemberDto> members = new ArrayList<MemberDto>();
		try {
			CSVReader reader = new CSVReader(new FileReader(input_file_path));
			List<String[]> lines = reader.readAll();
			reader.close();
			if (lines != null && lines.size() >= 2) {
				for (int i = 1; i < lines.size(); i++) {
					String[] line = lines.get(i); // [0]: EMail, [1]: Nachname, [2]: Vorname, [3]: Status, [4]:
													// Längengrad,
													// [5]: Breitengrad

					MemberDto member = new MemberDto();
					if (line[0] != null && line[0].length() > 0) {
						member.setEmail_address(line[0]);
					} else {
						throw new FileLoaderServiceException(
								"Format of file at " + input_file_path + " is not correct. Members could not be loaded!");
					}
					
					if (line[1] != null) {
						member.getMerge_fields().put("LNAME", line[1]);
					}
					
					if (line[2] != null) {
						member.getMerge_fields().put("FNAME", line[2]);
					}
					
					// set status
					if (line[3] != null && line[3].length() > 0) {
						if (line[3].equals("subscribed")) {
							member.setStatus_if_new(SubscriptionStatus.subscribed);
						} else if (line[3].equals("unsubscribed")) {
							member.setStatus_if_new(SubscriptionStatus.unsubscribed);
						} else if (line[3].equals("cleaned")) {
							member.setStatus_if_new(SubscriptionStatus.cleaned);
						} else if (line[3].equals("pending")) {
							member.setStatus_if_new(SubscriptionStatus.pending);
						}
					}
					// set longitude & latitude
					if (line[4] != null && line[4].length() > 0 && line[5] != null && line[5].length() > 0) {
						line[4] = line[4].replace(',', '.');
						line[5] = line[5].replace(',', '.');

						if (isDouble(line[4]) && isDouble(line[5])) {
							member.getLocation().put("longitude", Double.parseDouble(line[4]));
							member.getLocation().put("latitude", Double.parseDouble(line[5]));
						} else {
							logger.error("loadMembersFile: formats of location parameters of member " + line[0]
									+ " are wrong! Location of " + line[0] + " could not be set.");
						}

					}

					members.add(member);

				}
			} else {
				throw new FileLoaderServiceException(
						"Format of file at " + input_file_path + " is not correct. Members could not be loaded!");
			}
		} catch (FileNotFoundException e) {
			throw new FileLoaderServiceException(e.getMessage());
		}
		logger.info("loadMembersFile: " + members.size() + " members have been loaded from the input file.");
		return members;
	}

	public String[] loadEmailsFromSegmentFile(final String input_file_path)
			throws IOException, FileLoaderServiceException {
		logger.info("loadEmailsFromSegmentFile: Loading email addresses from the input file at " + input_file_path
				+ " ...");
		ArrayList<String> member_email_addresses = new ArrayList<String>();
		try {
			CSVReader reader = new CSVReader(new FileReader(input_file_path));
			List<String[]> lines = reader.readAll();
			reader.close();

			if (lines != null && lines.size() == 1 && lines.get(0) != null && lines.get(0).length >= 1) {
				String[] line = lines.get(0);
				for (int i = 1; i < line.length; i++) {
					String member_email_address = line[i];
					if (member_email_address != null && member_email_address.length() > 0) {
						member_email_addresses.add(member_email_address);
					} else {
						throw new FileLoaderServiceException("Format of file at " + input_file_path
								+ " is not correct. Email addresses from the input file could not be loaded!");
					}
				}
			} else {
				throw new FileLoaderServiceException("Format of file at " + input_file_path
						+ " is not correct. Email addresses from the input file could not be loaded!");
			}
		} catch (FileNotFoundException e) {
			throw new FileLoaderServiceException(e.getMessage());
		}
		String[] arr = new String[member_email_addresses.size()];
		arr = member_email_addresses.toArray(arr);
		logger.info("loadEmailsFromSegmentFile: " + arr.length + " email addresses have been loaded.");
		return arr;
	}

	public SegmentDto loadSegmentFromSegmentFile(final String input_file_path)
			throws IOException, FileLoaderServiceException {
		logger.info("loadSegmentFromSegmentFile: Loading segment from the input file at " + input_file_path + " ...");
		SegmentDto segment = new SegmentDto(null, new String[0]);
		try {
			CSVReader reader = new CSVReader(new FileReader(input_file_path));
			List<String[]> lines = reader.readAll();
			reader.close();

			if (lines != null && lines.size() == 1 && lines.get(0) != null && lines.get(0).length >= 1
					&& lines.get(0)[0] != null && lines.get(0)[0].length() > 0) {
				String[] line = lines.get(0);
				segment.setName(line[0]);
			} else {
				throw new FileLoaderServiceException("Format of file at " + input_file_path
						+ " is not correct. Segment from the input file could not be loaded!");
			}
		} catch (FileNotFoundException e) {
			throw new FileLoaderServiceException(e.getMessage());
		}
		logger.info("loadSegmentFromSegmentFile: Segment with the name \"" + segment.getName() + "\" has been loaded.");
		return segment;
	}

	public Config loadConfigFile(final String config_file_path) throws IOException, FileLoaderServiceException {
		logger.info("loadConfigFile: Loading config file from " + config_file_path + " ...");
		try {
			CSVReader reader = new CSVReader(new FileReader(config_file_path));
			List<String[]> lines = reader.readAll();
			reader.close();

			if (lines != null && lines.size() == 2 && lines.get(1) != null && lines.get(1).length==4) {
				String[] line = lines.get(1);
				String base_url = line[0];
				String vMCKey = line[1];
				String vApiPrefix = line[2];
				String vMCList = line[3];
				logger.info("loadConfigFile: Config has been created.");
				return new Config(base_url, vMCKey, vApiPrefix, vMCList);
			} else {
				throw new FileLoaderServiceException("Format of file at " + config_file_path
						+ " is not correct. Config object could not be created!");
			}
		} catch (FileNotFoundException e) {
			throw new FileLoaderServiceException(e.getMessage());
		}
	}

	private boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
