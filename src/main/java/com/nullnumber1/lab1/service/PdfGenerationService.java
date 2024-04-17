package com.nullnumber1.lab1.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.nullnumber1.lab1.exception.DocumentGenerationException;
import com.nullnumber1.lab1.exception.SaveDocumentException;
import com.nullnumber1.lab1.model.PaymentDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class PdfGenerationService {

    public byte[] generatePdf(PaymentDocument paymentDocument) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);

            log.info("Generating PDF with: " + paymentDocument.toString());
            document.open();
            document.add(new Paragraph("Payment Document ID: " + paymentDocument.getId()));
            document.add(new Paragraph("Payer INN: " + paymentDocument.getPayerInn()));
            document.add(new Paragraph("Payee INN: " + paymentDocument.getPayeeInn()));
            document.add(new Paragraph("Organization OKTMO: " + paymentDocument.getOrganizationOktmo()));
            document.add(new Paragraph("Payment Type: " + paymentDocument.getPaymentType()));
            document.add(new Paragraph("Amount: " + paymentDocument.getAmount()));
            document.add(new Paragraph("Date of Payment: " + paymentDocument.getDateOfPayment()));

        } catch (DocumentException e) {
            log.error("Error occurred while generating PDF");
            throw new DocumentGenerationException();
        } finally {
            document.close();
        }

        // Save the PDF to a file
        String filePath = "paymentDocument_" + paymentDocument.getId() + ".pdf";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(out.toByteArray());
        } catch (IOException e) {
            log.error("Error occurred while saving PDF to a file");
            throw new SaveDocumentException();
        }

        return out.toByteArray();
    }
}
