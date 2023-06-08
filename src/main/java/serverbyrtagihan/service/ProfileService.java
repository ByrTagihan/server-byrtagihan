package serverbyrtagihan.service;

import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.model.Customer;

import java.util.List;
import java.util.Map;

public interface ProfileService {
    Customer add(Customer customer, MultipartFile multipartFile);

    Customer getById(Long id);

    List<Customer> getAll();

    Customer put(Customer customer, Long id);

    Customer putPicture(Customer customer, MultipartFile multipartFile, Long id);

    Map<String, Boolean> delete(Long id);
}
