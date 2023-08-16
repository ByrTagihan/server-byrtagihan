package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM transaction  WHERE description  LIKE CONCAT('%',:keyword, '%') OR priode LIKE CONCAT('%',:keyword, '%') OR amount LIKE CONCAT('%',:keyword, '%') OR organization_name LIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
    Page<Transaction> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT priode AS periode, " +
            "COUNT(1) AS count_bill, " +
            "SUM(amount) AS total_bill " +
            "FROM transaction " +
            "WHERE YEAR(priode) = :year AND organization_id = :organizationId " +
            "GROUP BY priode " +
            "ORDER BY priode",nativeQuery = true)
    List<Object[]> getReport(int year, String organizationId);
    @Query(value = "SELECT priode AS periode, " +
            "COUNT(1) AS count_bill, " +
            "SUM(amount) AS total_bill " +
            "FROM transaction " +
            "WHERE YEAR(priode) = :year " +
            "GROUP BY priode " +
            "ORDER BY priode",nativeQuery = true)
    List<Object[]> getReportRoleUser(int year);
    @Query(value = "SELECT * FROM transaction WHERE organization_id = :organizationId ", nativeQuery = true)
    List<Transaction> findByOrganizationId(String organizationId);
}
