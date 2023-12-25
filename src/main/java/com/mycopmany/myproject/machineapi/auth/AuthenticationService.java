package com.mycopmany.myproject.machineapi.auth;

import com.mycopmany.myproject.machineapi.config.JwtService;
import com.mycopmany.myproject.machineapi.exception.UnauthorizedException;
import com.mycopmany.myproject.machineapi.user.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserToCreate userToCreate) {
        String encodedPassword = passwordEncoder.encode(userToCreate.getPassword());
        User user = new User(userToCreate.getFirstName(),
                userToCreate.getLastName(),
                userToCreate.getUsername(),
                encodedPassword,
                Role.USER);
        userRepository.save(user);
        String jwToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwToken);
    }

    public AuthenticationResponse authenticate(UserToLogin userToLogin) {
        Optional<User> userOptional = userRepository.findByUsername(userToLogin.getUsername());
        User user = userOptional.orElseThrow(() -> new UnauthorizedException("Wrong username or password"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), userToLogin.getPassword())
            );

            String jwToken = jwtService.generateToken(user);

            return new AuthenticationResponse(jwToken);

        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Wrong username or password");
        }
    }


}
