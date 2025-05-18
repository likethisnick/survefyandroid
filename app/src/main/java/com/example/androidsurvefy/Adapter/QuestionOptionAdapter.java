package com.example.androidsurvefy.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsurvefy.Model.QuestionOptionDto;
import com.example.androidsurvefy.R;

import java.util.List;

public class QuestionOptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnOptionActionListener {
        void onEdit(QuestionOptionDto option);
        void onDelete(QuestionOptionDto option);
    }
    private final List<QuestionOptionDto> options;
    private final OnOptionActionListener listener;
    public QuestionOptionAdapter(List<QuestionOptionDto> options, OnOptionActionListener listener) {
        this.options = options;
        this.listener = listener;
    }

    private static final int TYPE_PLACEHOLDER = 0;
    private static final int TYPE_OPTION = 1;

    @Override
    public int getItemViewType(int position) {
        return options.get(position).isPlaceholder() ? TYPE_PLACEHOLDER : TYPE_OPTION;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PLACEHOLDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_stub, parent, false);
            return new PlaceholderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
            return new OptionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_OPTION) {
            QuestionOptionDto option = options.get(position);
            OptionViewHolder optionHolder = (OptionViewHolder) holder;
            optionHolder.optionText.setText(option.getQuestionOptionText());
            optionHolder.buttonEdit.setOnClickListener(v -> listener.onEdit(option));
            optionHolder.buttonDelete.setOnClickListener(v -> listener.onDelete(option));
        }
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView optionText;
        Button buttonEdit, buttonDelete;

        OptionViewHolder(View itemView) {
            super(itemView);

            buttonEdit = itemView.findViewById(R.id.buttonEditOption);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteOption);
            optionText = itemView.findViewById(R.id.textOption);
        }
    }
    static class PlaceholderViewHolder extends RecyclerView.ViewHolder {
        PlaceholderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
