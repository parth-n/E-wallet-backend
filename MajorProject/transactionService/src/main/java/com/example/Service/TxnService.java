package com.example.Service;


import com.example.DTO.TxnRequestDTO;
import com.example.DTO.TxnStatusDTO;
import com.example.Entity.Transaction;
import com.example.Enum.TxnStatusEnum;
import com.example.Repo.TxnRepo;
import com.example.TxnInitPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class TxnService {

    @Autowired
    private TxnRepo txnRepo ;

    @Value("${txn.init.topic}")
    private String txnInitTopic ;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate ;

    private static Logger LOGGER = LoggerFactory.getLogger(TxnService.class);

    public String initTransaction(TxnRequestDTO txnRequestDTO) throws ExecutionException, InterruptedException {
        Transaction transaction = new Transaction() ;
        transaction.setFromUserId(txnRequestDTO.getFromUserId());
        transaction.setToUserId(txnRequestDTO.getToUserId());
        transaction.setAmount(txnRequestDTO.getAmount());
        transaction.setComment(txnRequestDTO.getComment());
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setStatus(TxnStatusEnum.PENDING);

        txnRepo.save(transaction) ;

        TxnInitPayload txnInitPayload = new TxnInitPayload() ;
        txnInitPayload.setId(transaction.getId());
        txnInitPayload.setFromUserId(transaction.getFromUserId());
        txnInitPayload.setToUserId(transaction.getToUserId());
        txnInitPayload.setAmount(transaction.getAmount());
        txnInitPayload.setRequestId(MDC.get("requestId"));

        Future<SendResult<String,Object>> future = kafkaTemplate.send(txnInitTopic,transaction.getFromUserId().toString(),txnInitPayload);
        LOGGER.info("Pushed txnInitPayload to kafka by TxnService : {}",future.get()) ;
        return transaction.getTransactionId() ;
    }


    public TxnStatusDTO getStatus(String txnId){
        Transaction transaction = txnRepo.findByTransactionId(txnId) ;
        TxnStatusDTO txnStatusDTO = new TxnStatusDTO() ;
        if(transaction!=null){
            txnStatusDTO.setStatus(transaction.getStatus().toString());
            txnStatusDTO.setReason(transaction.getReason());
        }
        return txnStatusDTO ;
    }
}
