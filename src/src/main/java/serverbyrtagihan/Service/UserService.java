package serverbyrtagihan.Service;

import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Modal.Customer;
import serverbyrtagihan.Modal.ForGotPassword;
import serverbyrtagihan.Modal.User;
import serverbyrtagihan.dto.ForGotPass;
import serverbyrtagihan.dto.Login;
import serverbyrtagihan.dto.ProfileDTO;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface UserService {

    ForGotPass sendEmail(ForGotPass forGotPass) throws MessagingException;

    ForGotPassword verificationPass(ForGotPassword verification ) throws MessagingException;

    User getProfileUser(String jwtToken);

    User update(Long id, ProfileDTO profileDTO, MultipartFile multipartFile, String jwtToken);

    User updatePassword(Long id, User user);

    List<User> getAllTagihan(String jwtToken);
}
