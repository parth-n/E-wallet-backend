package com.example.Repo;

import com.example.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TxnRepo extends JpaRepository<Transaction,Long> {
    Transaction findByTransactionId(String transactionId) ;
}
