package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.UserOrganizationModel;

@Repository
public interface UserOrganizationRepository  extends JpaRepository<UserOrganizationModel, Long> {
}
