package com.example.a41p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailsActivity extends AppCompatActivity {
    TaskManagerDB taskManagerDB;
    Task task;
    TextView IdTextView;
    TextView TitleTextView;
    TextView DescriptionTextView;
    TextView DueDateTextView;
    Button deleteBtn;
    Button backBtn;
    Button editTaskBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        IdTextView = findViewById(R.id.taskIdTV);
        TitleTextView = findViewById(R.id.titleTV);
        DescriptionTextView = findViewById(R.id.descriptionTV);
        DueDateTextView = findViewById(R.id.dueDateTV);
        editTaskBtn = findViewById(R.id.editTaskBtn);
        deleteBtn = findViewById(R.id.deleteTaskBtn);
        backBtn = findViewById(R.id.backBtn);

        // Get selected ID from previous activity
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        taskManagerDB = new TaskManagerDB(this);
        task = taskManagerDB.getTask(id);

        IdTextView.setText(task.getId());
        TitleTextView.setText(task.getTitle());
        DescriptionTextView.setText(task.getDescription());
        DueDateTextView.setText(task.getDueDate());
        deleteBtn.setEnabled(true);
        editTaskBtn.setEnabled(true);

        editTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskDetailsActivity.this, EditTaskActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskManagerDB.deleteTask(id)) {
                    Toast.makeText(TaskDetailsActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    clearTaskTextView();
                    deleteBtn.setEnabled(false);
                    editTaskBtn.setEnabled(false);
                } else {
                    Toast.makeText(TaskDetailsActivity.this, "Delete Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskDetailsActivity.this, TaskListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void clearTaskTextView() {
        IdTextView.setText("----------");
        TitleTextView.setText("----------");
        DescriptionTextView.setText("----------");
        DueDateTextView.setText("----------");
    }
}