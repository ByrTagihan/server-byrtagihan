package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Member;

import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<Member, Long> {

    Optional<Member> findByUniqueId(String uniqueId);

    Optional<Member> findByToken(String token);

    Boolean existsByUniqueId(String uniqueId);

}
