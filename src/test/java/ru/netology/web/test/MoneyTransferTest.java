package ru.netology.web.test;

import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.MoneyTransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    void validLoginAndOpenDashboardPage() {
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
        var dashboardPage = new DashboardPage();
        int cardOneBalance = dashboardPage.getCardBalance(1);
        int cardTwoBalance = dashboardPage.getCardBalance(2);
        var firstCard = DataHelper.getCard(1);
        int amount = 2000;
        var moneyTransferPage = dashboardPage.transferMoneyTo(firstCard);
        moneyTransferPage.transferMoneyFrom(2, amount);
        assertEquals(cardOneBalance + amount, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance - amount, dashboardPage.getCardBalance(2));
    }

    @Test
    void shouldTransferFromSecondCardToFirst() {
        var dashboardPage = new DashboardPage();
        int cardOneBalance = dashboardPage.getCardBalance(1);
        int cardTwoBalance = dashboardPage.getCardBalance(2);
        var firstCard = DataHelper.getCard(1);
        int amount = 2000;
        var moneyTransferPage = dashboardPage.transferMoneyTo(firstCard);
        moneyTransferPage.transferMoneyFrom(2, amount);
        assertEquals(cardOneBalance + amount, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance - amount, dashboardPage.getCardBalance(2));
    }

    @Test
    void buttonCancelShouldWork() {
        var dashboardPage = new DashboardPage();
        int cardOneBalance = dashboardPage.getCardBalance(1);
        int cardTwoBalance = dashboardPage.getCardBalance(2);
        var firstCard = DataHelper.getCard(1);
        var moneyTransferPage = dashboardPage.transferMoneyTo(firstCard);
        moneyTransferPage.cancel();
        assertEquals(cardOneBalance, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance, dashboardPage.getCardBalance(2));
    }
}