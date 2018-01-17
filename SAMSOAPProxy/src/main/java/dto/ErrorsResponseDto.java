package dto;

import java.util.List;

public class ErrorsResponseDto {
	
	private List<ValidationErrorDto> validationErrors;
	private SAMErrorDto samError;
		
	public ErrorsResponseDto() {
		
	}

	public List<ValidationErrorDto> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(List<ValidationErrorDto> validationErrors) {
		this.validationErrors = validationErrors;
	}

	public SAMErrorDto getSamError() {
		return samError;
	}

	public void setSamError(SAMErrorDto samError) {
		this.samError = samError;
	}

	@Override
	public String toString() {
		return "ErrorsResponseDto [validationErrors=" + validationErrors + ", samError=" + samError + "]";
	}

	
	
}
