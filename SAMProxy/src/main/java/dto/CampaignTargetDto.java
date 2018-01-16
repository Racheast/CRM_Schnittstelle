package dto;

import java.util.Arrays;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty; 

public class CampaignTargetDto {
		
	@NotEmpty
	@Length(max = 8)
	@Pattern(regexp = "^[A-Za-z0-9]+$") //only alphanumeric
	private String code; //mandatory,if campaignTargetId is not provided. Only digits and letts. Max Length 8 chars.
	
	@NotEmpty
	@Length(max = 60)
	private String internalName; //mandatory, if campaignTargetId is not provided. Max Length 60 chars.
	
	@Size(min = 1, max = 500000)
	private String[] contactNumbers; //mandatory. max arraylength = 500000.
	
	public CampaignTargetDto() {
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public String[] getContactNumbers() {
		return contactNumbers;
	}

	public void setContactNumbers(String[] contactNumbers) {
		this.contactNumbers = contactNumbers;
	}

	@Override
	public String toString() {
		return "CampaignTargetDto [code=" + code + ", internalName="
				+ internalName + ", contactNumbers=" + Arrays.toString(contactNumbers) + "]";
	}

	
	
}
