package serverbyrtagihan.service;

import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Modal.Customer;
import serverbyrtagihan.dto.PasswordDTO;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    void sendEmail(String to) throws MessagingException;

    Customer getProfileCustomer(String jwtToken);

    List<Customer> getAll();

    Customer put(Customer customer , String jwtToken);

    Customer putPassword(PasswordDTO passwordDTO, String jwtToken);

    Customer putPicture(Customer customer, MultipartFile multipartFile, String jwtToken);

    Customer putPass(Customer customer, Long id);

    Map<String, Boolean> delete(Long id);
}
