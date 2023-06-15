package serverbyrtagihan.service;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Modal.Customer;
import serverbyrtagihan.Modal.ForGotPassword;
import serverbyrtagihan.dto.ForGotPass;
import serverbyrtagihan.dto.PasswordDTO;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    ForGotPass sendEmail(ForGotPass forGotPass) throws MessagingException;

    Customer getProfileCustomer(String jwtToken);

    List<Customer> getAll();

    Customer put(Customer customer , String jwtToken);

    Customer putPassword(PasswordDTO passwordDTO, String jwtToken);

    Customer putPicture(Customer customer, String jwtToken);

    ForGotPassword verificationPass(ForGotPassword verification ) throws MessagingException;

    Map<String, Boolean> delete(Long id);
}
