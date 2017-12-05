package dto;

import java.util.HashMap;
import java.util.Map;

import enumeration.SubscriptionStatus;

public class MemberDto {
	private String email_address;
	private Map<String, Object> merge_fields;
	private SubscriptionStatus status_if_new;  
	//private SubscriptionStatus status;
	//private Map<String, Double> location;
	
	public MemberDto() {
	
	}
	
	/*
	public MemberDto() {
		this.email_address = null;
		this.status_if_new = null;
		this.merge_fields = new HashMap<String, Object>();
		this.merge_fields.put("FNAME", "");
		this.merge_fields.put("LNAME", "");
		this.location = new HashMap<String, Double>();
		this.location.put("latitude", null);
		this.location.put("longitude", null);
	}
	
	*/
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public SubscriptionStatus getStatus_if_new() {
		return status_if_new;
	}
	public void setStatus_if_new(SubscriptionStatus status_if_new) {
		this.status_if_new = status_if_new;
	}
	public Map<String, Object> getMerge_fields() {
		return merge_fields;
	}
	public void setMerge_fields(Map<String, Object> merge_fields) {
		this.merge_fields = merge_fields;
	}
	/*
	public Map<String, Double> getLocation() {
		return location;
	}

	public void setLocation(Map<String, Double> location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "MemberDto [email_address=" + email_address + ", merge_fields=" + merge_fields + ", status_if_new="
				+ status_if_new + ", location=" + location + "]";
	}
	*/	
	
}
