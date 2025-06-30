package com.example.Service;


import com.example.Entity.Wallet;
import com.example.Repo.WalletRepo;
import com.example.TxnCompletePayload;
import com.example.TxnInitPayload;
import com.example.WalletUpdatedPayload;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class WalletService {

    private static Logger LOGGER = LoggerFactory.getLogger(WalletService.class) ;

    @Autowired
    private WalletRepo walletRepo ;

    @Value("${txn.completed.topic}")
    private String txnCompletedTopic ;

    @Value("${wallet.updated.topic}")
    private String walletUpdatedTopic ;

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate ;

    @Transactional
    public void walletTxn(TxnInitPayload txnInitPayload) throws InterruptedException, ExecutionException {

        Long fromID = txnInitPayload.getFromUserId() ;
        Wallet fromWallet = walletRepo.findByUserId(fromID) ;


        Double transactionAmount = txnInitPayload.getAmount();

        TxnCompletePayload txnCompletePayload = TxnCompletePayload.builder()
                .id(txnInitPayload.getId())
                .requestId(txnInitPayload.getRequestId())
                .build();

        if(fromWallet.getBalance() < txnInitPayload.getAmount()){
            LOGGER.info("Entered low balance case ") ;
            txnCompletePayload.setSuccess(false);
            txnCompletePayload.setReason("Low Balance");
            System.out.println(txnCompletePayload);
        }
        else{

            Long toId = txnInitPayload.getToUserId();
            Wallet toWallet = walletRepo.findByUserId(toId);
            fromWallet.setBalance(fromWallet.getBalance()- transactionAmount);
            toWallet.setBalance(toWallet.getBalance()+transactionAmount);
            txnCompletePayload.setSuccess(true);
            WalletUpdatedPayload walletUpdatedPayload1 = WalletUpdatedPayload.builder()
                    .userEmail(fromWallet.getUserEmail())
                    .balance(fromWallet.getBalance())
                    .requestId(txnInitPayload.getRequestId())
                    .build() ;

            WalletUpdatedPayload walletUpdatedPayload2 = WalletUpdatedPayload.builder()
                    .userEmail(toWallet.getUserEmail())
                    .balance(toWallet.getBalance())
                    .requestId(txnInitPayload.getRequestId())
                    .build();

            Future<SendResult<String,Object>> walletFuture1 = kafkaTemplate.send(walletUpdatedTopic,walletUpdatedPayload1.getUserEmail(),walletUpdatedPayload1) ;
            LOGGER.info("Pushed WalletUpdated Payload to kafka : {}", walletFuture1.get());

            Future<SendResult<String,Object>> walletFuture2 = kafkaTemplate.send(walletUpdatedTopic,walletUpdatedPayload2.getUserEmail(),walletUpdatedPayload2) ;
            LOGGER.info("Pushed WalletUpdated Payload to kafka : {}", walletFuture2.get());


        }
        Future<SendResult<String,Object>> future = kafkaTemplate.send(txnCompletedTopic,txnCompletePayload) ;
        LOGGER.info("Pushed TxnCompleted Payload to kafka : {}", future.get());

    }
}
