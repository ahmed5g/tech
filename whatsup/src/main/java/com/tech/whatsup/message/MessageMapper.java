package com.tech.whatsup.message;


import com.tech.whatsup.file.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {
    public MessageResponse toMessageResponse (Message message){
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .type(message.getType())
                .state(message.getState())
                .receiverId(message.getReceiverId())
                .senderId(message.getSenderId())
                .createdAt(message.getCreatedDate())
                .media(FileUtils.readFileFromLocation(message.getMediaFilePath()))
                .build();
    }
}
