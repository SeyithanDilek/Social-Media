package com.example.backend.service;

import java.time.Instant;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.backend.exceptions.SocialMediaException;
import com.example.backend.model.RefreshToken;
import com.example.backend.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SocialMediaException("Invalid refresh Token"));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

}
