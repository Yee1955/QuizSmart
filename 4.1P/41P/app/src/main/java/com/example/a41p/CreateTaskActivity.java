package com.example.a41p;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {
    EditText titleText;
    EditText descriptionText;
    EditText dueDateText;
    Button createBtn;
    Button backBtn;
    Button datePicker;
    TaskManagerDB taskManagerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        taskManagerDB = new TaskManagerDB(this);

        titleText = findViewById(R.id.editTextTitle);
        descriptionText = findViewById(R.id.editTextDescription);
        dueDateText = findViewById(R.id.editTextDueDate);
        createBtn = findViewById(R.id.CreateBtn);
        backBtn = findViewById(R.id.BackBtn);
        datePicker = findViewById(R.id.pickDate);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleText.getText().toString();
                String description = descriptionText.getText().toString();
                String dueDate = dueDateText.getText().toString();

                if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
                    Toast.makeText(CreateTaskActivity.this, "Please fil in all the information", Toast.LENGTH_SHORT).show();
                } else {
                    String id = taskManagerDB.generateUniqueId(taskManagerDB);
                    Task task = new Task(id, title, description, dueDate);
                    taskManagerDB.addTask(task);
                    Toast.makeText(CreateTaskActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
                    clearAllEditText();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTaskActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void clearAllEditText() {
        titleText.setText("");
        descriptionText.setText("");
        dueDateText.setText("");
    }

    public void setDueDate(int year, int month, int day) {
        String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
        dueDateText.setText(date);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker.
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it.
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            ((CreateTaskActivity) getActivity()).setDueDate(year, month, day);
        }
    }
}