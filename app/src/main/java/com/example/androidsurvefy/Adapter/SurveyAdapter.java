package com.example.androidsurvefy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsurvefy.Model.TemplateSurveyDto;
import com.example.androidsurvefy.R;
import com.example.androidsurvefy.SurveyDetailActivity;

import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder> {
    private List<TemplateSurveyDto> surveys;
    private Context context;

    public SurveyAdapter(List<TemplateSurveyDto> surveys, Context context) {
        this.surveys = surveys;
        this.context = context;
    }

    @NonNull
    @Override
    public SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey, parent, false);
        return new SurveyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyViewHolder holder, int position) {
        TemplateSurveyDto survey = surveys.get(position);
        holder.title.setText(survey.getName());
        holder.description.setText(survey.getDescription());

        holder.openButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, SurveyDetailActivity.class);
            intent.putExtra("id", survey.getId());
            intent.putExtra("name", survey.getName());
            intent.putExtra("description", survey.getDescription());
            intent.putExtra("createdOn", survey.getCreatedOn());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return surveys.size();
    }

    static class SurveyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        Button openButton;

        SurveyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textSurveyTitle);
            description = itemView.findViewById(R.id.textSurveyDescription);
            openButton = itemView.findViewById(R.id.buttonOpen);
        }
    }
}