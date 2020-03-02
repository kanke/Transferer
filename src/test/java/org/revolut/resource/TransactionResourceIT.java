package org.revolut.resource;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;
import org.revolut.service.AccountService;
import org.revolut.service.TransactionService;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

@RunWith(MockitoJUnitRunner.class)
public class TransactionResourceIT extends BaseResourceTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Test
    public void shouldTransferFunds() throws AccountException {

        Account toAccount= new Account();
        toAccount.setBalance(BigDecimal.valueOf(50.00));
        toAccount.setCurrencyCode("GBP");

        //Mockito.when(accountService.getAccount(1L)).thenReturn(toAccount);
       // Mockito.when(accountService.getAccount(2L)).thenReturn(toAccount);
        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(1)
                .debitAccountId(2)
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

}
