package Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningexperienceapp.R;

import java.util.List;
import Class.*;

public class VerticalAdapter<T extends DisplayableItem> extends RecyclerView.Adapter<VerticalAdapter<T>.ViewHolder> {

    private List<T> data;
    private onItemClickListener<T> listener;
    private String inputClass;

    public VerticalAdapter(List<T> data, onItemClickListener<T> listener) {
        this.data = data;
        this.listener = listener;
        // Determine the class of the first item to configure the view type
        if (!data.isEmpty()) {
            if (data.get(0) instanceof Task) this.inputClass = "Task";
            else if (data.get(0) instanceof Quiz) this.inputClass = "Quiz";
            else if (data.get(0) instanceof Result) this.inputClass = "Result";
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView TV1, TV2;
        ImageView TaskBTN;
        RadioButton BTN1, BTN2, BTN3, BTN4;
        RadioGroup RG1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (inputClass.equals("Task")) initializeTaskView(itemView);
            else if (inputClass.equals("Quiz")) initializeQuizView(itemView);
            else if (inputClass.equals("Result")) initializeResultView(itemView);
        }

        public void taskBind(T item) {
            TV1.setText(item.getTitle());
            TV2.setText(item.getDescription());
            TaskBTN.setOnClickListener(view -> listener.itemClick(item, null));
        }

        public void quizBind(T item, int position) {
            TV1.setText("Question " + (position + 1));
            TV2.setText(item.getQuestion());
            BTN1.setText(item.getOptions()[0]);
            BTN2.setText(item.getOptions()[1]);
            BTN3.setText(item.getOptions()[2]);
            BTN4.setText(item.getOptions()[3]);

            RG1.clearCheck(); // Clear any previous selection
            RG1.setOnCheckedChangeListener((group, checkedId) -> {
                String selection = null;
                if (checkedId == R.id.AnswerRadioButton1) {
                    selection = "A";
                } else if (checkedId == R.id.AnswerRadioButton2) {
                    selection = "B";
                } else if (checkedId == R.id.AnswerRadioButton3) {
                    selection = "C";
                } else if (checkedId == R.id.AnswerRadioButton4) {
                    selection = "D";
                }
                if (selection != null) {
                    listener.itemClick(item, selection);
                }
            });

        }

        public void ResultBind(T item, int position) {
            TV1.setText((position + 1) + ". " + item.getTitle());
            TV1.setTextColor(Color.parseColor("#bf000a"));
            TV2.setText(item.getDescription());
            TV2.setTextColor(Color.parseColor("#bf000a"));
        }

        private void initializeTaskView(View itemView) {
            TV1 = itemView.findViewById(R.id.GeneratedTaskTextView);
            TV2 = itemView.findViewById(R.id.DescriptionTextView);
            TaskBTN = itemView.findViewById(R.id.imageViewButton);
        }

        private void initializeQuizView(View itemView) {
            TV1 = itemView.findViewById(R.id.QuestionTextView);
            TV2 = itemView.findViewById(R.id.DescriptionTextView);
            BTN1 = itemView.findViewById(R.id.AnswerRadioButton1);
            BTN2 = itemView.findViewById(R.id.AnswerRadioButton2);
            BTN3 = itemView.findViewById(R.id.AnswerRadioButton3);
            BTN4 = itemView.findViewById(R.id.AnswerRadioButton4);
            RG1 = itemView.findViewById(R.id.RadioGroup1);
        }

        private void initializeResultView(View itemView) {
            TV1 = itemView.findViewById(R.id.QuestionTextView);
            TV2 = itemView.findViewById(R.id.DescriptionTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (inputClass.equals("Task")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        } else if (inputClass.equals("Quiz")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        } else if (inputClass.equals("Result")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        } else {
            throw new IllegalArgumentException("Unknown class type for items");
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (inputClass.equals("Task")) holder.taskBind(data.get(position));
        else if (inputClass.equals("Quiz")) holder.quizBind(data.get(position), position);
        else if (inputClass.equals("Result")) holder.ResultBind(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface onItemClickListener<T> {
        void itemClick(T item, String selection);
    }
}
