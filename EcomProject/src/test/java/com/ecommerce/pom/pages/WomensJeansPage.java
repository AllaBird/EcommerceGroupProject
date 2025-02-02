package com.ecommerce.pom.pages;

import org.openqa.selenium.WebDriver;

import static com.ecommerce.pom.EndPoints.WOMENS_JEANS_URL;

public class WomensJeansPage extends SalesPage<WomensJeansPage> {

    public WomensJeansPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public WomensJeansPage load() {
        getDriver().get(WOMENS_JEANS_URL);

        return this;
    }
}
