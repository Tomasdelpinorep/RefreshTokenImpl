package net.openwebinars.springboot.restjwt.config;

import lombok.extern.java.Log;
import net.openwebinars.springboot.restjwt.user.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Log
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String usuarioDesconocido = "";

        if (authentication != null) {
            if (authentication.getPrincipal() instanceof User u) {
                return Optional.of(u)
                        .map(User::getId)
                        .map(UUID::toString);
            }

            usuarioDesconocido = authentication.toString();

        }

        log.info("Usuario desconocido " + usuarioDesconocido);
        return Optional.empty();

    }
}

