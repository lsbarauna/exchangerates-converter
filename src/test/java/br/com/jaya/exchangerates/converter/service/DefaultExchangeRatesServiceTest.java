package br.com.jaya.exchangerates.converter.service;

import br.com.jaya.exchangerates.converter.client.apilayer.ExchangeRatesData;
import br.com.jaya.exchangerates.converter.entity.Transaction;
import br.com.jaya.exchangerates.converter.exception.ApplicationException;
import br.com.jaya.exchangerates.converter.mapper.TransactionMapper;
import br.com.jaya.exchangerates.converter.mapper.TransactionMapperImpl;
import br.com.jaya.exchangerates.converter.repository.ExchangeRatesDataRapository;
import br.com.jaya.exchangerates.converter.repository.TransactionRespository;
import br.com.jaya.exchangerates.converter.repository.UserRepository;
import br.com.jaya.exchangerates.converter.to.TransactionInbound;
import br.com.jaya.exchangerates.converter.to.TransactionOutbound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DefaultExchangeRatesServiceTest {

    @Mock
    private TransactionRespository transactionRepository;
    @Mock
    private ExchangeRatesDataRapository exchangeRatesDataRepository;

    @Mock
    private UserRepository userRepository;

    @Captor
    ArgumentCaptor<Transaction> transactionArgumentCaptor;

    @InjectMocks
    private DefaultExchangeRatesService exchangeRatesService;

    @Spy
    private TransactionMapper transactionMapper = new TransactionMapperImpl();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void convertCurrency_InValidSourceCurrency_ReturnsApplicationException() {
        TransactionInbound transactionInbound = TransactionInbound.builder()
                .userId(1L)
                .sourceCurrency("BDT")
                .targetCurrency("EUR")
                .sourceAmount(100D).build();

        ExchangeRatesData exchangeRatesData = getExchangeRatesData();

        when(exchangeRatesDataRepository.findExchangeRatesData()).thenReturn(exchangeRatesData);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> exchangeRatesService.convertCurrency(transactionInbound));
        assertEquals("You entered an invalid or unsupported currency", exception.getMessage());
    }

    @Test
    void convertCurrency_InValidTargetCurrency_ReturnsApplicationException() {
        TransactionInbound transactionInbound = TransactionInbound.builder()
                .userId(1L)
                .sourceCurrency("EUR")
                .targetCurrency("BDT")
                .sourceAmount(100D).build();

        ExchangeRatesData exchangeRatesData = getExchangeRatesData();

        when(exchangeRatesDataRepository.findExchangeRatesData()).thenReturn(exchangeRatesData);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> exchangeRatesService.convertCurrency(transactionInbound));
        assertEquals("You entered an invalid or unsupported currency", exception.getMessage());
    }

    @Test
    void convertCurrency_ValidTransactionInbound_ReturnsTransactionOutbound() {
        TransactionInbound transactionInbound = TransactionInbound.builder()
                .userId(1L)
                .sourceCurrency("BRL")
                .targetCurrency("EUR")
                .sourceAmount(100D).build();

        ExchangeRatesData exchangeRatesData = getExchangeRatesData();

        when(exchangeRatesDataRepository.findExchangeRatesData()).thenReturn(exchangeRatesData);

        exchangeRatesService.convertCurrency(transactionInbound);

        verify(transactionRepository).save(transactionArgumentCaptor.capture());

        Transaction transaction = transactionArgumentCaptor.getValue();
        TransactionOutbound transactionOutbound = transactionMapper.toTransactionOutbound(transaction);

        verify(transactionRepository, Mockito.times(1)).save(any());
        assertNotNull(transactionOutbound);
        assertEquals(34.0, transactionOutbound.getConversionRate());
        assertEquals(3400.0, transactionOutbound.getTargetAmount());
        assertNotNull(transactionOutbound.getDateTime());
    }

    @Test
    void listTransactions_ReturnsListTransactionOutbound() {
        Long userId = 1L;

        List<Transaction> transactionList = getTransactions();

        when(transactionRepository.findByUserId(userId)).thenReturn(transactionList);

        List<TransactionOutbound> transactionOutboundList = transactionMapper.toTransactionOutboundList(transactionList);

        assertEquals(transactionOutboundList, exchangeRatesService.listTransactions(userId));

    }
    private ExchangeRatesData getExchangeRatesData() {
        ExchangeRatesData exchangeRatesData = new ExchangeRatesData();
        exchangeRatesData.setRates(new HashMap<>());
        exchangeRatesData.getRates().put("BRL", 5.0);
        exchangeRatesData.getRates().put("USD", 1.2);
        exchangeRatesData.getRates().put("EUR", 1.0);
        exchangeRatesData.getRates().put("EUR", 170d);
        return exchangeRatesData;
    }

    private List<Transaction> getTransactions() {

        Transaction transaction1 = Transaction.builder()
                .transactionId(1L)
                .sourceCurrency("BRL")
                .targetCurrency("EUR")
                .conversionRate(34.0)
                .sourceAmount(100D)
                .targetAmount(3400.0)
                .dateTime(LocalDateTime.now())
                .build();

        Transaction transaction2 = Transaction.builder()
                .transactionId(2L)
                .sourceCurrency("BRL")
                .targetCurrency("EUR")
                .conversionRate(54.0)
                .sourceAmount(100D)
                .targetAmount(5400.0)
                .dateTime(LocalDateTime.now())
                .build();

        return Arrays.asList(new Transaction[]{transaction1, transaction2});
    }
}