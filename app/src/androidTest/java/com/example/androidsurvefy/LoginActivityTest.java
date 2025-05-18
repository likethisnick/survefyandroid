package com.example.androidsurvefy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.intent.Intents.*;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.espresso.intent.rule.IntentsRule;

import android.content.Intent;
import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Test
    public void testLoginWithInvalidCredentials_showsError() {
        ActivityScenario.launch(LoginActivity.class);

        // Перевірка невірних даних
        onView(withId(R.id.editTextEmail)).perform(replaceText("wrong@email.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("wrongpass"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        // Перевірка відображення помилки
        onView(withId(R.id.textError)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
    }

    @Test
    public void testLoginWithValidCredentials_setsAccessToken() {
        // Скидуємо JWT токен
        AppContext.getInstance().setToken(null);

        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).perform(replaceText("admin2@admin.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin2"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        // Чекаємо на відповіть API
        SystemClock.sleep(1500);

        // Перевіряємо чи встановився JWT токен, що означає що ми авторизовані
        String token = AppContext.getInstance().getToken();
        assertNotNull("Token must be set after successful login", token);
        assertFalse("Token must not be empty", token.trim().isEmpty());
    }
}