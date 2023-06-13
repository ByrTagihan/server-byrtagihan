package serverbyrtagihan.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.CustomerOrganizationModel;


@Repository

public interface CustomerOrganizationRepository extends CrudRepository<CustomerOrganizationModel, Integer> {
    CustomerOrganizationModel findById(long id);

}
