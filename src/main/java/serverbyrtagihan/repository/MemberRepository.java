package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Member;

import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<Member, Long> {
    @Query(value = "SELECT * FROM member  WHERE name  LIKE CONCAT('%',:keyword, '%') OR unique_id LIKE CONCAT('%',:keyword, '%') OR addres LIKE CONCAT('%',:keyword, '%') OR hp LIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
    Page<Member> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<Member> findByUniqueId(String uniqueId);


    Optional<Member> findByToken(String token);

    Boolean existsByUniqueId(String uniqueId);

    @Query("SELECT u FROM Member u WHERE u.hp = ?1")
    Member findByHp(String hp);
}