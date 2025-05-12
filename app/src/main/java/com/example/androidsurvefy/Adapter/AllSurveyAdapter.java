package com.example.androidsurvefy.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsurvefy.Model.TemplateSurveyDto;
import com.example.androidsurvefy.R;

import java.util.List;

public class AllSurveyAdapter extends RecyclerView.Adapter<AllSurveyAdapter.SurveyViewHolder> {

    private final List<TemplateSurveyDto> surveys;

    public AllSurveyAdapter(List<TemplateSurveyDto> surveys) {
        this.surveys = surveys;
    }

    @NonNull
    @Override
    public SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey, parent, false);
        return new SurveyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyViewHolder holder, int position) {
        TemplateSurveyDto s = surveys.get(position);
        holder.name.setText(s.getName());
        holder.description.setText(s.getDescription());
        holder.created.setText(s.getCreatedOn());
    }

    @Override
    public int getItemCount() {
        return surveys.size();
    }

    static class SurveyViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, created;

        SurveyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textSurveyName);
            description = itemView.findViewById(R.id.textSurveyDescription);
            created = itemView.findViewById(R.id.textSurveyCreated);
        }
    }
}