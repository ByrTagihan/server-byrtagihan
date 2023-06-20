package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId", nativeQuery = true)
    Page<Bill> findByMemberId(Long memberId, Pageable pageable);

    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId AND id = :id", nativeQuery = true)
    Bill findByIdInMember(Long memberId, Long id);

    @Query(value = "DELETE FROM bill WHERE member_id = :memberId AND id = :id", nativeQuery = true)
    void deleteByIdInMember(Long memberId, Long id);
}
