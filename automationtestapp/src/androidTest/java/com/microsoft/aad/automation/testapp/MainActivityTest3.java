package com.microsoft.aad.automation.testapp;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.fail;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;

import static org.hamcrest.Matchers.allOf;

import android.widget.Button;
import android.widget.EditText;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest3 {
    private String mUPN = "";
    private String mPassword = "";

    String mBasicAuth = "{" +
            "\"authority\": \"https://login.microsoftonline.com/common\"," +
            "\"client_id\": \"1b345377-6ae2-4a23-9c3d-efc35ef04eb9\"," +
            "\"resource\": \"https://msdevex-my.sharepoint.com\"," +
            "\"redirect_uri\": \"urn:ietf:wg:oauth:2.0:oob\"," +
            "\"validate_authority\": \"true\"" +
            "}";
    @Before
    public void setUp() throws Exception {
        // we would call lab api and grab tenant/user info here.
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    public void awaitWebView()throws InterruptedException
    {
        int ms_Threshold = 15000;
        int ms_Elapsed = 0;
        int ms_Increments = 1000;

        while(ms_Threshold > ms_Elapsed)
        {
            Thread.sleep(ms_Increments);
            ms_Elapsed = ms_Elapsed + ms_Increments;
            try{
                onWebView().withElement(findElement(Locator.ID, "passwordInput"));
                break;
            } catch(Exception e){}
        }
    }

    @Ignore
    @Test
    public void clearToken()throws InterruptedException, UiObjectNotFoundException {

        ViewInteraction clearCacheButton = onView(
                allOf(withId(R.id.clearCache), withText("Clear Cache"), isDisplayed()));
        clearCacheButton.perform(click());

        ViewInteraction doneButton = onView(
                allOf(withId(R.id.resultDone), withText("Done"), isDisplayed()));
        doneButton.perform(click());

        ViewInteraction expireATButton = onView(
                allOf(withId(R.id.expireAccessToken), withText("Expire Access Token"), isDisplayed()));
        expireATButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.requestInfo),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(ViewActions.typeText(mBasicAuth));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.requestGo), withText("Go"),
                        isDisplayed()));

        appCompatButton2.perform(ViewActions.closeSoftKeyboard());
        appCompatButton2.perform(ViewActions.click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void mainActivityTest2() throws InterruptedException, UiObjectNotFoundException {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.acquireToken), withText("Acquire Token"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.requestInfo),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(ViewActions.typeText(mBasicAuth));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.requestGo), withText("Go"),
                        isDisplayed()));

        appCompatButton2.perform(ViewActions.closeSoftKeyboard());
        appCompatButton2.perform(ViewActions.click());

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Thread.sleep(4000);
        UiObject userNameBox = device.findObject(new UiSelector()
                .instance(0)
                .className(EditText.class)
        );


        userNameBox.setText(mUPN);
        UiObject2 nextButton = device.findObject(By.text("Next"));
        nextButton.click();

        Thread.sleep(2000);
        UiObject passwordBox = device.findObject(new UiSelector()
                .instance(0)
                .className(EditText.class));
        String pbox = passwordBox.getText();

        if(!pbox.contains("password")){
            fail("The password field never came up.");
        }

        passwordBox.setText(mPassword);

        UiObject signInButton = device.findObject(new UiSelector()
                .instance(1)
                .className(Button.class)
        );
        signInButton.click();

        Thread.sleep(2000);

        ViewInteraction doneButton = onView(
                allOf(withId(R.id.resultDone), withText("Done"), isDisplayed()));
        doneButton.perform(click());
    }


    @After
    public void tearDown() throws Exception {
        //Post test for telemetry or whatever we need here.
    }
}
