package br.com.jaya.exchangerates.converter.service;

import br.com.jaya.exchangerates.converter.client.apilayer.ExchangeRatesData;
import br.com.jaya.exchangerates.converter.entity.Transaction;
import br.com.jaya.exchangerates.converter.entity.User;
import br.com.jaya.exchangerates.converter.exception.ApplicationException;
import br.com.jaya.exchangerates.converter.exception.AuthenticationException;
import br.com.jaya.exchangerates.converter.mapper.TransactionMapper;
import br.com.jaya.exchangerates.converter.mapper.UserMapper;
import br.com.jaya.exchangerates.converter.repository.ExchangeRatesDataRapository;
import br.com.jaya.exchangerates.converter.repository.TransactionRespository;
import br.com.jaya.exchangerates.converter.repository.UserRepository;
import br.com.jaya.exchangerates.converter.to.NewApikeyOutbound;
import br.com.jaya.exchangerates.converter.to.TransactionOutbound;
import br.com.jaya.exchangerates.converter.to.TransactionInbound;
import br.com.jaya.exchangerates.converter.to.UserInBound;
import br.com.jaya.exchangerates.converter.to.UserOutbound;
import br.com.jaya.exchangerates.converter.to.NewApikeyInbound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static br.com.jaya.exchangerates.converter.util.EncryptUtil.encrypt;
import static br.com.jaya.exchangerates.converter.util.EncryptUtil.encryptWithRandomSault;

@Service
public class DefaultExchangeRatesService implements ExchangeRatesService {
    @Autowired
    private TransactionRespository transactionRespository;
    @Autowired
    private ExchangeRatesDataRapository exchangeRatesDataRapository;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public TransactionOutbound convertCurrency(TransactionInbound transactionInbound) {
        ExchangeRatesData exchangeRatesData = exchangeRatesDataRapository.findExchangeRatesData();

        Map<String, Double> rates = exchangeRatesData.getRates();

        if (!rates.containsKey(transactionInbound.getTargetCurrency())
                || !rates.containsKey(transactionInbound.getSourceCurrency())) {
            throw new ApplicationException("You entered an invalid or unsupported currency");
        }

        double rateTarget = exchangeRatesData.getRates().get(transactionInbound.getTargetCurrency());
        double rateSource = exchangeRatesData.getRates().get(transactionInbound.getSourceCurrency());
        double rate = rateTarget / rateSource;
        double targetAmount = rate * transactionInbound.getSourceAmount();

        Transaction transaction = saveTransaction(transactionInbound, rate, targetAmount);

        return transactionMapper.toTransactionOutbound(transaction);
    }

    private Transaction saveTransaction(TransactionInbound transactionInbound, double rate, double targetAmount) {
        Transaction transaction = transactionMapper.toTransaction(transactionInbound);
        transaction.setConversionRate(rate);
        transaction.setTargetAmount(targetAmount);
        transaction.setDateTime(LocalDateTime.now());
        return transactionRespository.save(transaction);
    }

    @Override
    public List<TransactionOutbound> listTransactions(Long userId) {
        return transactionMapper.toTransactionOutboundList(transactionRespository.findByUserId(userId));
    }
    @Override
    public UserOutbound createUser(UserInBound userInBound) {

        Optional<User> userOptional =  userRepository.findByEmail(userInBound.getEmail());

        if(userOptional.isPresent()){
            throw new ApplicationException("Email already in use");
        }

        User user = userMapper.toUser(userInBound);

        user.setPassword(encrypt(user.getEmail(), user.getPassword()));
        user.setApikey(encryptWithRandomSault(user.getEmail()));
        user = userRepository.save(user);

        return userMapper.toUserOutbound(user);
    }

    @Override
    public NewApikeyOutbound generateNewApiKey(NewApikeyInbound newApikeyInbound) {

        Optional<User> userOptional = userRepository.findByEmail(newApikeyInbound.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String encryptPassword = encrypt(newApikeyInbound.getEmail(), newApikeyInbound.getPassword());
            if (encryptPassword.equals(user.getPassword())) {
                String newApiKey = encryptWithRandomSault(user.getEmail());
                user.setApikey(newApiKey);
                userRepository.save(user);
                return new NewApikeyOutbound(newApiKey);
            }
        }
        throw new AuthenticationException("Invalid Credentials");
    }
}
