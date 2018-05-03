package model;

public class SAMSOAPCredentials {
	private final String institutionCode;
	private final String soapEndpointURL;
	private final String username;
	private final String password;
	
	public SAMSOAPCredentials(String institutionCode, String soapEndpointURL, String username, String password) {
		super();
		this.institutionCode = institutionCode;
		this.soapEndpointURL = soapEndpointURL;
		this.username = username;
		this.password = password;
	}

	public String getSoapEndpointURL() {
		return soapEndpointURL;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public String getInstitutionCode() {
		return institutionCode;
	}

	@Override
	public String toString() {
		return "SAMSOAPCredentials [institutionCode=" + institutionCode + ", soapEndpointURL=" + soapEndpointURL
				+ ", username=" + username + ", password=" + password + "]";
	}
	
}
