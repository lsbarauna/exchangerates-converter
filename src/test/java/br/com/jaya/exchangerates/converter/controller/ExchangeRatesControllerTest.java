package br.com.jaya.exchangerates.converter.controller;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import br.com.jaya.exchangerates.converter.entity.Transaction;
import br.com.jaya.exchangerates.converter.entity.User;
import br.com.jaya.exchangerates.converter.repository.TransactionRespository;
import br.com.jaya.exchangerates.converter.service.ExchangeRatesService;
import br.com.jaya.exchangerates.converter.to.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExchangeRatesControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    TransactionRespository transactionRespository;

    @Autowired
    ExchangeRatesService exchangeRatesService;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }


    @Test
    void shouldSucessPostRequestForCreateUserWithValidUser() {

        User user = User.builder()
                .name("Jhon")
                .password("xpto676")
                .email("jhon@test.com")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/exchangerates/users")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldFailPostRequestForCreateUserWitUserMissingFieldValue() {

        User user = User.builder()
                .name("Lyo")
                .password("55455456")
                .email("")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/exchangerates/users")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldFailPostRequestForCreatingUserWithSameEmail
            () {

        UserInBound davidSilvaUser = UserInBound.builder()
                .name("Devid Silva")
                .password("45345")
                .email("devid@test.com")
                .build();

        exchangeRatesService.createUser(davidSilvaUser);

        UserInBound davidCostaUser = UserInBound.builder()
                .name("Devid Costa")
                .password("535345")
                .email("devid@test.com")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(davidCostaUser)
                .when()
                .post("/exchangerates/users")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldSucessGetRequestUserTransactions() {

        UserInBound userInBound = UserInBound.builder()
                .name("Maick")
                .password("6348")
                .email("maick@test.com")
                .build();

        UserOutbound userOutbound = exchangeRatesService.createUser(userInBound);

        creatTransactions(userOutbound.getUserId());

        given()
                .contentType(ContentType.JSON)
                .header("X-API-KEY", userOutbound.getApikey())
                .when()
                .get("/exchangerates/transactions")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    void shouldFailPutRequestForNewApiKeyWithInvalidCredentials() {

        UserInBound userInBound = UserInBound.builder()
                .name("Any")
                .password("2344234")
                .email("any@test.com")
                .build();

        exchangeRatesService.createUser(userInBound);

        NewApikeyInbound newApikeyInbound = NewApikeyInbound.builder().email("any@test.com").password("123").build();

        given()
                .contentType(ContentType.JSON)
                .body(newApikeyInbound)
                .when()
                .put("/exchangerates/new-apikey")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldSucessPutRequestForNewApiKeyWithValidCredentials() {

        UserInBound userInBound = UserInBound.builder()
                .name("Marie")
                .password("12345")
                .email("marie@test.com")
                .build();

        exchangeRatesService.createUser(userInBound);

        NewApikeyInbound newApikeyInbound = NewApikeyInbound.builder().email("marie@test.com").password("12345").build();

        given()
                .contentType(ContentType.JSON)
                .body(newApikeyInbound)
                .when()
                .put("/exchangerates/new-apikey")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldSucessGetRequestForConvertCurrencyWithValidParams() {

        UserInBound userInBound = UserInBound.builder()
                .name("Luna")
                .password("344756")
                .email("luna@test.com")
                .build();

        UserOutbound userOutbound = exchangeRatesService.createUser(userInBound);

        given()
                .contentType(ContentType.JSON)
                .queryParam("sourceCurrency","BRL")
                .queryParam("targetCurrency","BRL")
                .queryParam("sourceAmount","100D")
                .header("X-API-KEY", userOutbound.getApikey())
                .when()
                .get("/exchangerates/convert")
                .then()
                .statusCode(200);
    }


    @Test
    void shouldFailGetRequestForConvertCurrencyWithIvalidCurrencyParam() {

        UserInBound userInBound = UserInBound.builder()
                .name("Tony")
                .password("344756")
                .email("tony@test.com")
                .build();

        UserOutbound userOutbound = exchangeRatesService.createUser(userInBound);

        given()
                .contentType(ContentType.JSON)
                .queryParam("sourceCurrency","XPTO")
                .queryParam("targetCurrency","BRL")
                .queryParam("sourceAmount","100D")
                .header("X-API-KEY", userOutbound.getApikey())
                .when()
                .get("/exchangerates/convert")
                .then()
                .statusCode(400);
    }

    private void creatTransactions(Long userId) {

        List<Transaction> transactionList = List.of(

                Transaction.builder()
                        .userId(userId)
                        .transactionId(1L)
                        .sourceCurrency("BRL")
                        .targetCurrency("EUR")
                        .conversionRate(34.0)
                        .sourceAmount(100D)
                        .targetAmount(3400.0)
                        .dateTime(LocalDateTime.now())
                        .build(),

                Transaction.builder()
                        .userId(userId)
                        .transactionId(2L)
                        .sourceCurrency("BRL")
                        .targetCurrency("EUR")
                        .conversionRate(54.0)
                        .sourceAmount(100D)
                        .targetAmount(5400.0)
                        .dateTime(LocalDateTime.now())
                        .build()
        );
        transactionRespository.saveAll(transactionList);
    }
}