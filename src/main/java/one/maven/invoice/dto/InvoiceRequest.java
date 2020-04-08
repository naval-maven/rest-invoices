package one.maven.invoice.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceRequest {

	private String client;
	private Long vatRate;
	private LocalDate invoiceDate;
	private List<InvoiceItemRequest> lineItems;
}
