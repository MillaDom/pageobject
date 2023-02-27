package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    DashboardPage dashboardPage = new DashboardPage();
    int cardOneBalance = dashboardPage.getCardBalance(1);
    int cardTwoBalance = dashboardPage.getCardBalance(2);

    @BeforeAll
    static void validLoginAndOpenDashboardPage() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen=true;

        var authInfo = DataHelper.getAuthInfo();
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromFirstCardToSecond() {
        int amount = 2000;
        var moneyTransferPage = dashboardPage.transferMoneyTo(2);
        moneyTransferPage.transferMoneyFrom(1, amount);
        assertEquals(cardOneBalance - amount, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance + amount, dashboardPage.getCardBalance(2));
    }

    @Test
    void shouldTransferFromSecondCardToFirst() {
        int amount = 2000;
        var moneyTransferPage = dashboardPage.transferMoneyTo(1);
        moneyTransferPage.transferMoneyFrom(2, amount);
        assertEquals(cardOneBalance + amount, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance - amount, dashboardPage.getCardBalance(2));
    }

    @Test
    void buttonCancelShouldWork() {
        var moneyTransferPage = dashboardPage.transferMoneyTo(1);
        moneyTransferPage.cancel();
        assertEquals(cardOneBalance, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance, dashboardPage.getCardBalance(2));
    }
}
