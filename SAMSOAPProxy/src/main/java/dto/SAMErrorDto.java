package dto;

public class SAMErrorDto {
	private String statusCode;
	private String statusDetail;
	
	public SAMErrorDto() {
		
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDetail() {
		return statusDetail;
	}

	public void setStatusDetail(String statusDetail) {
		this.statusDetail = statusDetail;
	}

	@Override
	public String toString() {
		return "SAMErrorDto [statusCode=" + statusCode + ", statusDetail=" + statusDetail + "]";
	}
	
	
}
