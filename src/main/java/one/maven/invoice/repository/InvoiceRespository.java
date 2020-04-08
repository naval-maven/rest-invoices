package one.maven.invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import one.maven.invoice.entity.Invoice;

@Repository
public interface InvoiceRespository extends JpaRepository<Invoice, Long> {

}
