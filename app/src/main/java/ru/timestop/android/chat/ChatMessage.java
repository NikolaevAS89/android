package ru.timestop.android.chat;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class ChatMessage {
    private String user;
    private String message;
    private String photoUrl;
}
