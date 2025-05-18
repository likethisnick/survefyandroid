package com.example.androidsurvefy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidsurvefy.Model.UpdateQuestionOptionRequest;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditQuestionOptionActivity extends AppCompatActivity {

    private EditText editTextOptionText;
    private EditText editTextOptionOrder;
    private Button buttonSave;

    private String optionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question_option);

        editTextOptionText = findViewById(R.id.editTextOptionText);
        editTextOptionOrder = findViewById(R.id.editTextOptionOrder);
        buttonSave = findViewById(R.id.buttonSaveOption);

        optionId = getIntent().getStringExtra("optionId");
        String text = getIntent().getStringExtra("optionText");
        int order = getIntent().getIntExtra("optionOrder", 0);

        editTextOptionText.setText(text);
        editTextOptionOrder.setText(String.valueOf(order));

        buttonSave.setOnClickListener(v -> {
            String updatedText = editTextOptionText.getText().toString().trim();
            int updatedOrder = 0;
            try {
                updatedOrder = Integer.parseInt(editTextOptionOrder.getText().toString().trim());
            } catch (NumberFormatException ignored) {}

            if (updatedText.isEmpty()) {
                Toast.makeText(this, "Text required", Toast.LENGTH_SHORT).show();
                return;
            }

            UpdateQuestionOptionRequest request = new UpdateQuestionOptionRequest();
            request.setId(optionId);
            request.setQuestionOptionText(updatedText);
            request.setQuestionOptionOrder(updatedOrder);

            ApiService api = ApiClient.getApiService(AppContext.getInstance().getToken());
            api.updateQuestionOption(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditQuestionOptionActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditQuestionOptionActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(EditQuestionOptionActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}