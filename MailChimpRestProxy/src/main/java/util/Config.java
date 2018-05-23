package util;

public class Config {
	private final String apiVersion;
	private final String apiKey;
	private final String apiPrefix;
	private final String url;
	
	public Config(String apiVersion, String apiKey) {
		super();
		this.apiKey = apiKey;
		this.apiPrefix = apiKey.substring(apiKey.lastIndexOf("-")+1);
		this.apiVersion = apiVersion;
		this.url="https://" + apiPrefix + ".api.mailchimp.com/" + this.apiVersion + "/";  //e.g. "https://" + vApiPrefix + ".api.mailchimp.com/3.0/";
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getApiPrefix() {
		return apiPrefix;
	}

	public String getBase_url() {
		return apiVersion;
	}
	
	public String getUrl() {
		return this.url;
	}

	@Override
	public String toString() {
		return "Config [apiVersion=" + apiVersion + ", apiKey=" + apiKey + ", apiPrefix=" + apiPrefix + ", url=" + url
				+ "]";
	}
	
}
