package online.entreprenly.platform.sales.interfaces.rest;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Small helper to read the currently authenticated user's identity from the security context.
 *
 * <p>Sales data is multi-tenant: every sale and cash register belongs to the account that
 * created it. The owner is derived from the JWT (its subject is the user email) rather than
 * from the request body, so a client cannot read or mutate another account's sales.</p>
 */
public final class AuthenticatedUser {

    private AuthenticatedUser() {
    }

    /**
     * Returns the email of the currently authenticated user, or {@code null} when there is
     * no authentication in context.
     *
     * @return the authenticated user email, or {@code null}
     */
    public static String email() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
    }
}
