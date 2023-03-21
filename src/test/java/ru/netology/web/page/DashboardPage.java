package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement title = $("[data-test-id='dashboard']");
    private SelenideElement subtitle = $(byText("Ваши карты"));
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        title.shouldBe(visible);
        subtitle.shouldBe(visible);
    }

    public MoneyTransferPage transferMoneyTo(DataHelper.Card card) {
        cards.findBy(attribute("data-test-id", card.getDataTestId()))
                .find(".button").click();
        return new MoneyTransferPage();
    }

    public int getCardBalance(int id) {
        String text = cards.findBy(attribute("data-test-id", DataHelper.getCard(id).getDataTestId())).text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public MoneyTransferPage replenish(int id) {
        cards.get(id).$(byText("Пополнить")).click();
        return new MoneyTransferPage();
    }
}
