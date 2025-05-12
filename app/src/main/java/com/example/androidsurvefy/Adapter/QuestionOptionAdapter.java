package com.example.androidsurvefy.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsurvefy.Model.QuestionOptionDto;
import com.example.androidsurvefy.R;

import java.util.List;

public class QuestionOptionAdapter extends RecyclerView.Adapter<QuestionOptionAdapter.OptionViewHolder> {

    private final List<QuestionOptionDto> options;

    public QuestionOptionAdapter(List<QuestionOptionDto> options) {
        this.options = options;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_stub, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        holder.optionText.setText(options.get(position).getQuestionOptionText());
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView optionText;

        OptionViewHolder(View itemView) {
            super(itemView);
            optionText = itemView.findViewById(R.id.textOption);
        }
    }
}