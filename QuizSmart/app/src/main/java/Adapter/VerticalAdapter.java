package Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizsmart.R;

import java.util.List;
import Class.*;
import HttpModel.*;

public class VerticalAdapter<T extends DisplayableItem> extends RecyclerView.Adapter<VerticalAdapter<T>.ViewHolder> {

    private List<T> data;
    private onItemClickListener<T> listener;

    public VerticalAdapter(List<T> data, onItemClickListener<T> listener) {
        this.data = data;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (data.get(0) instanceof QuizResponse) initializeQuestionView(itemView);
            else if (data.get(0) instanceof EmployeeSession) initializeCandidatesView(itemView);
        }

        // ------------------ Question Review Adapter ------------------
        TextView QuestionsReviewTV;
        public void questionsBind(T item, int questionNumber) {
            QuestionsReviewTV.setText(questionNumber + ". " + item.getQuestion());
        }

        private void initializeQuestionView(View itemView) {
            QuestionsReviewTV = itemView.findViewById(R.id.QuestionsReviewTextView);
        }

        // ------------------ Candidates Adapter ------------------
        TextView FullNameTV, ScoreTV;
        ImageButton NextBTN;
        public void candidatesBind(T item) {
            item.getEmployeeAsync(new EmployeeSession.EmployeeCallback() {
                @Override
                public void onEmployeeLoaded(Employee employee) {
                    FullNameTV.setText(employee.getFullName());
                }

                @Override
                public void onError(String error) {
                    FullNameTV.setText("Unknown");
                }
            });
            ScoreTV.setText(String.valueOf(item.getAverageScore()));
            NextBTN.setOnClickListener(view -> listener.itemClick(item));
        }

        private void initializeCandidatesView(View itemView) {
            FullNameTV = itemView.findViewById(R.id.FullNameTextView);
            ScoreTV = itemView.findViewById(R.id.ScoreTextView);
            NextBTN = itemView.findViewById(R.id.NextButton);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (data.get(0) instanceof QuizResponse) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_review, parent, false);
        } else if (data.get(0) instanceof EmployeeSession) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidate, parent, false);
        } else {
            throw new IllegalArgumentException("Unknown class type for items");
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if ((data.get(0) instanceof QuizResponse)) holder.questionsBind(data.get(position), position + 1);
        else if (data.get(0) instanceof EmployeeSession) holder.candidatesBind((data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface onItemClickListener<T> {
        void itemClick(T item);
    }
}
