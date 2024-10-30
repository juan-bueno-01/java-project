package com.globant.project.application.ports.in.services;

import com.globant.project.domain.dto.InvoiceClientDTO;
import com.globant.project.domain.dto.InvoiceDTO;

/**
 * InvoiceService
 */
public interface InvoiceService {

    String createInvoiceClient(InvoiceClientDTO clientInvoiceDTO);

    String getInvoiceServiceToken();

    String createInvoice(InvoiceDTO invoiceDTO);

}
