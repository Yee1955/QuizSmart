package Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizsmart.R;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import API.ApiClient;
import API.ApiService;
import Class.*;
import EmployeeActivity.HistoryActivity;
import Enumerable.SessionStatus;
import HttpModel.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            else if (data.get(0) instanceof Session) initializeEmployerHistoryView(itemView);
            else if (data.get(0) instanceof EmployeeHistoryDTO) initializeEmployeeHistoryView(itemView);
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
            NextBTN.setOnClickListener(view -> listener.itemClick1(item));
        }

        private void initializeCandidatesView(View itemView) {
            FullNameTV = itemView.findViewById(R.id.FullNameTextView);
            ScoreTV = itemView.findViewById(R.id.ScoreTextView);
            NextBTN = itemView.findViewById(R.id.NextButton);
        }

        // ------------------ Employer's Session History Adapter ------------------
        TextView CompletedTV, DateTV, PositionTV, StatusTV;
        LinearLayout DetailsBTN;

        @SuppressLint("ResourceAsColor")
        public void employerSessionBind(T item) {
            DateTimeFormatter formatter = null;
            String formattedDate = "?? ??? ????";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                formattedDate = item.getDate().format(formatter);
            }
            // Getting Count
            ApiService apiService = ApiClient.getApiService("DB");
            Call<Integer> call = apiService.getNumOfCompleted(item.getId());
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    System.out.println(response);
                    if (response.isSuccessful()) CompletedTV.setText("Completed:"+response.body());
                    else CompletedTV.setText("Completed:0");
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
            // Setup components
            DateTV.setText(formattedDate);
            PositionTV.setText(item.getJobPosition());
            StatusTV.setText(item.getStatus().name());
            if (item.getStatus().equals(SessionStatus.Started)) {
                StatusTV.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.light_yellow));
            } else if (item.getStatus().equals(SessionStatus.Ended)) {
                StatusTV.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.light_red));
            }
            DetailsBTN.setOnClickListener(view -> listener.itemClick1(item));
        }
        private void initializeEmployerHistoryView(View itemView) {
            CompletedTV = itemView.findViewById(R.id.PositionTextView);
            DateTV = itemView.findViewById(R.id.DateTextView);
            PositionTV = itemView.findViewById(R.id.CompanyNameTextView);
            StatusTV = itemView.findViewById(R.id.StatusTextView);
            DetailsBTN = itemView.findViewById(R.id.DetailsButton);
        }


        // ------------------ Employee's Session History Adapter ------------------
        TextView CompanyNameTV, SessionDateTV, JobPositionTV, SessionStatusTV;
        LinearLayout SessionDetailsBTN;
        public void employeeSessionBind(T item) {
            DateTimeFormatter formatter = null;
            String formattedDate = "?? ??? ????";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                formattedDate = item.getDate().format(formatter);
            }
            CompanyNameTV.setText(item.getCompanyName());
            SessionDateTV.setText(formattedDate);
            JobPositionTV.setText(item.getJobPosition());
            SessionStatusTV.setText(item.getStatus().name());
            if (item.getStatus().equals(SessionStatus.Started)) {
                SessionStatusTV.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.light_yellow));
                SessionDetailsBTN.setOnClickListener(view -> listener.itemClick1(item));
            } else if (item.getStatus().equals(SessionStatus.Completed)) {
                SessionStatusTV.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.light_blue));
                SessionDetailsBTN.setOnClickListener(view -> listener.itemClick2(item));
            }
        }
        private void initializeEmployeeHistoryView(View itemView) {
            CompanyNameTV = itemView.findViewById(R.id.CompanyNameTextView);
            SessionDateTV = itemView.findViewById(R.id.DateTextView);
            JobPositionTV = itemView.findViewById(R.id.PositionTextView);
            SessionStatusTV = itemView.findViewById(R.id.StatusTextView);
            SessionDetailsBTN = itemView.findViewById(R.id.DetailsButton);
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
        } else if (data.get(0) instanceof Session) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        } else if (data.get(0) instanceof EmployeeHistoryDTO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        } else {
            throw new IllegalArgumentException("Unknown class type for items");
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if ((data.get(0) instanceof QuizResponse)) holder.questionsBind(data.get(position), position + 1);
        else if (data.get(0) instanceof EmployeeSession) holder.candidatesBind((data.get(position)));
        else if (data.get(0) instanceof Session) holder.employerSessionBind((data.get(position)));
        else if (data.get(0) instanceof EmployeeHistoryDTO) holder.employeeSessionBind((data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface onItemClickListener<T> {
        void itemClick1(T item);
        void itemClick2(T item);
    }
}
