package com.example.ordenes.rest;

import com.example.ordenes.security.AuthenticationReq;
import com.example.ordenes.security.JwtUtilService;
import com.example.ordenes.security.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DemoRest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    private static final Logger logger = LoggerFactory.getLogger(DemoRest.class);

    @PostMapping("/authenticate")
    public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq) {
        logger.info("Autenticando al usuario {}", authenticationReq.getUsuario());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationReq.getUsuario(), authenticationReq.getClave())
            );
        } catch (Exception e) {
            logger.error("Error durante la autenticación", e);
            return ResponseEntity.status(401).build();
        }

        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUsuario());
        final String jwt = jwtUtilService.generateToken(userDetails);

        return ResponseEntity.ok(new TokenInfo(jwt));
    }
}
