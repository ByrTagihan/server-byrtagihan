package serverbyrtagihan.Service;

import serverbyrtagihan.Modal.Bill;
import serverbyrtagihan.Modal.Channel;
import serverbyrtagihan.Modal.Customer;
import serverbyrtagihan.Modal.Organization;
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
