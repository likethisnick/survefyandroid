package com.example.androidsurvefy;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;

import java.util.Collection;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private Activity getCurrentActivity() {
        final Activity[] currentActivity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> resumed =
                    ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
            if (!resumed.isEmpty()) {
                currentActivity[0] = resumed.iterator().next();
            }
        });
        return currentActivity[0];
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View childView = view.findViewById(id);
                if (childView != null && childView.isClickable()) {
                    childView.performClick();
                } else {
                    throw new PerformException.Builder()
                            .withActionDescription(this.getDescription())
                            .withViewDescription("Child view not found or not clickable")
                            .build();
                }
            }
        };
    }

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot(); // Можно вызывать только на корне дерева
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    @Test
    public void checkAuth_withoutToken() {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        prefs.edit().remove("token").apply();
        AppContext.getInstance().setToken("");

        ActivityScenario.launch(MainActivity.class);

        // Чекаємо результату перевірки
        onView(withId(R.id.button_logincheck)).perform(click());

        // Перевіряємо чи залишився статус індикатор сірим
        onView(withId(R.id.statusIndicator))
                .check(matches(new DrawableMatcher(R.drawable.indicator_circle_grey)));
    }

    @Test
    public void afterLogin_checkAuth_indicatorTurnsGreen() {
        AppContext.getInstance().setToken(null);

        ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).perform(replaceText("admin2@admin.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin2"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        ActivityScenario<MainActivity> mainScenario = ActivityScenario.launch(MainActivity.class);

        // "check auth"
        onView(withId(R.id.button_logincheck)).perform(click());

        // Перевіряємо чи зелений індикатор після коректного логіну
        onView(withId(R.id.statusIndicator))
                .check(matches(new DrawableMatcher(R.drawable.indicator_circle_green)));
    }

    @Test
    public void checkAuth_withInvalidToken_indicatorTurnsRed() {
        // Встановлюємо невалідний токен
        AppContext.getInstance().setToken("invalid_or_expired_token");

        // Запускаємо MainActivity
        ActivityScenario.launch(MainActivity.class);

        // check auth
        onView(withId(R.id.button_logincheck)).perform(click());

        // Перевіряємо чи став індикатор червоним
        onView(withId(R.id.statusIndicator))
                .check(matches(new DrawableMatcher(R.drawable.indicator_circle_red)));
    }

    @Test
    public void login_thenLogout_tokenSetAndCleared_indicatorChangesAccordingly() {
        // Скидаємо токен
        AppContext.getInstance().setToken(null);

        // 1 - виконуємо логін
        ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).perform(replaceText("admin2@admin.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin2"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        // 2 - відкриваємо MainActivity якщо не відкритий
        ActivityScenario<MainActivity> mainScenario = ActivityScenario.launch(MainActivity.class);

        // 3 натискаємо "check auth"
        onView(withId(R.id.button_logincheck)).perform(click());

        // 4 - еревірняємо чи зелений індикатор (логін успішний) та токен встановився
        onView(withId(R.id.statusIndicator))
                .check(matches(new DrawableMatcher(R.drawable.indicator_circle_green)));
        assertNotNull("Token should be set", AppContext.getInstance().getToken());
        assertFalse("Token shoudn't be empty", AppContext.getInstance().getToken().isEmpty());

        // 5 - перевіряємо чи відображається log out та натискаємо
        onView(withId(R.id.button_logout))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
        onView(withId(R.id.button_logout)).perform(click());

        // 6 - перевіряємо чи скинувся токен та індикатор знову сірий
        assertNull("Token must be cleared", AppContext.getInstance().getToken());
        onView(withId(R.id.statusIndicator))
                .check(matches(new DrawableMatcher(R.drawable.indicator_circle_grey)));
    }

    @Test
    public void Dashboard_CreateNewSurvey() {
        // Скидаємо токен
        AppContext.getInstance().setToken(null);

        // 1 - виконуємо логін
        ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).perform(replaceText("admin2@admin.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin2"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        // 2 - відкриваємо DashboardActivity
        ActivityScenario<DashboardActivity> dashboardScenario = ActivityScenario.launch(DashboardActivity.class);

        // 3 - натискаємо "створити опитування"
        onView(withId(R.id.button_create_survey)).perform(click());

        // 4 - перевірка, що ми у CreateSurveyActivity
        ActivityScenario<CreateSurveyActivity> createScenario = ActivityScenario.launch(CreateSurveyActivity.class);

        // 5 - залишаємо поля пустими та тиснемо "створити"
        onView(withId(R.id.buttonCreate)).perform(click());

        // 6 - перевіряємо що показано помилку
        onView(withId(R.id.textError))
                .check(matches(isDisplayed()));

        // 7 - заповнюємо поля
        onView(withId(R.id.editTextName)).perform(replaceText("New survey"));
        onView(withId(R.id.editTextDescription)).perform(replaceText("Simple description"));

        // 8 - тиснемо "створити"
        onView(withId(R.id.buttonCreate)).perform(click());

        // 9 - перевіряємо що активність завершилась
        createScenario.onActivity(activity -> {
            assertTrue(activity.isFinishing());
        });
    }
    @Test
    public void Dashboard_EditSurvey() {
        // Скидаємо токен
        AppContext.getInstance().setToken(null);

        // 1 - виконуємо логін
        ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).perform(replaceText("admin2@admin.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin2"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        // 2 - відкриваємо DashboardActivity
        ActivityScenario<DashboardActivity> dashboardScenario = ActivityScenario.launch(DashboardActivity.class);

        // 3 - натискаємо "створити опитування"
        onView(withId(R.id.button_create_survey)).perform(click());

        // 4 - перевірка, що ми у CreateSurveyActivity
        ActivityScenario<CreateSurveyActivity> createScenario = ActivityScenario.launch(CreateSurveyActivity.class);

        // 5 - залишаємо поля пустими та тиснемо "створити"
        onView(withId(R.id.buttonCreate)).perform(click());

        // 6 - перевіряємо що показано помилку
        onView(withId(R.id.textError))
                .check(matches(isDisplayed()));

        // 7 - заповнюємо поля
        onView(withId(R.id.editTextName)).perform(replaceText("New survey"));
        onView(withId(R.id.editTextDescription)).perform(replaceText("Simple description"));

        // 8 - тиснемо "створити"
        onView(withId(R.id.buttonCreate)).perform(click());

        // 9 - перевіряємо що активність завершилась
        createScenario.onActivity(activity -> {
            assertTrue(activity.isFinishing());
        });
    }
    @Test
    public void test1_Dashboard_EditUserSurvey_CreateQuestion() {
        // Скидаємо токен
        AppContext.getInstance().setToken(null);

        // 1 - виконуємо логін
        ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).perform(replaceText("admin2@admin.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin2"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        // 2 - відкриваємо DashboardActivity
        ActivityScenario<EditSurveysActivity> createScenario = ActivityScenario.launch(EditSurveysActivity.class);

        // 3 - перевіряємо чи існує хоча б одне опитування доступне для змін
        onView(withId(R.id.recyclerSurveys))
                .check(matches(hasMinimumChildCount(1)));

        // 4 - відкриваємо перше доступне опитування у списку
        onView(withId(R.id.recyclerSurveys))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        clickChildViewWithId(R.id.buttonOpen)));

        onView(withId(R.id.buttonOpenQuestions)).perform(click());

        // 5 - тиснемо "додати питання"
        onView(withId(R.id.buttonAddQuestion)).perform(click());
        onView(isRoot()).perform(waitFor(1000));

        // 6 - перевіряємо щоб при пустих полях була помилка
        onView(withId(R.id.buttonSaveQuestion)).perform(click());
        onView(withId(R.id.textError))
                .check(matches(isDisplayed()))
                .check(matches(withText("Question should contain text")));

        // 7 - заповнюємо та зберігаємо
        onView(withId(R.id.editTextQuestionText)).perform(replaceText("What is Espresso?"));
        onView(withId(R.id.buttonSaveQuestion)).perform(click());

        // 8 - перевіряжмо, що вікно "додати питання" закрилося (питання створилося)
        Activity current = getCurrentActivity();
        assertFalse(current instanceof CreateQuestionActivity);
    }

    @Test
    public void test2_Dashboard_EditUserSurvey_AddQuestionOption() {
        // Скидаємо токен
        AppContext.getInstance().setToken(null);

        // 1 - виконуємо логін
        ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).perform(replaceText("admin2@admin.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin2"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        // 2 - відкриваємо DashboardActivity
        ActivityScenario<EditSurveysActivity> createScenario = ActivityScenario.launch(EditSurveysActivity.class);

        // 3 - перевіряємо чи існує хоча б одне опитування доступне для змін
        onView(withId(R.id.recyclerSurveys))
                .check(matches(hasMinimumChildCount(1)));

        // 4 - відкриваємо перше доступне опитування у списку
        onView(withId(R.id.recyclerSurveys))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        clickChildViewWithId(R.id.buttonOpen)));

        onView(withId(R.id.buttonOpenQuestions)).perform(click());

        // 5 - додаємо до існуючого опитування варіант відповіді
        onView(withId(R.id.buttonAddOption)).perform(click());

        // 6 - тиснемо з пустими полями збереження та перевіряємо помилку
        onView(withId(R.id.buttonSaveOption)).perform(click());
        onView(withId(R.id.textError))
                .check(matches(isDisplayed()))
                .check(matches(withText("Option text cannot be empty")));

        // 7 - заповнюємо та зберігаємо
        onView(withId(R.id.editTextOptionText)).perform(replaceText("Option test"));
        onView(withId(R.id.buttonSaveOption)).perform(click());

        // 8 - перевіряжмо, що вікно "додати питання" закрилося (питання створилося)
        Activity current = getCurrentActivity();
        assertFalse(current instanceof CreateQuestionOptionActivity);
    }

    @Test
    public void test3_Dashboard_EditUserSurvey_EditOption() {
        // Скидаємо токен
        AppContext.getInstance().setToken(null);

        // 1 - виконуємо логін
        ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).perform(replaceText("admin2@admin.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin2"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        // 2 - відкриваємо DashboardActivity
        ActivityScenario<EditSurveysActivity> createScenario = ActivityScenario.launch(EditSurveysActivity.class);

        // 3 - перевіряємо чи існує хоча б одне опитування доступне для змін
        onView(withId(R.id.recyclerSurveys))
                .check(matches(hasMinimumChildCount(1)));

        // 4 - відкриваємо перше доступне опитування у списку
        onView(withId(R.id.recyclerSurveys))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        clickChildViewWithId(R.id.buttonOpen)));

        onView(withId(R.id.buttonOpenQuestions)).perform(click());

        // 5 - перевіряємо, що є хоч один варіант відповіді
        onView(withId(R.id.recyclerOptions))
                .check(matches(hasMinimumChildCount(1)));

        // 6 - натискаємо кнопку Edit у першому варіанті
        onView(withId(R.id.recyclerOptions))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        clickChildViewWithId(R.id.buttonEditOption)));

        SystemClock.sleep(1000); // дать активности открыться

        // 7 - зберігаємо текст, що будемо редагувати, додаємо апдейт частину, зберігаємо
        final String[] existingText = new String[1];
        Activity current = getCurrentActivity();
        if (current instanceof EditQuestionOptionActivity) {
            EditText editText = current.findViewById(R.id.editTextOptionText);
            existingText[0] = editText.getText().toString();
        }
        String updatedText = existingText[0] + " - updated";
        onView(withId(R.id.editTextOptionText)).perform(replaceText(updatedText));
        onView(withId(R.id.buttonSaveOption)).perform(click());

        // 8 - чекаємо і перевіряємо чи бачимо ми оновлений текст
        SystemClock.sleep(500); // или waitFor(500)
        onView(withText(updatedText)).check(matches(isDisplayed()));
    }

    @Test
    public void test4_Dashboard_EditUserSurvey_DeleteOption() {
        // Скидаємо токен
        AppContext.getInstance().setToken(null);

        // 1 - виконуємо логін
        ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).perform(replaceText("admin2@admin.com"));
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin2"));
        onView(withId(R.id.buttonLoginSubmit)).perform(click());

        // 2 - відкриваємо DashboardActivity
        ActivityScenario<EditSurveysActivity> createScenario = ActivityScenario.launch(EditSurveysActivity.class);

        // 3 - перевіряємо чи існує хоча б одне опитування доступне для змін
        onView(withId(R.id.recyclerSurveys))
                .check(matches(hasMinimumChildCount(1)));

        // 4 - відкриваємо перше доступне опитування у списку
        onView(withId(R.id.recyclerSurveys))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        clickChildViewWithId(R.id.buttonOpen)));
        SystemClock.sleep(1000);

        onView(withId(R.id.buttonOpenQuestions)).perform(click());

        // 5 - перевіряємо, що є хоч один варіант відповіді
        onView(withId(R.id.recyclerOptions))
                .check(matches(hasMinimumChildCount(1)));

        // 6 - натискаємо кнопку Delete у першому варіанті
                onView(withId(R.id.recyclerOptions))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                                clickChildViewWithId(R.id.buttonDeleteOption)));
    }
}
