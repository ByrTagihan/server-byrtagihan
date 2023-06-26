package serverbyrtagihan.service;

import serverbyrtagihan.dto.ForGotPass;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.modal.ForGotPassword;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    ForGotPass sendEmail(ForGotPass forGotPass) throws MessagingException;

    Customer getProfileCustomer(String jwtToken);

    Customer getById(Long id);

    List<Customer> getAll();

    Customer put(Customer customer , String jwtToken);

    Customer put2(Customer customer, String jwtToken, Long id);

    Customer putPassword(PasswordDTO passwordDTO, String jwtToken);

    Customer putPicture(Customer customer, String jwtToken);

    ForGotPassword verificationPass(ForGotPassword verification ) throws MessagingException;

    Map<String, Boolean> delete(Long id , String jwtToken);
}
