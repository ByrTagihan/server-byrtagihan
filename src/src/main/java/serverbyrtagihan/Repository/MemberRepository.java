package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Member;

import java.util.UUID;

@Repository
public interface MemberRepository  extends JpaRepository<Member, UUID> {
}
