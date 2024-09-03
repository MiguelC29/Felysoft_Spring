package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.dto.AuthenticationRequest;
import com.felysoft.felysoftApp.dto.RegisterRequest;
import com.felysoft.felysoftApp.dto.ReqRes;
import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.repository.UserRepository;
import com.felysoft.felysoftApp.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes register(RegisterRequest authRequest) {
        ReqRes resp = new ReqRes();
        try {
            Optional<User> userByNumIdentification = this.userRepository.findByNumIdentificationAndEliminatedFalse(authRequest.getNumIdentification());
            Optional<User> userByEmail = this.userRepository.findByEmailAndEliminatedFalse(authRequest.getEmail().toLowerCase());

            if (userByNumIdentification.isPresent() || userByEmail.isPresent()) {
                resp.setError("Usuario Existente");
                resp.setMessage("El usuario ya existe. Por favor inicie sesión");
                resp.setStatusCode(502);

                return resp;
            } else {
                var user = User.builder()
                        .numIdentification(authRequest.getNumIdentification())
                        .typeDoc(authRequest.getTypeDoc())
                        .names(authRequest.getNames().toUpperCase())
                        .lastNames(authRequest.getLastNames().toUpperCase())
                        .phoneNumber(authRequest.getPhoneNumber())
                        .user_name(authRequest.getUser_name())
                        .email(authRequest.getEmail().toLowerCase())
                        .password(passwordEncoder.encode(authRequest.getPassword()))
                        .role(Role.CUSTOMER)
                        .enabled(false) // El usuario no está habilitado hasta que verifique su correo
                        .build();

                User userResult = userRepository.save(user);
                if (userResult.getIdUser() > 0) {
                    // Generar un token de verificación y enviar el correo de activación
                    String verificationToken = jwtService.generateVerificationToken(userResult);
                    sendVerificationEmail(userResult, verificationToken);

                    resp.setUser((userResult));
                    resp.setMessage("Usuario Registrado Correctamente. Por favor revise su email para activar su cuenta.");
                    resp.setStatusCode(200);
                }
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    private void sendVerificationEmail(User user, String token) {
        String recipientAddress = user.getEmail();
        String subject = "Activa tu cuenta y completa tu registro | FELYSOFT";
        String confirmationUrl = "http://localhost:3000/activarCuenta?token=" + token;
        //String message = "Por favor, haz clic en el enlace para verificar tu cuenta: " + confirmationUrl;

        String message = """
            <html>
                 <head>
                       <style>
                           body {
                             font-family: sans-serif;
                             background-color: #f5f5f5;
                             color: #fff;
                             margin: 0;
                             padding: 0;
                           }
                           .email-container {
                             background-color: #f5f5f5;
                             display: flex;
                             justify-content: center;
                             align-items: center;
                             min-height: 100vh;
                             padding: 20px;
                           }
                           .email-content {
                             background-color: #ffffff;
                             border-radius: 8px;
                             box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                             padding: 30px;
                             text-align: center;
                             max-width: 450px; /* Tamaño máximo del contenedor */
                             margin: 0 auto;
                           }
                           h1 {
                             font-size: 24px;
                             font-weight: bold;
                             color: #333;
                             margin-bottom: 20px;
                           }
                           h3 {
                             font-size: 18px;
                             margin-bottom: 20px;
                           }
                           h3 span {
                             font-weight: bold;
                             color: #333;
                           }
                           .logo {
                             width: 100px;
                             height: 100px;
                             margin-bottom: 30px;
                           }
                           .description {
                             line-height: 1.6;
                             margin-bottom: 30px;
                           }
                           .button {
                             background-color: #265073;
                             color: #fff;
                             padding: 15px 30px;
                             border: none;
                             border-radius: 5px;
                             font-size: 16px;
                             cursor: pointer;
                             text-decoration: none;
                             display: inline-block;
                             transition: background-color 0.3s ease;
                           }
                           .button:hover {
                             background-color: #1a3d5b;
                           }
                           .small-question {
                             padding: 10px 20px;
                             text-align: center;
                             display: inline-block;
                             font-size: 14px;
                             margin: 20px auto;
                             margin-bottom: 5px;
                             color: #666;
                           }
                           .small-message {
                             margin: 0 auto;
                             padding: 0 10px 20px 20px;
                             text-align: center;
                             font-size: 14px;
                             color: #666;
                           }
                       </style>
                 </head>
                 <body>
                    <div role="article">
                       <table class="email-container" role="presentation" cellpadding="0" cellspacing="0" border="0">
                         <tr>
                           <td>
                             <table class="email-content" role="presentation" cellpadding="0" cellspacing="0" border="0">
                               <tr>
                                 <td>
                                   <img src="https://i.postimg.cc/FznvrwC7/logo.png" alt="Felysoft Logo" class="logo">
                                   <h1>Activa tu cuenta en Felysoft</h1>
                                   <h3>Hola, <span>%s</span></h3>
                                   <p class="description">Pulsa en el siguiente botón para activar tu cuenta y poder completar tu registro correctamente.</p>
                                   <a class="button" href="%s" style="color: white;">Activar cuenta</a>
                                   <p class="small-question">¿Tienes problemas con la activación?</p>
                                   <p class="small-message">Respondenos a este email y te ayudaremos.</p>
                                 </td>
                               </tr>
                             </table>
                           </td>
                         </tr>
                       </table>
                    </div>
                 </body>
            </html>""".formatted(user.getNames(), confirmationUrl);

        emailSenderService.sendEmail(recipientAddress, subject, message);
    }

    public ReqRes verifyAccount(String token) {
        ReqRes resp = new ReqRes();
        try {
            String email = jwtService.extractUsername(token);
            Optional<User> userOptional = userRepository.findByEmailAndEliminatedFalse(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setEnabled(true);
                userRepository.save(user);

                resp.setStatusCode(200);
                resp.setMessage("Cuenta verificada correctamente. Ahora puede iniciar sesión.");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Token invalido o la cuenta no existe.");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Un error ocurrió mientras se verificaba la cuenta: " + e.getMessage());
        }
        return resp;
    }

    public ReqRes login(AuthenticationRequest authRequest) {
        ReqRes response = new ReqRes();
        try {
            Optional<User> userOptional = userRepository.findByEmailAndEliminatedFalse(authRequest.getEmail()); // findByUsername

            if (userOptional.isEmpty()) {
                response.setStatusCode(403);
                response.setError("Usuario no encontrado");
                response.setMessage("El usuario ingresado no existe.");
                return response;
            }

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getEmail(),
                                authRequest.getPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
                response.setStatusCode(403);
                response.setError("Credenciales incorrectas");
                response.setMessage("La contraseña es incorrecta.");
                return response;
            } catch (DisabledException ex) {
                //response.setMessage("Su cuenta está deshabilitada. Por favor, contacte al administrador.");
                response.setStatusCode(403);
                response.setError("Cuenta no verificada");
                response.setMessage("Por favor verifique su email para activar su cuenta.");
                return response;
            } catch (Exception ex) {
                response.setStatusCode(500);
                response.setError("Error de autenticación");
                response.setMessage("Ocurrió un error al intentar iniciar sesión.");
                return response;
            }

            User user = userOptional.get();
            var jwtToken = jwtService.generateToken(user, generateExtraClaims(user));
            //var refreshToken = jwtService.generateRefreshToken(user, new HashMap<>());
            response.setStatusCode(200);
            response.setToken(jwtToken);
            response.setRole(user.getRole());
            // response.setRefreshToken(refreshToken);
            //response.setExpirationTime("15Hrs");
            // Enviar la fecha de expiración en formato ISO 8601
            response.setExpirationTime(new Date(System.currentTimeMillis() + (jwtService.getExpirationMillis())).toInstant().toString());
            response.setMessage("Sesión iniciada correctamente");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }

        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRegist) {
        ReqRes response = new ReqRes();
        try {
            String email = jwtService.extractUsername(refreshTokenRegist.getToken());
            User user = userRepository.findByEmailAndEliminatedFalse(email).orElseThrow();
            if (jwtService.isTokenValid(refreshTokenRegist.getToken(), user)) {
                var jwt = jwtService.generateToken(user, generateExtraClaims(user));
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRegist.getToken());
                response.setExpirationTime("3Hrs");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = userRepository.findByEmailAndEliminatedFalse(email);
            if (userOptional.isPresent()) {
                reqRes.setUser(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("User found successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Un error ocurrió mientras se obtenia la información del usuario: " + e.getMessage());
        }
        return reqRes;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getNames());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("permissions", user.getAuthorities());

        return extraClaims;
    }

    public ReqRes changePassword(String email, String oldPassword, String newPassword) {

        ReqRes reqRes = new ReqRes();
        try {
            // Buscar el usuario por email
            Optional<User> userOptional = userRepository.findByEmailAndEliminatedFalse(email);

            if (userOptional.isEmpty()) {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Usuario no encontrado");
                return reqRes;
            }

            User user = userOptional.get();
            // Validar la contraseña actual
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                reqRes.setStatusCode(400);
                reqRes.setMessage("Contraseña actual incorrecta");
                return reqRes;
            }

            // Si la contraseña actual es correcta, cifrar la nueva contraseña y actualizarla
            user.setPassword(passwordEncoder.encode(newPassword));
            // Actualizar la fecha de última modificación
            user.setLastModification(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);

            reqRes.setStatusCode(200);
            reqRes.setMessage("Contraseña actualizada correctamente");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Un error ocurrió mientras se obtenia la información del usuario: " + e.getMessage());
        }
        return reqRes;
    }
}
