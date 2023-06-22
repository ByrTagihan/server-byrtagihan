package serverbyrtagihan.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import serverbyrtagihan.impl.CustomerDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.User;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.repository.UserRepository;

import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;
    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private static final String SECRET_KEY = "codingshooltelogosari";

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
                .claim("type_token" , adminPrincipal.getType())
                .setAudience(adminPrincipal.getType())
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
        Date expiryDate = new Date(now.getTime() + 3600000 * 168);
        User user1 = userRepository.findByEmail(user).get();
        return Jwts.builder()
                .setSubject(user)
                .claim("id" , user1.getId())
                .claim("type_token" , user1.getTypeToken())
                .setAudience(user1.getTypeToken())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateTokenmember(String uniqueId) {
        return createTokenMember( uniqueId);
    }

    private String createTokenMember(String uniqueId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000 * 168);
        Member member = memberRepository.findByUniqueId(uniqueId).get();
        return Jwts.builder()
                .setSubject(member.getUniqueId())
                .claim("id" , member.getId())
                .setId(String.valueOf(member.getId()))
                .claim("type_token" , member.getTypeToken())
                .setAudience(member.getTypeToken())
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
