package com.example.a41p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    RecyclerView recyclerViewMain;
    VerticalAdapter vAdapter;
    TaskManagerDB taskManagerDB;
    List<Task> taskList;
    Button clearTaskBtn;
    Button backBtn;
    ImageView imageView;
    TextView emptyTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        clearTaskBtn = findViewById(R.id.clearAllTaskBtn);
        backBtn = findViewById(R.id.backBtn);
        imageView = findViewById(R.id.imageView);
        emptyTitle = findViewById(R.id.EmptyTitle);

        recyclerViewMain = findViewById(R.id.recyclerView);
        taskManagerDB = new TaskManagerDB(this);
        createTaskList();

        DividerItemDecoration divider = new DividerItemDecoration(recyclerViewMain.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewMain.addItemDecoration(divider);

        clearTaskBtn.setEnabled((!taskManagerDB.getAllTask().isEmpty()));

        clearTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskManagerDB.deleteAllTask();
                Toast.makeText(TaskListActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                onResume();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        createTaskList();
    }

    private void createTaskList() {
        taskList = taskManagerDB.getAllTask();

        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getDueDate().compareTo(o2.getDueDate());
            }
        });

        if (taskList.isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            emptyTitle.setVisibility(View.VISIBLE);
            clearTaskBtn.setEnabled(false);
        }

        vAdapter = new VerticalAdapter(taskList, new onItemClickListener() {
            @Override
            public void itemClick(Task task) {
                Intent intent = new Intent(TaskListActivity.this, TaskDetailsActivity.class);
                intent.putExtra("id", task.getId());
                startActivity(intent);
            }
        });
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMain.setAdapter(vAdapter);
    }
}

class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.ViewHolder> {

    private List<Task> taskList;
    private final onItemClickListener listener;

    public VerticalAdapter(List<Task> taskList, onItemClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView dueDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTV);
            dueDateTextView = itemView.findViewById(R.id.dueDateTV);
        }

        public void bind(Task task, final onItemClickListener listener) {
            titleTextView.setText(task.getTitle());
            dueDateTextView.setText(task.getDueDate());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(task);
                }
            });
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task, listener);
    }

    @Override
    public int getItemCount() {
        if (this.taskList == null) {
            return 0;
        }
        return this.taskList.size();
    }
}

interface onItemClickListener {
    void itemClick(Task task);
}