package serverbyrtagihan.Jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.MemberLogin;
import serverbyrtagihan.Modal.MemberPrinciple;
import serverbyrtagihan.Modal.TemporaryToken;
import serverbyrtagihan.Repository.ByrTagihanRepository;
import serverbyrtagihan.Repository.MemberLoginRepository;
import serverbyrtagihan.Repository.TokenRepository;
import serverbyrtagihan.exception.InternalErrorException;

import java.util.Date;
import java.util.UUID;


@Component
public class JwtUtils {
    private static String secretKey = "Token";
    private static Integer expired = 900000;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    MemberLoginRepository registerRepository;

    public String generateTokenMember(UserDetails userDetails) {
        String token = UUID.randomUUID().toString().replace("-", "");
        MemberLogin user = registerRepository.memberByUnique(userDetails.getUsername());
        var checkingToken = tokenRepository.findByMemberId(user.getId());
        if (checkingToken.isPresent()) tokenRepository.deleteById(checkingToken.get().getId());
        TemporaryToken temporaryToken = new TemporaryToken();
        temporaryToken.setToken(token);
        temporaryToken.setExpiredDate(new Date(new Date().getTime() + expired));
        temporaryToken.setMemberId(user.getId());
        tokenRepository.save(temporaryToken);
        return token;
    }

    public TemporaryToken getSubjectMember(String token) {
        return tokenRepository.findByToken(token).orElseThrow(() -> new InternalErrorException("Token error parse"));
    }


    public boolean checkingTokenJwtMember(String token) {
        TemporaryToken tokenExist = tokenRepository.findByToken(token).orElse(null);
        if (tokenExist == null) {
            System.out.println("Token kosong");
            return false;
        }
        if (tokenExist.getExpiredDate().before(new Date())) {
            System.out.println("Token expired");
            return false;
        }
        return true;
    }

}