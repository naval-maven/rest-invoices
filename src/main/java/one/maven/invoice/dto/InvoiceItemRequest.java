package one.maven.invoice.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class InvoiceItemRequest {
	
	private String description;
	private Long quantity;
	private BigDecimal unitPrice;

}
