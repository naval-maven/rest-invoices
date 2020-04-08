package one.maven.invoice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse {

	public static final String SUCCESS_STATUS_MESSAGE = "Transaction processed successfully."; 
	
	private ResponseStatus statusCode;

	private String statusMessage;

}