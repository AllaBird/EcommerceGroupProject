package com.ecommerce.tests.components.left_sidebar;

import com.ecommerce.base.BaseTest;
import com.ecommerce.data.Product;
import com.ecommerce.pom.pages.HomePage;
import com.ecommerce.pom.pages.MenPage;
import com.ecommerce.pom.pages.ProductPage;
import com.ecommerce.pom.pages.PurchasePage;
import com.ecommerce.pom.pages.StorePage;
import com.ecommerce.pom.pages.WomenPage;
import com.ecommerce.pom.EndPoints;
import com.ecommerce.pom.EndPoints;
import com.ecommerce.pom.pages.StorePage;
import com.ecommerce.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeftSidebarTest extends BaseTest {

    @Test(description = "10.4-10.4-1  | TC >Search box Test> Search by key word # https://app.clickup.com/t/8689x8h18")
    public void testSearchBox() {
        String searchWord = "Blue";
        HomePage homePage = new HomePage(driver);
        StorePage storePage = homePage.clickShopNowButton();
        storePage.getLeftSidebar().searchProduct(searchWord);
        List<String> expectedSearchResultList = storePage.getLeftSidebar().getExpectedSearchResultListWithBlue();
        List<String> actualSearchResultList = List.of(
                "Blue Shoes",
                "Denim Blue Jeans",
                "Faint Blue Jeans",
                "Blue Denim Shorts",
                "Basic Blue Jeans",
                "Blue Tshirt");

        Assert.assertEquals(actualSearchResultList, expectedSearchResultList);
    }


    @Test(description = "10.4-10.4-2  | TC >Search box Test> Search by key word> No product were found # https://app.clickup.com/t/8689xwcb5")
    public void testSearchBoxNoResult() {
        String searchWord = "moon";
        HomePage homePage = new HomePage(driver);
        StorePage storePage = homePage.clickShopNowButton();
//        storePage.getLeftSidebar().searchProduct(searchWord, new StorePage(driver));
        String NotFoundMassage = storePage.getLeftSidebar().getNotFoundMessageText();

        Assert.assertEquals(NotFoundMassage, "No products were found matching your selection.");

    }

    @Test(description = "10.2-10.2-2 | TC > Leftside_Bar > Browser by categories # https://app.clickup.com/t/8689x8my5")
    public void testBrowseByCategoriesLeftMenu() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        List<String> actualSortedList = homePage.clickShopNowButton().getLeftSidebar().
                selectCategoryByIndex(2, new MenPage(driver)).
                getLeftSidebar().getActualSortedListManCategory();
        List<String> expectedMenSorting = new ArrayList<>(actualSortedList);
        Collections.sort(expectedMenSorting);

        Assert.assertEquals(actualSortedList, expectedMenSorting, "Sorting by Category Dropdown Did Not Apply to 'Men' Category");
    }


    @Test(description = "10.3-10.3-1  | TC > Leftside_Bar > filter price  > price range # https://app.clickup.com/t/8689x9pxq")
    public void testFilterPriceCheck() {
        StorePage storePage = new StorePage(driver);
        storePage.load();
        storePage.moveRightNodOfPriceFilter(60).clickFilterButton();
        List<Product> storePageAfterFiltering = storePage.getProductsGrid().getAllProductsOnPage();
        for (Product product : storePageAfterFiltering) {
            Assert.assertTrue(product.getPrice() <= 60);
            Assert.assertTrue(storePageAfterFiltering.contains(product));
        }
    }


    @Test(description = "10.4-1-3 | TC > Verify Search Returns All Items Across Categories"
            + "# https://app.clickup.com/t/8689vk47d")
    public void testVerifySearchReturnsAllItemsInAllCategories() {
        final String itemCategory = "jeans";

        int quantityFromSearchResult = new StorePage(driver).load()
                .getLeftSidebar().searchProduct(itemCategory)
                .countItemsOnPage(itemCategory);

        int quantityFromMenPage = new MenPage(driver).load()
                .countItemsOnPage(itemCategory);

        int quantityFromWomenPage = new WomenPage(driver).load()
                .countItemsOnPage(itemCategory);

        Assert.assertEquals(quantityFromSearchResult, quantityFromMenPage + quantityFromWomenPage,
                "Search box did not find all the items with item name '"
                        + itemCategory + "' or find extra items");
    }


    @Test(description = "10.4 -1-5 | TC > Verify search results with 2-character input. "
            + "#https://app.clickup.com/t/868abp8yn")
    public void testSearchWithTwoCharactersReturnsRelevantItems() {
        String partialFirstCharacters = "Je";

        PurchasePage<?> resultPage = new StorePage(driver).load()
                .getLeftSidebar().searchProduct(partialFirstCharacters);//неопределённый параметр типа

        List<String> productNameList = new ArrayList<>();

        if (resultPage instanceof StorePage) {
            productNameList = ((StorePage) resultPage).getAllItemsNameList();
        } else if (resultPage instanceof ProductPage) {

            Assert.assertTrue(((ProductPage) resultPage).getProductNameText().contains(partialFirstCharacters));

        } else {
            throw new IllegalStateException("Unexpected page after search.");
        }

        boolean allContainMatch = true;

        for (String name : productNameList) {
            if (!name.toLowerCase().contains(partialFirstCharacters.toLowerCase())) {
                allContainMatch = false;
                break;
            }
        }

        Assert.assertTrue(allContainMatch,
                "Not all product names contain the substring: " + partialFirstCharacters);
    }

    @Test(description = "10.3-1...3-1 | TC > Women > Clear browser by categories."
            + "# https://app.clickup.com/t/8689wk070")
    public void closeWomenCategory() {

        StorePage storePage = new StorePage(driver);
        storePage.getHeader()
                .navigateToWomenPage()
                .getLeftSidebar()
                .scrollToBrowseByCategories()
                .clearCategory();

        String actualCategoryText = storePage.getLeftSidebar().getSelectedCategoryText();
        String expectedCategoryText = "Select a category";

        Assert.assertEquals(actualCategoryText, expectedCategoryText, "Category was not cleared out!");
        Assert.assertEquals(driver.getCurrentUrl(), EndPoints.STORE_URL, "User was not redirected to the Store page");
    }
}

