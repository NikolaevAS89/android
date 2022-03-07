package ru.timestop.android.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import ru.timestop.android.model.ChatMessage;
import ru.timestop.android.myapplication.R;

public class MessageAdaptors extends ArrayAdapter<ChatMessage> {

    public MessageAdaptors(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message, parent, false);
        }
        ImageView userPhoto = convertView.findViewById(R.id.userPhoto);
        TextView userMessage = convertView.findViewById(R.id.userMessage);
        TextView userName = convertView.findViewById(R.id.userName);
        ProgressBar userProgress = convertView.findViewById(R.id.messageProgress);
        ChatMessage message = getItem(position);
        userName.setText(message.getUser());
        if (message.getMessage() == null) {
            userMessage.setVisibility(View.GONE);
            userPhoto.setVisibility(View.VISIBLE);
            Glide.with(userPhoto.getContext()).load(message.getPhotoUrl()).into(userPhoto);
        } else {
            userPhoto.setVisibility(View.GONE);
            userMessage.setVisibility(View.VISIBLE);
            userMessage.setText(message.getMessage());
        }
        return convertView;
    }

    public static class MessageHolder {

    }
}
