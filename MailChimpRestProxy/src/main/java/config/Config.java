package config;

public class Config {
	private String vMCKey;
	private String vApiPrefix;
	private String vMCList;
	private String base_url;
	
	public Config(String base_url, String vMCKey, String vApiPrefix, String vMCList) {
		super();
		this.vMCKey = vMCKey;
		this.vApiPrefix = vApiPrefix;
		this.vMCList = vMCList;
		this.base_url = base_url;
		//this.base_url="https://" + vApiPrefix + ".api.mailchimp.com/3.0/";
	}

	public String getvMCKey() {
		return vMCKey;
	}

	public void setvMCKey(String vMCKey) {
		this.vMCKey = vMCKey;
	}

	public String getvApiPrefix() {
		return vApiPrefix;
	}

	public void setvApiPrefix(String vApiPrefix) {
		this.vApiPrefix = vApiPrefix;
	}

	public String getvMCList() {
		return vMCList;
	}

	public void setvMCList(String vMCList) {
		this.vMCList = vMCList;
	}

	public String getBase_url() {
		return base_url;
	}
	
	public String generateURL() {
		//return "https://" + vApiPrefix + ".api.mailchimp.com/3.0/";
		return "https://" + vApiPrefix + this.base_url;
	}
	
	public void setBase_url(String base_url) {
		this.base_url = base_url;
	}

	@Override
	public String toString() {
		return "Config [vMCKey=" + vMCKey + ", vApiPrefix=" + vApiPrefix + ", vMCList=" + vMCList + ", base_url="
				+ base_url + "]";
	}

	
	
	
}
