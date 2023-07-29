package serverbyrtagihan.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.repository.UserRepository;
import serverbyrtagihan.impl.CustomerDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.User;

import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private String jwtSecret = "bayartagihan";
    private int jwtExpirationMs = 604800000;
    private static final String SECRET_KEY = "bayartagihan";

    @Autowired
    UserRepository userRepository;
    @Autowired
    MemberRepository memberRepository;

    public String generateJwtToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000 * 168);
        CustomerDetailsImpl adminPrincipal = (CustomerDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .claim("id" , adminPrincipal.getId())
                .setAudience("Customer")
                .setSubject((adminPrincipal.getUsername()))
                .claim("organization_id" , adminPrincipal.getOrganizationIdId())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateToken(String username) {
        return createToken( username);
    }

    private String createToken(String user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        User user1 = userRepository.findByEmail(user).get();
        return Jwts.builder()
                .claim("data", user1)
                .setSubject(user)
                .claim("id" , user1.getId())
                .setAudience("User")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateTokenMember(String username) {
        return createTokenMember( username);
    }

    private String createTokenMember(String uniqueId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000 * 168);
        Member member = memberRepository.findByUniqueId(uniqueId).get();
        return Jwts.builder()
                .setSubject(uniqueId)
                .claim("id" , member.getId())
                .setId(String.valueOf(member.getId()))
                .setAudience("Member")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
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
