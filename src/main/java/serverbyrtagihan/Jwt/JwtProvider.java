package serverbyrtagihan.Jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.TemporaryToken;
import serverbyrtagihan.repository.ByrTagihanRepository;
import serverbyrtagihan.Repository.TokenRepository;
import serverbyrtagihan.exception.InternalErrorException;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private static String secretKey = "Token";
    private static Integer expired = 900000;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ByrTagihanRepository registerRepository;

    public String generateToken(UserDetails userDetails) {
        String token = UUID.randomUUID().toString().replace("-", "");
        ByrTagihan user = registerRepository.findByEmail(userDetails.getUsername());
        var checkingToken = tokenRepository.findByRegisterId(user.getId());
        if (checkingToken.isPresent()) tokenRepository.deleteById(checkingToken.get().getId());
        TemporaryToken temporaryToken = new TemporaryToken();
        temporaryToken.setToken(token);
        temporaryToken.setExpiredDate(new Date(new Date().getTime() + expired));
        temporaryToken.setRegisterId(user.getId());
        tokenRepository.save(temporaryToken);
        return token;
    }

    public TemporaryToken getSubject(String token) {
        return tokenRepository.findByToken(token).orElseThrow(() -> new InternalErrorException("Token error parse"));
    }

    public boolean checkingTokenJwt(String token) {
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
