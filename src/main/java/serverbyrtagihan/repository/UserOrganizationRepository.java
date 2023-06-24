package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.UserOrganizationModel;

@Repository
public interface UserOrganizationRepository  extends JpaRepository<UserOrganizationModel, Long> {
}