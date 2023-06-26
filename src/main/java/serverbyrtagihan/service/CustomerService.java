package serverbyrtagihan.service;

import org.springframework.data.domain.Page;
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

    Customer post(Customer customer, String jwtToken) throws MessagingException;


    Customer put(Customer customer , String jwtToken);

    Customer put2(Customer customer, String jwtToken, Long id);

    Customer putPassword(PasswordDTO passwordDTO, String jwtToken);

    Customer putPicture(Customer customer, String jwtToken);

    Page<Customer> getAll(String jwtToken, Long page, Long pageSize, String sortBy, String sortDirection);

    Page<Customer> searchCustomersWithPagination(String jwtToken, String search, Long page, Long pageSize, String sortBy, String sortDirection);

    ForGotPassword verificationPass(ForGotPassword verification ) throws MessagingException;

    Map<String, Boolean> delete(Long id , String jwtToken);
}
