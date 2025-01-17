package com.tech.whatsup.message;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
@Tag(name = "Messages")
public class MessageController {


    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage (@RequestBody MessageRequest message) {
        messageService.saveMessage(message);
    }


    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setMessageToSEEN (@RequestParam("chat-id") String chatID, Authentication authentication) {
        messageService.setMessageToSEEN(chatID, authentication);
    }


    @GetMapping("/chat/{chat-id}")
    public ResponseEntity<List<MessageResponse>> getAllMessages (
            @RequestParam("chat-id") String chatID) {
        return ResponseEntity.ok(messageService.findAllMessages(chatID));
    }

    @PostMapping(value = "/upload-media", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadMedia(
            @RequestParam("chat-id") String chatId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication authentication
    ) {
        messageService.uploadMediaMessage(chatId, file, authentication);
    }

}
