package com.example.Config;


import com.example.Entity.Transaction;
import com.example.Enum.TxnStatusEnum;
import com.example.Repo.TxnRepo;
import com.example.TxnCompletePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.ExecutionException;

@Configuration
public class TransactionKafkaConsumerConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionKafkaConsumerConfig.class) ;

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper() ; // json to java object

    @Autowired
    private TxnRepo txnRepo ;

    @KafkaListener(topics = "${txn.completed.topic}", groupId = "Txn")
    public void consumeTxnCompleted(ConsumerRecord payload) throws JsonProcessingException
    {
        TxnCompletePayload txnCompletePayload = OBJECT_MAPPER.readValue(payload.value().toString(),TxnCompletePayload.class) ;
        MDC.put("requestId",txnCompletePayload.getRequestId());
        LOGGER.info("Consuming Txn-completed from Kafka : {}",txnCompletePayload);

        Transaction transaction = txnRepo.findById(txnCompletePayload.getId()).get() ;
        if(txnCompletePayload.getSuccess()){
            transaction.setStatus(TxnStatusEnum.SUCCESS);
            txnRepo.save(transaction);
        }
        else {
            transaction.setStatus(TxnStatusEnum.FAILED);
            transaction.setReason(txnCompletePayload.getReason());
            txnRepo.save(transaction);
        }

        MDC.clear();

    }
}
