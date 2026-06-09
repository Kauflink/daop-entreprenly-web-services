package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.application.commandservices.ChatbotConversationService;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundReceiptCommand;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.ChatMessageResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.InboundReceiptResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.InboundWhatsAppMessageResource;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.ChatMessageResourceFromEntityAssembler;
import online.entreprenly.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inbound WhatsApp webhook.
 *
 * <p>Receives client messages, routes them to a conversation, persists them, lets the
 * chatbot answer automatically and pushes the changes in real time. The verification
 * endpoint mirrors the WhatsApp Cloud API handshake so a real channel can be plugged
 * in later without changing this contract.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/chatbot/whatsapp/webhook")
@Tag(name = "Chatbot - WhatsApp Webhook", description = "Inbound WhatsApp message endpoints")
public class ChatbotWebhookController {

    private final ChatbotConversationService conversationService;
    private final String verifyToken;

    public ChatbotWebhookController(
            ChatbotConversationService conversationService,
            @org.springframework.beans.factory.annotation.Value("${chatbot.whatsapp.verify-token:entreprenly-verify}")
            String verifyToken) {
        this.conversationService = conversationService;
        this.verifyToken = verifyToken;
    }

    @GetMapping
    @Operation(summary = "Verify the WhatsApp webhook",
            description = "Echoes the challenge when the verify token matches, per the WhatsApp Cloud API handshake.")
    @ApiResponse(responseCode = "200", description = "Challenge echoed")
    public ResponseEntity<String> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge) {
        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Receive an inbound WhatsApp message",
            description = "Persists the client message and returns the chatbot's automatic reply.")
    @ApiResponse(responseCode = "201", description = "Message processed and reply generated")
    public ResponseEntity<?> receive(@Valid @RequestBody InboundWhatsAppMessageResource resource) {
        var command = new HandleInboundMessageCommand(
                resource.fromPhone(), resource.clientName(), resource.content(), resource.ownerEmail());
        var result = conversationService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ChatMessageResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PostMapping(value = "/receipt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Receive a WhatsApp payment receipt",
            description = "Attaches the receipt image to the order awaiting payment for the seller to review.")
    @ApiResponse(responseCode = "201", description = "Receipt attached")
    public ResponseEntity<?> receiveReceipt(@Valid @RequestBody InboundReceiptResource resource) {
        var command = new HandleInboundReceiptCommand(
                resource.fromPhone(), resource.ownerEmail(), resource.image());
        var result = conversationService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ChatMessageResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}
