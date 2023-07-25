package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(value = "SELECT * FROM table_payment  WHERE description  LIKE CONCAT('%',:keyword, '%') OR priode LIKE CONCAT('%',:keyword, '%') OR amount LIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
    Page<Payment> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
