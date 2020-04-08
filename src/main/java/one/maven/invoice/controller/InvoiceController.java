package one.maven.invoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import one.maven.invoice.dto.BaseResponse;
import one.maven.invoice.dto.InvoiceRequest;
import one.maven.invoice.dto.InvoiceResponse;
import one.maven.invoice.dto.InvoicesResponse;
import one.maven.invoice.service.InvoiceService;

@Slf4j
@RestController
public class InvoiceController {
	
	private static final String INVOICES_MAPPING = "invoices";
	
	@Autowired
	private InvoiceService invoiceService;
	
	@GetMapping(INVOICES_MAPPING + "/{invoiceId}")
	public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable Long invoiceId) {
		log.info("get invoice for invoiceId " + invoiceId);
	    return new ResponseEntity<InvoiceResponse>(invoiceService.getInvoice(invoiceId), HttpStatus.OK);
	}
	
	@GetMapping(INVOICES_MAPPING)
	public ResponseEntity<InvoicesResponse> getInvoices() {
	    return new ResponseEntity<InvoicesResponse>(invoiceService.getInvoices(), HttpStatus.OK);
	}

	@PostMapping(INVOICES_MAPPING)
	public ResponseEntity<BaseResponse> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
	    return new ResponseEntity<BaseResponse>(invoiceService.createInvoice(invoiceRequest), HttpStatus.OK);
	}

}
