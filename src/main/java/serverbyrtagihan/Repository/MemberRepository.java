package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.swagger.Modal.Member;

import java.util.UUID;

@Repository
public interface MemberRepository  extends JpaRepository<Member, UUID> {
}
