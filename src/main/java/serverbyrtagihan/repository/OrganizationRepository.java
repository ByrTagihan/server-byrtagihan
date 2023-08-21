package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Organization;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query(value = "SELECT * FROM organization  WHERE name  LIKE CONCAT('%',:keyword, '%') OR email LIKE CONCAT('%',:keyword, '%') OR address LIKE CONCAT('%',:keyword, '%') OR hp LIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
    Page<Organization> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query(value = "SELECT * FROM organization  WHERE email LIKE :email", nativeQuery = true)
    Optional<Organization> findByEmail(String email);

    @Query(value = "SELECT * FROM organization  WHERE customer_id LIKE :customerId", nativeQuery = true)
    Optional<Organization> findByCustomerId(Long customerId);
}
