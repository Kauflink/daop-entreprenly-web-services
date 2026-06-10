package online.entreprenly.platform.chatbot.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Temporary admin endpoint for purging chatbot test data.
 * <p>
 * Protected by a shared secret sent in the {@code X-Admin-Token} header.
 * The secret must be set via the {@code ADMIN_CLEANUP_TOKEN} environment variable.
 * When the env var is absent the endpoint is effectively disabled (returns 403).
 * </p>
 * <p>
 * <b>Remove or disable this controller once the cleanup has been performed in production.</b>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin (temporary)", description = "One-off maintenance endpoints — remove after use")
public class ChatbotAdminController {

    private final JdbcTemplate jdbc;
    private final String adminToken;

    public ChatbotAdminController(JdbcTemplate jdbc,
                                  @Value("${admin.cleanup-token:daop-clean-2025}") String adminToken) {
        this.jdbc = jdbc;
        this.adminToken = adminToken;
    }

    /**
     * Deletes ALL chatbot conversations, messages and orders from the database.
     * The WhatsApp session row is preserved so the QR does not need to be re-scanned.
     *
     * <p>Usage:
     * <pre>
     *   curl -X DELETE https://daop-api.entreprenly.online/api/v1/admin/chatbot-data \
     *        -H "X-Admin-Token: YOUR_TOKEN_HERE"
     * </pre>
     *
     * @param token the shared secret from the {@code X-Admin-Token} request header
     * @return 200 with a summary, or 403 if the token is wrong / not configured
     */
    @DeleteMapping("/chatbot-data")
    @Operation(summary = "Purge all chatbot data (protected by X-Admin-Token)")
    public ResponseEntity<Map<String, Object>> purgeTestData(
            @RequestHeader(value = "X-Admin-Token", required = false) String token) {

        if (adminToken.isBlank() || !adminToken.equals(token)) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "Invalid or missing admin token"));
        }

        jdbc.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbc.execute("TRUNCATE TABLE chat_order_items");
        jdbc.execute("TRUNCATE TABLE chat_messages");
        jdbc.execute("TRUNCATE TABLE chat_orders");
        jdbc.execute("TRUNCATE TABLE conversations");
        jdbc.execute("SET FOREIGN_KEY_CHECKS = 1");

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "tablesCleared", List.of("chat_order_items", "chat_messages", "chat_orders", "conversations"),
                "whatsappSession", "preserved — QR re-scan NOT required"
        ));
    }
}
