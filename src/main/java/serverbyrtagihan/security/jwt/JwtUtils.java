package serverbyrtagihan.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import serverbyrtagihan.impl.MemberDetailsImpl;
import serverbyrtagihan.impl.UserDetailsImpl;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.repository.CustomerRepository;
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
    @Autowired
    CustomerRepository customerRepository;



    public String generateJwtToken(Authentication authentication) {
        CustomerDetailsImpl adminPrincipal = (CustomerDetailsImpl) authentication.getPrincipal();
        Customer customer = customerRepository.findByEmail(adminPrincipal.getUsername()).get();
        return Jwts.builder()
                .claim("id" , adminPrincipal.getId())
                .setAudience("Customer")
                .claim("data",customer )
                .setSubject((adminPrincipal.getUsername()))
                .setId(String.valueOf(adminPrincipal.getOrganizationIdId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public String generateToken(Authentication authentication) {
        UserDetailsImpl adminPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByEmail(adminPrincipal.getUsername()).get();
        return Jwts.builder()
                .claim("data", user)
                .setSubject(adminPrincipal.getUsername())
                .claim("id" , adminPrincipal.getId())
                .setAudience("User")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }




    public String generateTokenMember(Authentication authentication) {
        MemberDetailsImpl adminPrincipal = (MemberDetailsImpl) authentication.getPrincipal();
        Member member = memberRepository.findByUniqueId(adminPrincipal.getUsername()).get();
        return Jwts.builder()
                .setSubject(adminPrincipal.getUsername())
                .setId(String.valueOf(adminPrincipal.getId()))
                .setAudience("Member")
                .claim("data", member)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
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
