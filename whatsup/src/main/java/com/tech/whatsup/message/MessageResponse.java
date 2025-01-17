package com.tech.whatsup.message;



import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageResponse {

    private Long id;
    private String content;
    private MessageState state;
    private MessageType type;
    private String senderId;
    private String receiverId;
    private byte[] media;
    private LocalDateTime createdAt;
}



