package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value= "SELECT * FROM message WHERE message_type_id = :memberTypeId", nativeQuery = true)
    Page<Message> findAllByMessageTypeId(Long memberTypeId, Pageable pageable);

    @Query(value= "SELECT * FROM message WHERE message_status_id = :messageStatusId", nativeQuery = true)
    Page<Message> findAllByMessageStatusId(Long messageStatusId, Pageable pageable);

    @Query(value= "SELECT * FROM message WHERE send_as = :sendAs", nativeQuery = true)
    Page<Message> findAllBySendAs(String sendAs, Pageable pageable);

    @Query(value= "SELECT * FROM message WHERE message_type_id = :memberTypeId AND message_status_id = :messageStatusId AND send_as = :sendAs", nativeQuery = true)
    Page<Message> findAllByKeyword(Long memberTypeId, Long messageStatusId, String sendAs, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE " +
            "(:message_type_id IS NULL OR m.message_type_id = :message_type_id) " +
            "AND (:message_status_id IS NULL OR m.message_status_id = :message_status_id) " +
            "AND (:send_as IS NULL OR m.send_as = :send_as)")
    Page<Message> findByMessageTypeAndStatusAndSendAs(
            @Param("message_type_id") Long messageTypeId,
            @Param("message_status_id") Long messageStatusId,
            @Param("send_as") String sendAs,
            Pageable pageable);

}
