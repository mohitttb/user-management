package fiftyfive.administration.usermanagement.utility;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import fiftyfive.administration.usermanagement.entity.ApiRolePermission;
import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.exception.TokenValidationException;
import fiftyfive.administration.usermanagement.repository.ApiRolePermissionRepository;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Configuration
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    UserRepository userRepository;
    @Value("${jwt.client-secret}")
    private String secretKey;


    private List<ApiRolePermission> permissions;

    @PostConstruct
    public void init() {
        permissions = apiRolePermissionRepository.findAll();
    }

    @Autowired
    private ApiRolePermissionRepository apiRolePermissionRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            try {
                SignedJWT signedJWT = SignedJWT.parse(token);
                JWSVerifier verifier = new MACVerifier(secretKey);

                // Retrieve the requested API endpoint
                String requestedApi = request.getRequestURI();

                String role = extractRole(token);

                // Load the permissions if not already loaded
                if (permissions == null) {
                    permissions = apiRolePermissionRepository.findAll();
                }

                // Check if the role is authorized to access the requested API
                if (!isRoleAuthorized(role, requestedApi)) {
                    throw new AccessDeniedException("unauthorised");
                }

                if (signedJWT.verify(verifier)) {
                    JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                    Long userId = Long.parseLong(claimsSet.getSubject());

                    if (claimsSet.getExpirationTime().before(new Date())) {
                        throw new TokenValidationException("Token has expired");
                    }

                    // Additional validation logic can be added here if required
                    boolean isValid = isValidUser(userId);

                    if (isValid) {
                        // Set the authenticated user ID in the request attribute for further processing
                        request.setAttribute("userId", userId);
                        return true;
                    }
                }
            } catch (JOSEException | ParseException e) {
                // Handle JOSEException and ParseException
                throw new TokenValidationException("Kindly regenerate token");
            }
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        return false;
    }

    private boolean isValidUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent();
    }

    public String extractRole(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        return claimsSet.getStringClaim("role");
    }

    private boolean isRoleAuthorized(String role, String requestedApi) {
        for (ApiRolePermission apiPermission : permissions) {
            if (requestedApi.startsWith(apiPermission.getEndpoints()) && apiPermission.getRoles().contains(role)) {
                return true;
            }
        }
        return false;
    }
}