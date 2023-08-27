package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<Member, Long> {
    @Query(value = "SELECT * FROM member  WHERE name  LIKE CONCAT('%',:keyword, '%') OR unique_id LIKE CONCAT('%',:keyword, '%') OR addres LIKE CONCAT('%',:keyword, '%') OR hp LIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
    Page<Member> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query(value = "SELECT * FROM member  WHERE unique_id LIKE :unique_id", nativeQuery = true)
    Optional<Member> findByUniqueId(String unique_id);

    @Query(value = "SELECT * FROM member WHERE organization_id = :organizationId ", nativeQuery = true)
    List<Member> findByOrganizationId(String organizationId);

    @Query(value = "SELECT * FROM member WHERE va_number = :va_number ", nativeQuery = true)
    Boolean existsByVa_number(String va_number);

    @Query(value = "SELECT * FROM member WHERE hp = :hp ", nativeQuery = true)
    Boolean existsByHp(String hp);
}
