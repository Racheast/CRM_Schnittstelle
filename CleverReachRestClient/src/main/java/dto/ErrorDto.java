package dto;

public class ErrorDto {
	private int code;
	private String message;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ErrorDto(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	@Override
	public String toString() {
		return "ErrorDto [code=" + code + ", message=" + message + "]";
	}
	
	
}
