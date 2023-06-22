package serverbyrtagihan.service;

import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.modal.Organization;
import serverbyrtagihan.dto.OrganizationDto;

import java.util.List;
import java.util.Map;

public interface OrganizationService {

    List<Organization> get(String jwtToken);

    Organization preview(Long id, String jwtToken);

    Organization put(Long id ,OrganizationDto organization , String jwtToken);

    Organization add(OrganizationDto organization, String jwtToken);

    Map<String, Boolean> delete(Long id , String jwtToken);
}
