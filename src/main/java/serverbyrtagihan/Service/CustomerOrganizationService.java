package serverbyrtagihan.Service;

import serverbyrtagihan.Modal.CustomerOrganizationModel;

import java.util.*;

public interface CustomerOrganizationService {
    CustomerOrganizationModel add(CustomerOrganizationModel customer, String JwtToken);

    CustomerOrganizationModel preview(Long id,String JwtToken);

    List<CustomerOrganizationModel> getAll(String JwtToken);

    CustomerOrganizationModel put(Long id,CustomerOrganizationModel customer, String JwtToken);

    Map<String, Boolean> delete(Long id,String JwtToken);
}
