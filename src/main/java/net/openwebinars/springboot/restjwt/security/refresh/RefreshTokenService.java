package net.openwebinars.springboot.restjwt.security.refresh;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.openwebinars.springboot.restjwt.user.model.User;
import net.openwebinars.springboot.restjwt.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    @Value("${jwt.refresh.duration}")
    private int durationInMinutes;

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(durationInMinutes * 60));

        refreshToken = refreshTokenRepository.save((refreshToken));

        return refreshToken;
    }

    //Mejor usar este creo, suelta excepción si no existe el usuario
    // y si existe llama al método de arriba
    public RefreshToken createRefreshToken(UUID userId) {
        return userService.findById(userId)
                .map(this::createRefreshToken)
                .orElseThrow(() -> new UsernameNotFoundException("Error al crear el token de refresco"));
    }

    public RefreshToken verify(RefreshToken refreshToken){
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Expired refresh token: " + refreshToken.getToken() +  ". Please log in again.");
        }

        return refreshToken;
    }

    @Transactional //para asegurarse que no haya errores
    public int deleteByUser(User user){
        return refreshTokenRepository.deleteByUser(user);
    }



}
