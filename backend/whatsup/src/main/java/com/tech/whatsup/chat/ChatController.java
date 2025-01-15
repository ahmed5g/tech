package com.tech.whatsup.chat;

import com.tech.whatsup.common.StringResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "Chat")
public class ChatController {


    private final ChatService chatService;
    @PostMapping
    public ResponseEntity<StringResponse> createChat (
            @RequestParam(name = "sender_id")
            String senderID,
            @RequestParam(name = "recevierID")
            String receiverID
    ){

        final String chatID = chatService.createChat(senderID, receiverID);
        StringResponse response = StringResponse.builder()
                .response(chatID)
                .build();
        return
                ResponseEntity.ok(response);

    }


    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChatByReceiver(Authentication authentication){
        return
                ResponseEntity.ok(chatService.getChatByReceiver(authentication));
    }

}
