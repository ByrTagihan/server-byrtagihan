package serverbyrtagihan.service;

import org.springframework.web.multipart.MultipartFile;

import serverbyrtagihan.dto.ForGotPass;
import serverbyrtagihan.dto.ProfileDTO;
import serverbyrtagihan.modal.ForGotPassword;
import serverbyrtagihan.modal.User;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    ForGotPass sendEmail(ForGotPass forGotPass) throws MessagingException;

    ForGotPassword verificationPass(ForGotPassword verification ) throws MessagingException;

    User getProfileUser(String jwtToken);

    User update(Long id, ProfileDTO profileDTO , String jwtToken);

    User updatePassword(Long id, User user);

    List<User> getAllTagihan(String jwtToken);
}
