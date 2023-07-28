package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId", nativeQuery = true)
    Page<Bill> findByMemberId(Long memberId, Pageable pageable);

    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId AND id = :id", nativeQuery = true)
    Bill findByIdInMember(Long memberId, Long id);
    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId", nativeQuery = true)
    Page<Bill> findByMemberId(String memberId, Pageable pageable);

    @Query("SELECT b FROM Bill b WHERE LOWER(b.description) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.member_name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.periode) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.amount) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Bill> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Bill b WHERE LOWER(b.description) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.member_name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.periode) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.amount) LIKE LOWER(concat('%', :keyword, '%') AND member_id = :memberId )")
    Page<Bill> findAllByKeywordMember(Long memberId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Bill b WHERE LOWER(b.description) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.member_name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.periode) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.amount) LIKE LOWER(concat('%', :keyword, '%') AND member_id = :memberId )")
    Page<Bill> findAllByKeywordMember(String memberId, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT periode AS periode, " +
            "COUNT(1) AS count_bill, " +
            "SUM(amount) AS total_bill, " +
            "SUM(CASE WHEN paid_id = 0 THEN amount ELSE 0 END) AS unpaid_bill, " +
            "SUM(CASE WHEN paid_id > 0 THEN amount ELSE 0 END) AS paid_bill " +
            "FROM bill " +
            "WHERE YEAR(periode) = :year AND organization_id = :organizationId " +
            "GROUP BY periode " +
            "ORDER BY periode", nativeQuery = true)
    List<Object[]> getBillingSummaryByYearAndOrganizationId(int year, String organizationId);

}
