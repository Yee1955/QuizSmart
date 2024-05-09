package com.example.llama2chatbot;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private List<Message> mMessageList;

        public MessageListAdapter(List<Message> messageList) {
            mMessageList = messageList;
        }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, dateText, timeText, nameText;
        ImageView profileImage;
        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            dateText = (TextView) itemView.findViewById(R.id.text_gchat_date_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = (ImageView) itemView.findViewById(R.id.image_gchat_profile_other);
        }
        void bind(Message message) {
            DateTimeFormatter DateFormatter = null;
            DateTimeFormatter TimeFormatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateFormatter = DateTimeFormatter.ofPattern("dd/MM");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            }
            messageText.setText(message.getContent());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateText.setText(message.getDateTime().format(DateFormatter));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeText.setText(message.getDateTime().format(TimeFormatter));
            }
            nameText.setText(message.getSender());
            // Insert the profile image from the URL into the ImageView.
            // Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, dateText, timeText;
        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            dateText = (TextView) itemView.findViewById(R.id.text_gchat_date_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }
        void bind(Message message) {
            // Initialize DateTimeFormatters
            DateTimeFormatter dateFormatter = null;
            DateTimeFormatter timeFormatter = null;

            // Check once if the OS version supports DateTimeFormatter
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
                timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                // Format and set the date and time texts
                dateText.setText(message.getDateTime().format(dateFormatter));
                timeText.setText(message.getDateTime().format(timeFormatter));
            }

            // Set the message text
            messageText.setText(message.getContent());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.send_item, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.receive_item, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);

        if (message.getSender().equals("Llama 2")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_SENT;
        }
    }
}