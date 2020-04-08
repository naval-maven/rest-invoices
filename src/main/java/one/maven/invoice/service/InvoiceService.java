package one.maven.invoice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import one.maven.invoice.dto.BaseResponse;
import one.maven.invoice.dto.InvoiceItemResponse;
import one.maven.invoice.dto.InvoiceRequest;
import one.maven.invoice.dto.InvoiceResponse;
import one.maven.invoice.dto.InvoicesResponse;
import one.maven.invoice.dto.ResponseStatus;
import one.maven.invoice.entity.Invoice;
import one.maven.invoice.entity.InvoiceItem;
import one.maven.invoice.repository.InvoiceRespository;

@Service
@Slf4j
public class InvoiceService {

	@Autowired
	private InvoiceRespository invoiceRepository;

	public InvoiceResponse getInvoice(Long invoiceId) {

		InvoiceResponse invoiceResponse = new InvoiceResponse();

		Invoice invoice = null;

		try {
			invoice = invoiceRepository.getOne(invoiceId);
			mapInvoice(invoice, invoiceResponse);
		} catch (EntityNotFoundException ex) {
			invoiceResponse.setStatusCode(ResponseStatus.FAILURE);
			invoiceResponse.setStatusMessage("No invoice is defined with invoiceId " + invoiceId);
			return invoiceResponse;
		}

		invoiceResponse.setStatusCode(ResponseStatus.SUCCESS);
		invoiceResponse.setStatusMessage(BaseResponse.SUCCESS_STATUS_MESSAGE);
		return invoiceResponse;
	}

	public InvoicesResponse getInvoices() {
		InvoicesResponse invoicesResponse = new InvoicesResponse();
		invoicesResponse.setInvoices(new ArrayList<>());

		invoiceRepository.findAll().forEach(invoice -> {
			InvoiceResponse invoiceResponse = new InvoiceResponse();
			mapInvoice(invoice, invoiceResponse);
			invoicesResponse.getInvoices().add(invoiceResponse);
		});

		if (CollectionUtils.isEmpty(invoicesResponse.getInvoices())) {
			invoicesResponse.setStatusCode(ResponseStatus.FAILURE);
			invoicesResponse.setStatusMessage("No invoice is defined");
			return invoicesResponse;
		}
		invoicesResponse.setStatusCode(ResponseStatus.SUCCESS);
		invoicesResponse.setStatusMessage(BaseResponse.SUCCESS_STATUS_MESSAGE);
		return invoicesResponse;
	}

	public BaseResponse createInvoice(InvoiceRequest invoiceRequest) {
		BaseResponse response = new BaseResponse();
		Invoice invoice = new Invoice();
		invoice.setItems(new HashSet<>());
		invoice.setClient(invoiceRequest.getClient());
		invoice.setVatRate(invoiceRequest.getVatRate());
		invoice.setInvoiceDate(invoiceRequest.getInvoiceDate());

		if (!CollectionUtils.isEmpty(invoiceRequest.getLineItems())) {
			invoice.setItems(new HashSet<>());
			invoiceRequest.getLineItems().forEach(item -> {
				InvoiceItem invoiceItem = new InvoiceItem();
				invoiceItem.setDescription(item.getDescription());
				invoiceItem.setQuantity(item.getQuantity());
				invoiceItem.setUnitPrice(item.getUnitPrice());
				invoiceItem.setInvoice(invoice);
				invoice.getItems().add(invoiceItem);
			});
		}
		invoiceRepository.save(invoice);
		log.info("Invoie save with id " + invoice.getId());
		response.setStatusCode(ResponseStatus.SUCCESS);
		response.setStatusMessage(BaseResponse.SUCCESS_STATUS_MESSAGE);
		return response;
	}

	private void mapInvoice(Invoice invoice, InvoiceResponse invoiceResponse) {
		invoiceResponse.setInvoiceId(invoice.getId());
		invoiceResponse.setClient(invoice.getClient());
		invoiceResponse.setVatRate(invoice.getVatRate());
		invoiceResponse.setInvoiceDate(invoice.getInvoiceDate());
		invoiceResponse.setLineItems(new ArrayList<>());
		BigDecimal subTotal = new BigDecimal(0);

		for (InvoiceItem item : invoice.getItems()) {
			InvoiceItemResponse invoiceItemResponse = new InvoiceItemResponse();
			invoiceItemResponse.setDescription(item.getDescription());
			invoiceItemResponse.setQuantity(item.getQuantity());
			invoiceItemResponse.setUnitPrice(item.getUnitPrice());
			BigDecimal lineItemTotal = item.getUnitPrice()
					.multiply(BigDecimal.valueOf(item.getQuantity()))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			invoiceItemResponse.setLineItemTotal(lineItemTotal);
			invoiceResponse.getLineItems().add(invoiceItemResponse);
			subTotal = subTotal.add(lineItemTotal);
		}
		invoiceResponse.setSubTotal(subTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
		BigDecimal vat = invoiceResponse.getSubTotal()
				.multiply(BigDecimal.valueOf(invoiceResponse.getVatRate()))
				.divide(BigDecimal.valueOf(100))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		invoiceResponse.setTotal(invoiceResponse.getSubTotal()
				.add(vat)
				.setScale(2, BigDecimal.ROUND_HALF_UP)
				);
	}

}
