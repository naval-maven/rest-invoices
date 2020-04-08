package one.maven.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InvoiceResponse extends BaseResponse {
	private Long invoiceId;
	private String client;
	private Long vatRate;
	private LocalDate invoiceDate;
	private BigDecimal subTotal;
	private BigDecimal total;
	private List<InvoiceItemResponse> lineItems;
}
