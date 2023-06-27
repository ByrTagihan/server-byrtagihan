package serverbyrtagihan.service;


import serverbyrtagihan.modal.CustomerOrganizationModel;

import java.util.*;

public interface CustomerOrganizationService {

    List<CustomerOrganizationModel> getAll(String JwtToken);

    CustomerOrganizationModel put(Long id,CustomerOrganizationModel customer, String JwtToken);

}
