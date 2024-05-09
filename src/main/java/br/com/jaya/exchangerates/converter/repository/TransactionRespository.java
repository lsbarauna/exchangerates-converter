package br.com.jaya.exchangerates.converter.repository;

import br.com.jaya.exchangerates.converter.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  TransactionRespository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUserId(Long userId);
}
