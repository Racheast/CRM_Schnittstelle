package dto;

public class ErrorDto {
	private String objectName;
	private String field;
	private String defaultMessage;
	
	public ErrorDto() {
		
	}
	
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getDefaultMessage() {
		return defaultMessage;
	}
	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}
	@Override
	public String toString() {
		return "ErrorDto [objectName=" + objectName + ", field=" + field + ", defaultMessage=" + defaultMessage + "]";
	}
	
	
}
