package br.com.jaya.exchangerates.converter.mapper;

import br.com.jaya.exchangerates.converter.entity.Transaction;
import br.com.jaya.exchangerates.converter.to.TransactionInbound;
import br.com.jaya.exchangerates.converter.to.TransactionOutbound;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionOutbound toTransactionOutbound (Transaction transaction);

    List<TransactionOutbound> toTransactionOutboundList (List<Transaction> transaction);
    Transaction toTransaction (TransactionInbound transactionInbound);
}
