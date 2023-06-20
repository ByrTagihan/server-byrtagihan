package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.Bill;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId", nativeQuery = true)
    List<Bill> findByMemberId(Long memberId);

    @Query(value = "SELECT * FROM bill WHERE member_id = :memberId AND id = :id", nativeQuery = true)
    Bill findByIdInMember(Long memberId, Long id);

    @Query(value = "DELETE FROM bill WHERE member_id = :memberId AND id = :id", nativeQuery = true)
    void deleteByIdInMember(Long memberId, Long id);
}
