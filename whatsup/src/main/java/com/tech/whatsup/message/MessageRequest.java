package com.tech.whatsup.message;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MessageRequest {

    private String content;
    private MessageType type;
    private String chatID;
    private String senderId;
    private String receiverId;


}
