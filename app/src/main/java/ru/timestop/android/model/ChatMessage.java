package ru.timestop.android.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class ChatMessage {
    String user;
    String message;
    String photoUrl;
}
