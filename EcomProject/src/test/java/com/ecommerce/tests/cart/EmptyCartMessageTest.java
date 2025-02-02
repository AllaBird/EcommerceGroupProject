package com.ecommerce.tests.cart;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pojo.User;
import com.ecommerce.pom.pages.AccountPage;
import com.ecommerce.pom.pages.CartPage;
import com.ecommerce.pom.pages.HomePage;
import com.ecommerce.utils.UserUtils;
import com.ecommerce.utils.WaitUtils;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;


public class EmptyCartMessageTest extends BaseTest {

    @Test(description = "9.1-9-1 | TC The cart page displays a message 'Your cart is currently empty' # https://app.clickup.com/t/868abdbhv")
    public void testRemoveOneProductFromTheCartByX() throws IOException {
        User user = UserUtils.readUserFromJson("user.json");
        HomePage homePage = new HomePage(driver);
        AccountPage accountPage = homePage.getHeader().navigateToAccountPage();
        accountPage.logIn(user.getLogin(), user.getPassword());

        CartPage cartPage = accountPage.getHeader().navigateToCartPage();
        cartPage.clearTheCartFromOneItem();

        WaitUtils.waitForQuantityToBe(cartPage.getDriver(), By.xpath("//span[@class='count']"), "0");

        Assert.assertEquals(cartPage.getCartItemsNumber(), "0");

        Assert.assertEquals(cartPage.getEmptyCartMessage(), "Your cart is currently empty.");

    }

}

