package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Message_Type;

@Repository
public interface MessageTypeRepository extends JpaRepository<Message_Type, Integer> {

}
