package com.example.backend.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backend.dto.AuthenticationResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.exceptions.SocialMediaException;
import com.example.backend.model.NotificationEmail;
import com.example.backend.model.User;
import com.example.backend.model.VerificationToken;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VerificationTokenRepository;
import com.example.backend.security.JwtProvider;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
	
	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private VerificationTokenRepository verificationTokenRepository;
	private MailService mailService;
	private AuthenticationManager authenticationManager;
	private JwtProvider jwtProvider;
	

	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user = userSets(registerRequest);
		userRepository.save(user);
		
		String token = generatedVerificationToken(user);
		
		mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
		
	}

	private User userSets(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		return user;
	}

	private String generatedVerificationToken(User user) {
		String token= UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationTokenRepository.save(verificationToken);
		return token;
	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> existToken= verificationTokenRepository.findByToken(token);
		existToken.orElseThrow(()-> new SocialMediaException("Invalid Token"));
		fetchUserAndEnable(existToken.get());
	}
	
	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username=verificationToken.getUser().getUsername();
		User existUser=userRepository.findByUsername(username).
					   orElseThrow(()-> new SocialMediaException
					   ("User not found with name :" +username));
		existUser.setEnabled(true);
		userRepository.save(existUser);
	
	}
	
	 @Transactional
	    public User getCurrentUser() {
	        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
	                getContext().getAuthentication().getPrincipal();
	        return userRepository.findByUsername(principal.getUsername())
	                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
	    }
	
	
	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
										  (loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token=jwtProvider.generateToken(authentication);
		return new AuthenticationResponse(token,loginRequest.getUsername());
		
	}
}