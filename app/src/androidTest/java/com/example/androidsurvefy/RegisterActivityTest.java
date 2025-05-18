package com.example.androidsurvefy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility;
import static org.junit.Assert.*;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.androidsurvefy.AppContext;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Test
    public void testRegisterWithInvalidData_showsError() {
        ActivityScenario.launch(RegisterActivity.class);

        // Вводимо некоректні дані
        onView(withId(R.id.editTextEmail)).perform(replaceText("invalidemail"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("123"));
        onView(withId(R.id.buttonRegisterSubmit)).perform(click());

        // Перевіряємо чи є помилка
        onView(withId(R.id.textError)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
    }

    @Test
    public void testRegisterWithValidData_setsAccessToken() {
        ActivityScenario.launch(RegisterActivity.class);

        // Скидаємо токен про всяк випадок
        AppContext.getInstance().setToken(null);

        // Вводимо унікальний емейл
        String uniqueEmail = "user" + System.currentTimeMillis() + "@test.com";

        onView(withId(R.id.editTextEmail)).perform(replaceText(uniqueEmail));
        onView(withId(R.id.editTextPassword)).perform(replaceText("validPassword123"));
        onView(withId(R.id.editTextRepeatPassword)).perform(replaceText("validPassword123"));
        onView(withId(R.id.buttonRegisterSubmit)).perform(click());

        // Чекаємо на відповідь від API
        SystemClock.sleep(2000);

        // Перевіряємо отриманий токен
        String token = AppContext.getInstance().getToken();
        assertNotNull("Token must be set after successful registration", token);
        assertFalse("Token must not be empty", token.trim().isEmpty());
    }
}
