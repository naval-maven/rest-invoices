package one.maven.invoice.dto;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InvoicesResponse extends BaseResponse {
	private Collection<InvoiceResponse> invoices;
}
