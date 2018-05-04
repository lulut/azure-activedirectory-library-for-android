package com.microsoft.identity.common.test.automation.tasks;
import com.microsoft.identity.common.test.automation.actors.User;
import com.microsoft.identity.common.test.automation.interactions.CloseKeyboard;
import com.microsoft.identity.common.test.automation.ui.Main;
import com.microsoft.identity.common.test.automation.ui.Request;
import com.microsoft.identity.common.test.automation.ui.Results;
import com.microsoft.identity.common.test.automation.ui.identityproviders.AADV1.SignInPagePassword;
import com.microsoft.identity.common.test.automation.ui.identityproviders.AADV1.SignInPageUserName;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Steps;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class AcquireTokenPBRefresh implements Task {
    @Steps
    CloseKeyboard closeKeyboard;


    @Override
    public <T extends Actor> void performAs(T actor) {
        User user = (User)actor;
        SignInUser signInUser = SignInUser.GetSignInUserByFederationProvider(user.getFederationProvider());
        actor.attemptsTo(
                WaitUntil.the(Main.ACQUIRE_TOKEN_BUTTON, isVisible()).forNoMoreThan(10).seconds(),
                Click.on(Main.ACQUIRE_TOKEN_BUTTON),
                Enter.theValue(user.getpromptbehaviorRefreshSession_as_json()).into(Request.REQUEST_INFO_FIELD),
                WaitUntil.the(Request.SUBMIT_REQUEST_BUTTON, isVisible()).forNoMoreThan(10).seconds(),
                Click.on(Request.SUBMIT_REQUEST_BUTTON),
                WaitUntil.the(SignInPageUserName.USERNAME, isVisible()).forNoMoreThan(10).seconds(),
                new EnterUserNameForSignInDisambiguation(),
                WaitUntil.the(SignInPagePassword.PASSWORD, isVisible()).forNoMoreThan(10).seconds(),
                signInUser
        );
    }
}
