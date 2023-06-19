package fiftyfive.administration.usermanagement.utility;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserValidation {

    @Autowired
    UserRepository userRepository;
    @Value("${jwt.client-secret}")
    private String secretKey;
    @Value("${jwt.expiration-time.seconds}")
    private Long expirationTime;

    public User isUserExists(String constant, Long userId) throws UserNotExistsException {
        return userRepository.findAll().stream()
                .filter(user -> user.getId() != null && user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotExistsException(getExceptionMessage(constant, userId.toString())));
    }

    public String generateToken(User user) {
        try {
            JWSSigner signer = new MACSigner(secretKey);

            // Prepare JWT with claims
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.subject(user.getId().toString());
            builder.issueTime(new Date());
            builder.claim("role", user.getRole());
            Instant expirationInstant = Instant.now().plus(expirationTime, ChronoUnit.SECONDS);

            builder.expirationTime(Date.from(expirationInstant));
            JWTClaimsSet claimsSet = builder.build();

            // Create the signed JWT
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            // Sign the JWT
            signedJWT.sign(signer);

            // Serialize the JWT to a string
            return signedJWT.serialize();

        } catch (JOSEException e) {
            // Handle JOSEException
            e.printStackTrace();
            return null;
        }
    }

    private String getExceptionMessage(String constant, String userName) {
        return String.format(constant, userName);
    }

}

