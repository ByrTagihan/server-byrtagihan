package serverbyrtagihan.service;


import org.springframework.data.domain.Page;
import serverbyrtagihan.modal.CustomerOrganizationModel;

import java.util.*;

public interface CustomerOrganizationService {
    CustomerOrganizationModel add(CustomerOrganizationModel customer, String JwtToken);

    Page<CustomerOrganizationModel> getAll(String jwtToken, Long page, Long limit, String sort, String search);

    CustomerOrganizationModel preview(Long id, String JwtToken);
    CustomerOrganizationModel put(Long id,CustomerOrganizationModel customer, String JwtToken);

}
