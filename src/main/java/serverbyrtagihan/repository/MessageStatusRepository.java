package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Message_Status;

@Repository
public interface MessageStatusRepository extends JpaRepository<Message_Status, Long> {

}
