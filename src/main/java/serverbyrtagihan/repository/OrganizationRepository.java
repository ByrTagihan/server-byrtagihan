package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Organization;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

//    @Query(value = "SELECT * FROM organization WHERE customer_id = :customer_id", nativeQuery = true)
//    List<Organization> findByCustomerId(Long customerId);

}
