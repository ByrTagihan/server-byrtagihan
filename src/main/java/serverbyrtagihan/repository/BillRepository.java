package serverbyrtagihan.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import serverbyrtagihan.modal.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId", nativeQuery = true)
    Page<Bill> findByMemberId(Long memberId, Pageable pageable);

    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId AND id = :id", nativeQuery = true)
    Bill findByIdInMember(Long memberId, Long id);

    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId", nativeQuery = true)
    Page<Bill> findBillsByMember(String memberId, Pageable pageable);

    @Query("SELECT b FROM Bill b WHERE LOWER(b.description) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.memberName) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.periode) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.amount) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Bill> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
