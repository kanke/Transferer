package org.revolut.resource;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.revolut.dto.AccountDto;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;
import org.revolut.service.AccountService;
import org.revolut.service.TransactionService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


@RunWith(MockitoJUnitRunner.class)
public class TransactionResourceIT extends BaseResourceTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Test
    public void shouldTransferFunds() throws AccountException {

        Account toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(50.00));
        toAccount.setCurrencyCode("GBP");

        //long toAccountId = createAccount(AccountDto.builder().accountName("test").balance(BigDecimal.valueOf(20.00)).currency("GBP").build());
        //long fromAccountId = createAccount(AccountDto.builder().accountName("test").balance(BigDecimal.valueOf(50.00)).currency("GBP").build());


        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(1l)
                .debitAccountId(2l)
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();

        Response resp = given()
        .header("Content-Type", "application/json")
                .body(accountTransactionDto)
                .post("/transfer").then()
                .extract()
                .response();
//
//        ValidatableResponse respe = with().body(accountTransactionDto)
//                .when()
//                .request("POST", "/transfer")
//                .then()
//                .assertThat().body("odd.ck", equalTo(12.2f))
//                .statusCode(201);

        final ResponseBody body = resp.getBody();
        System.out.println("body " + body.asString());

        //assertEquals("Transfer Successful", body.path("message"));
    }

    @Test
    public void createAccount(){

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("accountName", "Test12");
        jsonAsMap.put("balance", "20.00");
        jsonAsMap.put("currency", "GBP");
        //mjsonAsMapap.put("description", "testing purpose");

        AccountDto accountDto1 = AccountDto.builder().accountName("test").balance(BigDecimal.valueOf(20.00)).currencyCode("GBP").build();
        Response resp = given()
                //.header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonAsMap)
                .post("/transfer")
                .then()
                .extract().response();
                //.andReturn();
        System.out.println("**********body********** " + resp.getBody().asString());

        ResponseBody responseBody =
                given().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                                body(jsonAsMap).
                                        when().
                                        post("/transfer").
                                        thenReturn().body();
        System.out.println("**********responseBody********** " + responseBody.asString());



        //return Long.parseLong(resp.body().asString());
    }
}
