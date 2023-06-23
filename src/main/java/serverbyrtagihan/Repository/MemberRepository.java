package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.Member;

import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<Member, Long> {

    Optional<Member> findByUniqueId(String uniqueId);

}
