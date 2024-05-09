package API;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.llama2chatbot.Message;
import com.example.llama2chatbot.MessageListAdapter;
import com.example.llama2chatbot.R;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    RecyclerView ChatRV;
    EditText ChatET;
    ImageButton SendBTN;
    Message initialMessage;
    List<Message> MessageList;
    MessageListAdapter messageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_acitivity);
        initialMessage = (Message) getIntent().getSerializableExtra("Message");

        MessageList = new ArrayList<>();

        // Setup view components
        ChatRV = findViewById(R.id.recyclerView);
        ChatET = findViewById(R.id.ChatEditText);
        SendBTN = findViewById(R.id.SendButton);

        // Setup edittext
        ChatET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SendBTN.setEnabled(!s.toString().trim().isEmpty());
            }
        });

        // Setup button
        SendBTN.setEnabled(false);
        SendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ChatET.getText().toString().trim();
                if (!content.isEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Message message = new Message(content, initialMessage.getSender(), LocalDateTime.now());
                        MessageList.add(message);
                        SetupRecyclerView();
                        ChatET.getText().clear();
                        SetupChatResponse(content);
                    }
                }
            }
        });

        SetupChatResponse(initialMessage.getContent());
    }

    private void SetupRecyclerView() {
        messageListAdapter = new MessageListAdapter(MessageList);
        LinearLayoutManager linearLayour = new LinearLayoutManager(this);
        linearLayour.setStackFromEnd(true);
        ChatRV.setLayoutManager(linearLayour);
        ChatRV.setAdapter(messageListAdapter);

    }

    private void SetupChatResponse(String inputMessage) {
        // Initialize chat histories object
        List<ChatHistory> ChatHistories = new ArrayList<>();

        // Assuming the first message is always from the system or an initial message
        if (MessageList.size() > 0 && initialMessage != null) {
            // Start from the initial message
            ChatHistories.add(new ChatHistory(initialMessage.getContent(), ""));
        }

        // Add subsequent pairs of user and bot messages
        for (int i = 0; i < MessageList.size(); i++) {
            if (i + 1 < MessageList.size()) {
                ChatHistory history = new ChatHistory(MessageList.get(i + 1).getContent(), MessageList.get(i).getContent());
                ChatHistories.add(history);
                i++;
            } else {
                // For the last message if it's not paired
                ChatHistories.add(new ChatHistory("", MessageList.get(i).getContent()));
            }
        }

        // Setup chat response
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, TimeUnit.MINUTES).build())
                .build();
        ChatApiService apiService = retrofit.create(ChatApiService.class);
        Call<ChatResponse> call = apiService.postChatRequest(new Chat(inputMessage, ChatHistories));
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("ChatActivity", "Raw JSON response: " + new Gson().toJson(response.body()));
                    ChatResponse chatResponse = response.body();
                    String llamaResponse = chatResponse.getResponse();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Message responseMessage = new Message(llamaResponse, "Llama 2", LocalDateTime.now());
                        MessageList.add(responseMessage);
                        SetupRecyclerView();
                    }
                } else {
                    Log.e("ChatActivity", "Response not successful: " + response.code());
                    Toast.makeText(ChatActivity.this, "Response not successful: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable throwable) {
                Log.e("ChatActivity", "Failed to fetch data");
                Toast.makeText(ChatActivity.this, "Unable to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}