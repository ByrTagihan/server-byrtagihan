package serverbyrtagihan.service;

import serverbyrtagihan.modal.CustomerOrganizationModel;

import java.util.*;

public interface CustomerOrganizationService {
    CustomerOrganizationModel add(CustomerOrganizationModel customer);

    CustomerOrganizationModel getById(Long id);

    List<CustomerOrganizationModel> getAll();

    CustomerOrganizationModel put(CustomerOrganizationModel customer, Long id);

    Map<String, Boolean> delete(Long id);
}
