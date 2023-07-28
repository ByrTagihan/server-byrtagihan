package serverbyrtagihan.service;

import serverbyrtagihan.modal.Organization;
import serverbyrtagihan.dto.OrganizationDto;

import java.util.List;
import java.util.Map;

public interface OrganizationService {

    List<Organization> get(String jwtToken);

    List<Organization> getOrganizationCustomer(String jwtToken);

    Organization preview(Long id, String jwtToken);

    Organization put(Long id ,Organization organization , String jwtToken);

    Organization putOrganizationCustomer(Long id , Organization organization , String jwtToken);

    Organization add(Organization organization, String jwtToken);

    Map<String, Boolean> delete(Long id , String jwtToken);
}
