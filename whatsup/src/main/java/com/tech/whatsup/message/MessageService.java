package com.tech.whatsup.message;

import com.tech.whatsup.chat.Chat;
import com.tech.whatsup.chat.ChatRepository;
import com.tech.whatsup.file.FileService;
import com.tech.whatsup.notification.Notification;
import com.tech.whatsup.notification.NotificationService;
import com.tech.whatsup.notification.NotificationType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService{

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper mapper;
    private final FileService fileService;
    private final NotificationService notificationService;


    public void saveMessage (MessageRequest messageRequest) {


        Chat chat = chatRepository.findById(messageRequest.getChatID())
                .orElseThrow(() -> new EntityNotFoundException("chat not found"));

        Message message = new Message();
        message.setChat(chat);
        message.setContent(messageRequest.getContent());
        message.setType(messageRequest.getType());
        message.setState(MessageState.SENT);
        message.setReceiverId(messageRequest.getReceiverId());
        message.setSenderId(message.getSenderId());

        messageRepository.save(message);


        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .messageType(messageRequest.getType())
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .receiverId(messageRequest.getReceiverId())
                .type(NotificationType.MESSAGE)
                .chatName(chat.getTargetChatName(message.getSenderId()))
                .build();

        notificationService.sendNotification(messageRequest.getReceiverId(), notification);



    }



    @Transactional
    public void setMessageToSEEN (String chatID, Authentication authentication) {
        Chat chat = chatRepository.findById(chatID)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        final String recipientId = getRecipientId(chat, authentication);

        messageRepository.setMessagesToSeenByChatId(chatID, MessageState.SEEN);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .type(NotificationType.SEEN)
                .receiverId(recipientId)
                .senderId(getSenderId(chat, authentication))
                .build();

        notificationService.sendNotification(recipientId, notification);
    }

    public List<MessageResponse> findAllMessages (String chatID) {
        return messageRepository.findMessagesByChatId(chatID)
                .stream()
                .map(mapper ::  toMessageResponse)
                .toList();
    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        final String senderId = getSenderId(chat, authentication);
        final String receiverId = getRecipientId(chat, authentication);

        final String filePath = fileService.saveFile(file, senderId);
        Message message = new Message();
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setState(MessageState.SENT);
        message.setType(MessageType.IMAGE);
        message.setMediaFilePath(filePath);
        message.setChat(chat);
        messageRepository.save(message); }

    private String getSenderId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getSender().getId();
        }
        return chat.getRecipient().getId();
    }

    private String getRecipientId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }
}