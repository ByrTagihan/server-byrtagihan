package serverbyrtagihan.service;

import org.springframework.data.domain.Page;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Organization;
import serverbyrtagihan.dto.OrganizationDto;

import java.util.List;
import java.util.Map;

public interface OrganizationService {
    //User
    Page<Organization> getAll(String jwtToken, Long page, Long limit, String sort, String search);

    Organization preview(Long id, String jwtToken);

    Organization put(Long id ,Organization organization , String jwtToken);

    Organization add(Organization organization, String jwtToken);

    Map<String, Boolean> delete(Long id , String jwtToken);

    //Customer
    Organization getByCustomerId(String jwtToken);

    Organization putByCustomerId(Organization organization , String jwtToken);
}
