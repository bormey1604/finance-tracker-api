package com.techgirl.finance_tracker_api.controller;

import com.itextpdf.text.DocumentException;
import com.techgirl.finance_tracker_api.dto.TransactionDto;
import com.techgirl.finance_tracker_api.model.MyUser;
import com.techgirl.finance_tracker_api.model.Transaction;
import com.techgirl.finance_tracker_api.model.TransactionType;
import com.techgirl.finance_tracker_api.model.response.ApiResponse;
import com.techgirl.finance_tracker_api.service.MailService;
import com.techgirl.finance_tracker_api.service.TransactionService;
import com.techgirl.finance_tracker_api.utility.AuthUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/finance")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private MailService mailService;

    @PostMapping("/transaction")
    public ResponseEntity<ApiResponse> createTransaction(@RequestBody Transaction transaction, HttpServletRequest request) {

        MyUser user = authUtil.getAuthenticatedUser(request);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("401", "Unauthorized: User not found!"));
        }

        transaction.setUser(user);
        transactionService.saveTransaction(transaction);

        return ResponseEntity.ok(new ApiResponse("200", "Successfully added transaction!"));
    }

    @GetMapping("/getTransactionById/{id}")
    public ResponseEntity<ApiResponse> getTransactions(@PathVariable String id) {

        TransactionDto transactions = transactionService.getTransactionById(id);
        Map<String, Object> map = Map.of("transaction", transactions);

        return ResponseEntity.ok(new ApiResponse("200", "Successfully retrieved transactions!",map));
    }

    @GetMapping("/getTransactionsByUser")
    public ResponseEntity<ApiResponse> getTransactions(HttpServletRequest request){

        MyUser user = authUtil.getAuthenticatedUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("401", "Unauthorized: User not found!"));
        }

        List<TransactionDto> transactions = transactionService.getTransactionByUserId(user.getId());
        Map<String, Object> map = Map.of("transactions", transactions);

        return ResponseEntity.ok(new ApiResponse("200", "Successfully retrieved transactions!",map));
    }

    @GetMapping("/getTransactions/{type}")
    public ResponseEntity<ApiResponse> getTransactionType(@PathVariable String type ,HttpServletRequest request){

        MyUser user = authUtil.getAuthenticatedUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("401", "Unauthorized: User not found!"));
        }

        List<TransactionDto> transactions = transactionService.getTransactionByType(type,user);
        Map<String, Object> map = Map.of("transactions", transactions);

        return ResponseEntity.ok(new ApiResponse("200", "Successfully retrieved transactions!",map));
    }

    @GetMapping("/export/transactions")
    public ResponseEntity<?> exportIncomeTransactionsToPdf(@RequestParam(value = "type") TransactionType type, HttpServletRequest request, HttpServletResponse response) {
        MyUser user = authUtil.getAuthenticatedUser(request);

        if (user != null) {
            try {
                byte[] pdfData = transactionService.exportTransactionsToPdf(type, user);
                String receiver = user.getEmail();
                mailService.sendEmailWithAttachment(receiver,"Transactions Report", "Please find your attached file here: ", String.valueOf(type).toLowerCase() ,pdfData);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse("200", "Transactions report has sent to your email." ));

            } catch (IOException | DocumentException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse("500", "Error generating PDF: " + e.getMessage()));
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("401", "Unauthorized: User not found!"));
    }



}
