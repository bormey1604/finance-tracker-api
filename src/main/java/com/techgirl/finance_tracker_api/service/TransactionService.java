package com.techgirl.finance_tracker_api.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.techgirl.finance_tracker_api.dto.TransactionDto;
import com.techgirl.finance_tracker_api.model.MyUser;
import com.techgirl.finance_tracker_api.model.Transaction;
import com.techgirl.finance_tracker_api.model.TransactionType;
import com.techgirl.finance_tracker_api.repository.TransactionRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<TransactionDto> getTransactionByUserId(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        return transactions.stream().map(transaction -> {

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setCategory(transaction.getCategory());
            transactionDto.setType(transaction.getType());
            transactionDto.setAmount(transaction.getAmount());
            transactionDto.setDescription(transaction.getDescription());
            transactionDto.setDate(transaction.getDate());

            return transactionDto;
        }).collect(Collectors.toList());
    }

    public TransactionDto getTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id).get();
        TransactionDto transactionDto = new TransactionDto();

        transactionDto.setId(transaction.getId());
        transactionDto.setCategory(transaction.getCategory());
        transactionDto.setType(transaction.getType());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setDate(transaction.getDate());

        return transactionDto;
    }

    public List<TransactionDto> getTransactionByType(String transactionType) {
        List<Transaction> transactions = transactionRepository.findByType(TransactionType.valueOf(transactionType));

        return transactions.stream().map(transaction -> {

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setCategory(transaction.getCategory());
            transactionDto.setType(transaction.getType());
            transactionDto.setAmount(transaction.getAmount());
            transactionDto.setDescription(transaction.getDescription());
            transactionDto.setDate(transaction.getDate());

            return transactionDto;
        }).collect(Collectors.toList());
    }

    public void exportTransactionsToPdf(HttpServletResponse response, TransactionType type) throws IOException, DocumentException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename="+String.valueOf(type).toLowerCase(Locale.ROOT)+"_transactions.pdf");

        List<TransactionDto> transactions = getTransactionByType(String.valueOf(type));


        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();


        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        Paragraph title = new Paragraph(String.valueOf(type).substring(0, 1).toUpperCase() + String.valueOf(type).substring(1).toLowerCase()+" Transactions Report", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Create table with column headers
        PdfPTable table = new PdfPTable(4); // 4 columns: ID, Amount, Category, Date
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set table headers
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        table.addCell(new PdfPCell(new Phrase("ID", fontHeader)));
        table.addCell(new PdfPCell(new Phrase("Amount", fontHeader)));
        table.addCell(new PdfPCell(new Phrase("Category", fontHeader)));
        table.addCell(new PdfPCell(new Phrase("Date", fontHeader)));

        // Add transaction data to the table
        for (TransactionDto transaction : transactions) {
            table.addCell(String.valueOf(transaction.getId()));
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getCategory());
            table.addCell(String.valueOf(transaction.getDate()));
        }

        // Add the table to the document
        document.add(table);

        // Close the document
        document.close();
    }


}
