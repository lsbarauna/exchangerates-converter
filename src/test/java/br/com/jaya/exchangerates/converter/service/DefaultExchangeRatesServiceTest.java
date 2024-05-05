package br.com.jaya.exchangerates.converter.service;

import br.com.jaya.exchangerates.converter.client.apilayer.ExchangeRatesData;
import br.com.jaya.exchangerates.converter.entity.Transaction;
import br.com.jaya.exchangerates.converter.entity.User;
import br.com.jaya.exchangerates.converter.exception.ApplicationException;
import br.com.jaya.exchangerates.converter.exception.AuthenticationException;
import br.com.jaya.exchangerates.converter.mapper.TransactionMapper;
import br.com.jaya.exchangerates.converter.mapper.TransactionMapperImpl;
import br.com.jaya.exchangerates.converter.mapper.UserMapper;
import br.com.jaya.exchangerates.converter.mapper.UserMapperImpl;
import br.com.jaya.exchangerates.converter.repository.ExchangeRatesDataRapository;
import br.com.jaya.exchangerates.converter.repository.TransactionRespository;
import br.com.jaya.exchangerates.converter.repository.UserRepository;
import br.com.jaya.exchangerates.converter.to.*;
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
import java.util.Optional;

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

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

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

    @Test
    void createUser_EmailAlreadyInuse_ReturnApplicationException() {

        Optional<User> userOptional = getRegisteredUser();
        UserInBound userInBound = getUserInBound();
        when(userRepository.findByEmail(userInBound.getEmail())).thenReturn(userOptional);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> exchangeRatesService.createUser(userInBound));
        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    void createUser_ReturnUserOutbound() {
        UserInBound userInBound = getUserInBound();
        UserOutbound userOutboundExpected = getUserOutbound();

        when(userRepository.findByEmail(userInBound.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(getRegisteredUser().get());

        UserOutbound userOutboundActual = exchangeRatesService.createUser(userInBound);

        assertEquals(userOutboundExpected, userOutboundActual);
    }
    @Test
    void generateNewApiKey_WithInvalidCredentials_ReturnAuthenticationException() {

        NewApikeyInbound newApikeyInbound = NewApikeyInbound.builder().email("luiz@teste.com").password("12").build();

        when(userRepository.findByEmail(newApikeyInbound.getEmail())).thenReturn(getRegisteredUser());

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> exchangeRatesService.generateNewApiKey(newApikeyInbound));
        assertEquals("Invalid Credentials", exception.getMessage());
    }

    @Test
    void generateNewApiKey_WithValidCredentials_ReturnNewApikeyOutbound() {

        NewApikeyInbound newApikeyInbound = NewApikeyInbound.builder().email("luiz@teste.com").password("1234").build();

        when(userRepository.findByEmail(newApikeyInbound.getEmail())).thenReturn(getRegisteredUser());

        NewApikeyOutbound newApikeyOutbound =  exchangeRatesService.generateNewApiKey(newApikeyInbound);

        assertNotNull(newApikeyOutbound);
        assertNotNull(newApikeyOutbound.getNewApiKey());
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
    private Optional<User> getRegisteredUser(){

        return Optional.ofNullable(User.builder()
                .userId(1L)
                .name("Luiz")
                .password("e248efe072a175659f89e68c3990d8a5")
                .email("luiz@teste.com")
                .apikey("a5ae673d347e061b671cdecdf1f0c128").build());
    }
    private UserInBound getUserInBound() {

        return UserInBound.builder()
                .name("Luiz")
                .email("luiz@teste.com")
                .password("12345").build();
    }
    private UserOutbound getUserOutbound() {

        return UserOutbound.builder()
                .userId(1L)
                .name("Luiz")
                .email("luiz@teste.com")
                .apikey("a5ae673d347e061b671cdecdf1f0c128").build();
    }

}
