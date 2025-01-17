package com.tech.whatsup.chat;




import com.tech.whatsup.user.User;
import com.tech.whatsup.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper mapper;


    public String createChat(String senderID, String receiverID) {

       Optional<Chat> existingChat = chatRepository.findChatByReceiverAndSender(senderID, receiverID);
        if(existingChat.isPresent()){
            return existingChat.get().getId();
        }

        User sender = userRepository.findByPublicId(senderID)
                .orElseThrow(() -> new EntityNotFoundException("User with id" + senderID + "not found"));

        User receiver = userRepository.findByPublicId(receiverID)
                .orElseThrow(() -> new EntityNotFoundException("user with id" + receiverID + "not found"));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setRecipient(receiver);

        Chat savedChat = chatRepository.save(chat);
        return savedChat.getId();



    }

    @Transactional(readOnly = true)
    public List<ChatResponse> getChatByReceiver(Authentication currentUser) {

        final String userId = currentUser.getName();
        return chatRepository.findChatsBySenderId(userId)
                .stream()
                .map(c -> mapper.toChatResponse(c, userId))
                .toList();
    }
}
