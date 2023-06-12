package serverbyrtagihan.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.model.Customer;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface ProfileService {

    void sendEmail(String to) throws MessagingException;

    Customer getProfileCustomer(String jwtToken);

    List<Customer> getAll();

    Customer put(Customer customer , String jwtToken);

    Customer putPassword(Customer customer, String jwtToken);

    Customer putPicture(Customer customer, MultipartFile multipartFile, String jwtToken);

    Map<String, Boolean> delete(Long id);
}
