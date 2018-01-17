package dto;

import java.util.List;

public class ErrorsResponseDto {
	
	private List<ErrorDto> errors;

	public ErrorsResponseDto() {
		
	}

	public List<ErrorDto> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorDto> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "ErrorsResponseDto [errors=" + errors + "]";
	}
	
	
	
	
}
