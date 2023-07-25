package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.modal.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM transaction  WHERE description  LIKE CONCAT('%',:keyword, '%') OR priode LIKE CONCAT('%',:keyword, '%') OR amount LIKE CONCAT('%',:keyword, '%') OR organization_name LIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
    Page<Transaction> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
