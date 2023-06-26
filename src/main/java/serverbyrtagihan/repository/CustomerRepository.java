package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByToken(String token);
    Boolean existsByEmail(String email);
    @Query("SELECT b FROM customer b WHERE LOWER(b.nama) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.email) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.no_hp) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.active) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Customer> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
