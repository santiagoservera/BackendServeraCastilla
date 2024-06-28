package com.ucc.Crudservice.rest;

import com.ucc.Crudservice.security.AuthenticationReq;
import com.ucc.Crudservice.security.JwtUtilService;
import com.ucc.Crudservice.security.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class DemoRest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    private static final Logger logger = LoggerFactory.getLogger(DemoRest.class);

//    Ruta para generar el token al usuario
    @PostMapping("/authenticate")
    public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq) {
        logger.info("Autenticando al usuario {}", authenticationReq.getUsuario());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationReq.getUsuario(), authenticationReq.getClave())
        );

        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUsuario());
        final String jwt = jwtUtilService.generateToken(userDetails);

        return ResponseEntity.ok(new TokenInfo(jwt));
    }
}
