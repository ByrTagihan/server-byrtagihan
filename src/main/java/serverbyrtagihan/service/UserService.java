package serverbyrtagihan.service;

import serverbyrtagihan.dto.ForGotPass;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.dto.ProfileDTO;
import serverbyrtagihan.modal.Reset_Password;
import serverbyrtagihan.modal.User;
import serverbyrtagihan.response.LoginRequest;

import javax.mail.MessagingException;
import java.util.Map;

public interface UserService {

    ForGotPass sendEmail(ForGotPass forGotPass) throws MessagingException;

    Map<String, Object> verificationPass(Reset_Password verification ) throws MessagingException;


    Map<Object, Object> login(LoginRequest loginRequest);

    User update(ProfileDTO profileDTO , String jwtToken);

    User updatePassword(Long id, User user);

    User getProfile(String jwtToken);

    Map<String, Integer> getRecapTotal(String jwtToken);

    User putPassword(PasswordDTO passwordDTO, String jwtToken);
}
