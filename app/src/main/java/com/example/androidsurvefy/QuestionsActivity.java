package com.example.androidsurvefy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView textQuestion;
    private TextView textNoQuestions;
    private LinearLayout questionContainer;
    private ScrollView scrollContent;
    private RecyclerView recyclerOptions;
    private Button buttonPrev, buttonNext, buttonBack, buttonAddQuestion, buttonDeleteQuestion, buttonAddOption;
    private QuestionDto currentQuestion;
    private List<QuestionOptionDto> questionOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        scrollContent = findViewById(R.id.scrollContent);
        questionContainer = findViewById(R.id.questionContainer);
        textQuestionId = findViewById(R.id.textQuestionId);
        textQuestion = findViewById(R.id.textQuestion);
        textNoQuestions = findViewById(R.id.textNoQuestions);
        recyclerOptions = findViewById(R.id.recyclerOptions);
        buttonAddQuestion = findViewById(R.id.buttonAddQuestion);
        buttonDeleteQuestion = findViewById(R.id.buttonDeleteQuestion);
        recyclerOptions.setLayoutManager(new LinearLayoutManager(this));
        buttonAddOption = findViewById(R.id.buttonAddOption);

        buttonPrev = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        String templateSurveyId = getIntent().getStringExtra("surveyId");
        loadQuestions(templateSurveyId);
        loadQuestionOptions(templateSurveyId);

        buttonBack.setOnClickListener(v -> finish());

        buttonPrev.setOnClickListener(v -> {
            if (questions != null && currentIndex > 0) {
                currentIndex--;
                displayQuestion();
            }
        });

        // button Add question option
        buttonAddOption.setOnClickListener(v -> {
            if (currentQuestion != null) {
                Intent intent = new Intent(QuestionsActivity.this, CreateQuestionOptionActivity.class);
                intent.putExtra("questionId", currentQuestion.getId());
                intent.putExtra("templateSurveyId", currentQuestion.getTemplateSurveyId());
                startActivityForResult(intent, 101);
            }
        });

        buttonDeleteQuestion.setOnClickListener(v -> deleteCurrentQuestion());

        buttonNext.setOnClickListener(v -> {
            if (questions != null && currentIndex < questions.size() - 1) {
                currentIndex++;
                displayQuestion();
            }
        });

        buttonAddQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(QuestionsActivity.this, CreateQuestionActivity.class);
            intent.putExtra("surveyId", getIntent().getStringExtra("surveyId"));
            startActivityForResult(intent, 100);
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
                    questions = null;
                    displayQuestion();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionDto>> call, Throwable t) {
                questions = null;
                displayQuestion();
            }
        });
    }

    private void deleteCurrentQuestion() {
        if (currentQuestion == null) return;

        ApiService api = ApiClient.getApiService(AppContext.getInstance().getToken());
        api.deleteQuestion(currentQuestion.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuestionsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    loadQuestions(getIntent().getStringExtra("surveyId"));
                } else {
                    Toast.makeText(QuestionsActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(QuestionsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            String templateSurveyId = getIntent().getStringExtra("surveyId");
            loadQuestions(templateSurveyId);
        }

        if (requestCode == 101 && resultCode == RESULT_OK) {
            loadQuestionOptions(currentQuestion.getTemplateSurveyId());
        }

        if (requestCode == 102 && resultCode == RESULT_OK && currentQuestion != null) {
            loadQuestionOptions(currentQuestion.getTemplateSurveyId());
        }
    }
    private void loadQuestionOptions(String templateSurveyId) {
        ApiService api = ApiClient.getApiService(AppContext.getInstance().getToken());
        api.getOptions(templateSurveyId).enqueue(new Callback<List<QuestionOptionDto>>() {
            @Override
            public void onResponse(Call<List<QuestionOptionDto>> call, Response<List<QuestionOptionDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionOptions = response.body();
                    displayQuestion();
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
        if (questions == null || questions.isEmpty()) {
            questionContainer.setVisibility(View.GONE);
            recyclerOptions.setVisibility(View.GONE);
            scrollContent.setVisibility(View.GONE);
            textNoQuestions.setVisibility(View.VISIBLE);
            buttonDeleteQuestion.setVisibility(View.GONE);
            return;
        }

        questionContainer.setVisibility(View.VISIBLE);
        recyclerOptions.setVisibility(View.VISIBLE);
        scrollContent.setVisibility(View.VISIBLE);
        textNoQuestions.setVisibility(View.GONE);
        buttonDeleteQuestion.setVisibility(View.VISIBLE);

        currentQuestion = questions.get(currentIndex);

        textQuestionId.setText("ID: " + currentQuestion.getId());
        textQuestion.setText(currentQuestion.getQuestionText());

        List<QuestionOptionDto> filtered = questionOptions != null
                ? questionOptions.stream()
                .filter(o -> o.getQuestionId().equals(currentQuestion.getId()))
                .collect(Collectors.toList())
                : List.of();

        if (filtered.isEmpty()) {
            QuestionOptionDto placeholder = new QuestionOptionDto();
            placeholder.setPlaceholder(true);
            filtered = List.of(placeholder);
        }

        recyclerOptions.setAdapter(new QuestionOptionAdapter(filtered, new QuestionOptionAdapter.OnOptionActionListener() {
            @Override
            public void onEdit(QuestionOptionDto option) {
                Intent intent = new Intent(QuestionsActivity.this, EditQuestionOptionActivity.class);
                intent.putExtra("optionId", option.getId());
                intent.putExtra("optionText", option.getQuestionOptionText());
                intent.putExtra("optionOrder", option.getQuestionOptionOrder());
                startActivityForResult(intent, 102);
            }

            @Override
            public void onDelete(QuestionOptionDto option) {
                ApiService api = ApiClient.getApiService(AppContext.getInstance().getToken());

                api.deleteQuestionOption(option.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(QuestionsActivity.this, "Option deleted", Toast.LENGTH_SHORT).show();
                            if (currentQuestion != null) {
                                loadQuestionOptions(currentQuestion.getTemplateSurveyId());
                            }
                        } else {
                            Toast.makeText(QuestionsActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(QuestionsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
    }


}