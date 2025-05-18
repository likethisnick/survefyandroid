package com.example.androidsurvefy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidsurvefy.Model.CreateQuestionOptionRequest;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateQuestionOptionActivity extends AppCompatActivity {

    private EditText editTextOptionText, editTextOptionOrder;
    private Button buttonSave, buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_option);

        editTextOptionText = findViewById(R.id.editTextOptionText);
        editTextOptionOrder = findViewById(R.id.editTextOptionOrder);
        buttonSave = findViewById(R.id.buttonSaveOption);
        buttonCancel = findViewById(R.id.buttonCancelOption);

        String questionId = getIntent().getStringExtra("questionId");
        String templateSurveyId = getIntent().getStringExtra("templateSurveyId");

        buttonSave.setOnClickListener(v -> {
            String text = editTextOptionText.getText().toString().trim();
            int order = 0;
            try {
                order = Integer.parseInt(editTextOptionOrder.getText().toString().trim());
            } catch (NumberFormatException ignored) {}

            if (text.isEmpty()) {
                Toast.makeText(this, "Option text required", Toast.LENGTH_SHORT).show();
                return;
            }

            CreateQuestionOptionRequest request = new CreateQuestionOptionRequest();
            request.setQuestionId(questionId);
            request.setTemplateSurveyId(templateSurveyId);
            request.setQuestionOptionOrder(order);
            request.setQuestionOptionText(text);

            ApiService api = ApiClient.getApiService(AppContext.getInstance().getToken());
            api.createQuestionOption(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CreateQuestionOptionActivity.this, "Option added", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(CreateQuestionOptionActivity.this, "Failed to add option", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CreateQuestionOptionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        buttonCancel.setOnClickListener(v -> finish());
    }
}