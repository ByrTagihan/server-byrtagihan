package serverbyrtagihan.Service;

import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Modal.Customer;
import serverbyrtagihan.Modal.User;
import serverbyrtagihan.dto.Login;
import serverbyrtagihan.dto.ProfileDTO;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface UserService {

    void sendEmail(String to) throws MessagingException;

    User register(User user);

    User getProfileUser(String jwtToken);

    User update(Long id, ProfileDTO profileDTO, MultipartFile multipartFile, String jwtToken);

    User updatePassword(Long id, User user);

    List<User> getAllTagihan();
}
