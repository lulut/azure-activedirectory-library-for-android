package com.microsoft.identity.common.test.automation.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.webdriver.WebDriverFacade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByAll;

import java.util.List;

import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.android.AndroidDriver;

public class CloseKeyboard implements Interaction {

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriverFacade facade = (WebDriverFacade) BrowseTheWeb.as(actor).getDriver();
        AndroidDriver androidDriver = (AndroidDriver) facade.getProxiedDriver();
        if(androidDriver.isKeyboardShown()){
            androidDriver.hideKeyboard();
        }
    }
}