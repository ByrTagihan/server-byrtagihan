package serverbyrtagihan.customerProfile;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.customerProfile.CustomerOrganizationModel;


@Repository

public interface CustomerOrganizationRepository extends CrudRepository<CustomerOrganizationModel, Integer> {
    CustomerOrganizationModel findById(long id);

}
