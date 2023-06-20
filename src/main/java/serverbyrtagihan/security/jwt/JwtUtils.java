package serverbyrtagihan.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import serverbyrtagihan.Impl.CustomerDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import serverbyrtagihan.Impl.UserDetailsImpl;

import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;
    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private static final String SECRET_KEY = "codingshooltelogosari";

    public String generateJwtToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000 * 3); // Set token expiration to 1 hour
        CustomerDetailsImpl adminPrincipal = (CustomerDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .claim("id" , adminPrincipal.getId())
                .claim("type_token" , adminPrincipal.getType())
                .setAudience(adminPrincipal.getType())
                .setSubject((adminPrincipal.getUsername()))
                .claim("organization_id" , adminPrincipal.getOrganizationIdId())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateJwtTokenUser(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000 * 3);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .claim("id" , userDetails.getId())
                .claim("type_token" , userDetails.getType())
                .setAudience(userDetails.getType())
                .setSubject((userDetails.getUsername()))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public static Claims decodeJwt(String jwtToken) {
        Jws<Claims> jwsClaims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwtToken);

        return jwsClaims.getBody();
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
