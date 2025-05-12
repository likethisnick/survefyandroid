package com.example.androidsurvefy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsurvefy.Adapter.QuestionOptionAdapter;
import com.example.androidsurvefy.Model.QuestionDto;
import com.example.androidsurvefy.Model.QuestionOptionDto;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsActivity extends AppCompatActivity {

    private List<QuestionDto> questions;
    private int currentIndex = 0;

    private TextView textQuestionId;
    private EditText editTextQuestion;
    private RecyclerView recyclerOptions;
    private Button buttonPrev, buttonNext, buttonBack;
    private List<QuestionOptionDto> questionOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        textQuestionId = findViewById(R.id.textQuestionId);
        editTextQuestion = findViewById(R.id.editTextQuestion);
        recyclerOptions = findViewById(R.id.recyclerOptions);
        recyclerOptions.setLayoutManager(new LinearLayoutManager(this));

        buttonPrev = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        String templateSurveyId = getIntent().getStringExtra("surveyId");
        loadQuestions(templateSurveyId);
        loadQuestionOptions(templateSurveyId);

        buttonBack.setOnClickListener(v -> finish());

        buttonPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayQuestion();
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                displayQuestion();
            }
        });
    }

    private void loadQuestions(String surveyId) {
        ApiService api = ApiClient.getApiService(AppContext.getInstance().getToken());
        api.getQuestionsBySurvey(surveyId).enqueue(new Callback<List<QuestionDto>>() {
            @Override
            public void onResponse(Call<List<QuestionDto>> call, Response<List<QuestionDto>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    questions = response.body();
                    displayQuestion();
                } else {
                    textQuestionId.setText("Нет вопросов");
                }
            }

            @Override
            public void onFailure(Call<List<QuestionDto>> call, Throwable t) {
                textQuestionId.setText("Ошибка загрузки");
            }
        });
    }

    private void loadQuestionOptions(String templateSurveyId) {
        ApiService api = ApiClient.getApiService(AppContext.getInstance().getToken());
        api.getOptions(templateSurveyId).enqueue(new Callback<List<QuestionOptionDto>>() {
            @Override
            public void onResponse(Call<List<QuestionOptionDto>> call, Response<List<QuestionOptionDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionOptions = response.body();
                    displayQuestion(); // вызвать повторный рендер с опциями
                } else {
                    questionOptions = null;
                    recyclerOptions.setAdapter(null);
                }
            }

            @Override
            public void onFailure(Call<List<QuestionOptionDto>> call, Throwable t) {
                questionOptions = null;
                recyclerOptions.setAdapter(null);
            }
        });
    }

    private void displayQuestion() {
        QuestionDto q = questions.get(currentIndex);
        textQuestionId.setText("ID: " + q.getId());
        editTextQuestion.setText(q.getQuestionText());

        if (questionOptions != null) {
            List<QuestionOptionDto> filtered = questionOptions.stream()
                    .filter(o -> o.getQuestionId().equals(q.getId()))
                    .collect(Collectors.toList());

            recyclerOptions.setAdapter(new QuestionOptionAdapter(filtered));
        } else {
            recyclerOptions.setAdapter(null); // или заглушка
        }
    }
}
