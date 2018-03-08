package com.microsoft.aad.automation.testapp;


import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.DisableOnAndroidDebug;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.Until;
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
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;
import android.widget.EditText;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {

    String mBasicAuth = "{" +
            "authority='https://login.microsoftonline.com/common', " +
            "client_id='4b0db8c2-9f26-4417-8bde-3f0e3656f8e0', " +
            "resource='00000002-0000-0000-c000-000000000000', " +
            "redirect_uri='urn:ietf:wg:oauth:2.0:oob', " +
            "validate_authority=true" +
            "}";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void registerIdlingResource() {
        // To prove that the test fails, omit this call:
        //IdlingRegistry.getInstance().register(SignInActivity.IDLING);
    }

    @After
    public void unregisterIdlingResource() {
        //IdlingRegistry.getInstance().unregister(SignInActivity.IDLING);
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

        userNameBox.setText("shoatman@microsoft.com");
        UiObject2 nextButton = device.findObject(By.text("Next"));
        nextButton.click();

        Thread.sleep(8000);
        UiObject passwordBox = device.findObject(new UiSelector()
        .instance(1)
        .className(EditText.class));
        passwordBox.setText("Theresa@70");

        UiObject signInButton = device.findObject(new UiSelector()
                .instance(0)
                .className(Button.class)
        );
        signInButton.click();


        Thread.sleep(20000);


        ViewInteraction resultsView = onView(
                allOf(withId(R.id.resultInfo), isDisplayed())
        );




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
}
