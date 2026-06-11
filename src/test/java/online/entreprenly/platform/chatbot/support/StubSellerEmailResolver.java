package online.entreprenly.platform.chatbot.support;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;

import java.util.Optional;
import java.util.function.Function;

/**
 * Test double for {@link SellerEmailResolver}. Wraps a single id→email function so
 * tests stay terse, while supplying a no-op reverse lookup (resolveSellerId) that
 * the order/message flows do not exercise.
 */
public class StubSellerEmailResolver implements SellerEmailResolver {

    private final Function<Long, Optional<String>> byId;

    public StubSellerEmailResolver(Function<Long, Optional<String>> byId) {
        this.byId = byId;
    }

    @Override
    public Optional<String> resolveEmail(Long sellerId) {
        return byId.apply(sellerId);
    }

    @Override
    public Optional<Long> resolveSellerId(String email) {
        return Optional.empty();
    }
}
