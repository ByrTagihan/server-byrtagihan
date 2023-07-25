package serverbyrtagihan.service;

import org.springframework.data.domain.Page;
import serverbyrtagihan.modal.Message;

import java.util.Map;

public interface MessageService {

    //User
    Page<Message> getAll(String jwtToken, Long page, Long limit, String sort, Long message_type_id, Long message_status_id, String send_as);

    Message add(Message message, String jwtToken);

    Message getById(Long id, String jwtToken);

    Message put(Message message, Long id, String jwtToken);

    Map<String, Boolean> delete(Long id, String jwtToken);
}
