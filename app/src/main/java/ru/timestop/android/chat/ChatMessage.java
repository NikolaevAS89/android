package ru.timestop.android.chat;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class ChatMessage {
    String user;
    String message;
    String photoUrl;
}
