package serverbyrtagihan.service;

import org.springframework.web.multipart.MultipartFile;

import serverbyrtagihan.dto.ForGotPass;
import serverbyrtagihan.dto.ProfileDTO;
import serverbyrtagihan.modal.ForGotPassword;
import serverbyrtagihan.modal.User;
import serverbyrtagihan.response.LoginRequest;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface UserService {

    ForGotPass sendEmail(ForGotPass forGotPass) throws MessagingException;

    ForGotPassword verificationPass(ForGotPassword verification ) throws MessagingException;


    Map<Object, Object> login(LoginRequest loginRequest);

    User update(ProfileDTO profileDTO , String jwtToken);

    User updatePassword(Long id, User user);

    User getProfile(String jwtToken);

    Map<String, Integer> getRecapTotal(String jwtToken);
}
