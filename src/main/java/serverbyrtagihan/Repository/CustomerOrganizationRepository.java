package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.CustomerOrganizationModel;

@Repository
public interface CustomerOrganizationRepository  extends JpaRepository<CustomerOrganizationModel, Long> {
}
