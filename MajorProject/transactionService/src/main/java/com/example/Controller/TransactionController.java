package com.example.Controller;


import com.example.DTO.TxnRequestDTO;
import com.example.DTO.TxnStatusDTO;
import com.example.Service.TxnService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class) ;

    @Autowired
    private TxnService txnService ;

    @PostMapping("/txn")
    public ResponseEntity<String> initTransaction(@RequestBody @Valid TxnRequestDTO txnRequestDTO) throws ExecutionException, InterruptedException {
        LOGGER.info("init - Txn started : {}",txnRequestDTO) ;
        String txnID  = txnService.initTransaction(txnRequestDTO);
        return ResponseEntity.accepted().body(txnID);
    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<TxnStatusDTO> getTxnStatus(@PathVariable String transactionId){
        return ResponseEntity.ok(txnService.getStatus(transactionId)) ;
    }

}
