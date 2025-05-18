package com.example.androidsurvefy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidsurvefy.Model.CreateQuestionRequest;
import com.example.androidsurvefy.Model.CreateQuestionResponse;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateQuestionActivity extends AppCompatActivity {

    private EditText editTextQuestionText;
    private EditText editTextQuestionOrder;
    private Button buttonSave;
    private TextView textError;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        editTextQuestionText = findViewById(R.id.editTextQuestionText);
        editTextQuestionOrder = findViewById(R.id.editTextQuestionOrder);
        buttonSave = findViewById(R.id.buttonSaveQuestion);
        textError = findViewById(R.id.textError);
        buttonCancel = findViewById(R.id.buttonCancelQuestion);

        String templateSurveyId = getIntent().getStringExtra("surveyId");
        String questionTypeId = "00000000-0000-0000-0000-000000000000";

        buttonCancel.setOnClickListener(v -> finish());

        buttonSave.setOnClickListener(v -> {
            String text = editTextQuestionText.getText().toString().trim();

            textError.setVisibility(View.GONE);

            if (text.isEmpty()) {
                textError.setText("Question should contain text");
                textError.setVisibility(View.VISIBLE);
                return;
            }

            String orderInput = editTextQuestionOrder.getText().toString().trim();
            int order = 0;

            try {
                if (!orderInput.isEmpty()) {
                    order = Integer.parseInt(orderInput);
                }
            } catch (NumberFormatException e) {
                // Ignore and leave zero
            }

            CreateQuestionRequest request = new CreateQuestionRequest();
            request.setTemplateSurveyId(templateSurveyId);
            request.setQuestionText(text);
            request.setQuestionOrder(order);
            request.setQuestionTypeId(questionTypeId);

            ApiService api = ApiClient.getApiService(AppContext.getInstance().getToken());



            api.createQuestion(request).enqueue(new Callback<CreateQuestionResponse>() {
                @Override
                public void onResponse(Call<CreateQuestionResponse> call, Response<CreateQuestionResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String createdId = response.body().getId();
                        Toast.makeText(CreateQuestionActivity.this, "Question created: " + createdId, Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(CreateQuestionActivity.this, "Error on adding", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CreateQuestionResponse> call, Throwable t) {
                    Toast.makeText(CreateQuestionActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}