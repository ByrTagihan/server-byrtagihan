package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.MessageMember;

import java.util.List;

public interface MessageMemberRepository extends JpaRepository<MessageMember, Long> {

    List<MessageMember> findByMember(Member member);

}