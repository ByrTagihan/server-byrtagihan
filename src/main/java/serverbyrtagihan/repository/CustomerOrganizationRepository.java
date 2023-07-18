package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.CustomerOrganizationModel;
import serverbyrtagihan.modal.Member;

@Repository
public interface CustomerOrganizationRepository  extends JpaRepository<CustomerOrganizationModel, Long> {
    @Query(value = "SELECT * FROM customer_organization  WHERE name  LIKE CONCAT('%',:keyword, '%') OR addres LIKE CONCAT('%',:keyword, '%') OR hp LIKE CONCAT('%',:keyword, '%') OR email LIKE CONCAT('%',:keyword, '%') OR city LIKE CONCAT('%',:keyword, '%') OR provinsi LIKE CONCAT('%',:keyword, '%') OR balance LIKE CONCAT('%',:keyword, '%') OR bank_account_number LIKE CONCAT('%',:keyword, '%') OR bank_account_name LIKE CONCAT('%',:keyword, '%') OR bank_name LIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
    Page<CustomerOrganizationModel> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
