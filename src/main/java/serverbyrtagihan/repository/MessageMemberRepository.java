package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.Message;

import java.util.List;

public interface MessageMemberRepository extends JpaRepository<Message, Long> {

    List<Message> findByMember(Member member);

}