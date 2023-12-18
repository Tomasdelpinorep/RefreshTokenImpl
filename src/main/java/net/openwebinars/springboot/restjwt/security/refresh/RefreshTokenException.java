package net.openwebinars.springboot.restjwt.security.refresh;

import net.openwebinars.springboot.restjwt.security.errorhandling.JwtTokenException;

public class RefreshTokenException extends JwtTokenException {
    public RefreshTokenException(String message){
        super(message);
    }
}
