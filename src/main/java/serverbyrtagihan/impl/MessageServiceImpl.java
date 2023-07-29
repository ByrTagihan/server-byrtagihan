package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Message;
import serverbyrtagihan.modal.Message_Type;
import serverbyrtagihan.modal.Template;
import serverbyrtagihan.repository.MessageRepository;
import serverbyrtagihan.repository.MessageStatusRepository;
import serverbyrtagihan.repository.MessageTypeRepository;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.MessageService;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageTypeRepository messageTypeRepository;

    @Autowired
    MessageStatusRepository messageStatusRepository;
    @Autowired
    private JwtUtils jwtUtils;

    //User
    @Override
    public Page<Message> getAll(String jwtToken, Long page, Long limit, String sort, Long message_type_id, Long message_status_id, String send_as) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sort.startsWith("-")) {
            sort = sort.substring(1);
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(limit), direction, sort);
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            if (message_type_id != null || message_status_id != null || send_as != null) {
                return messageRepository.findByMessageTypeAndStatusAndSendAs(message_type_id, message_status_id, send_as, pageable);
            } else {
                return messageRepository.findAll(pageable);
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Message add(Message message, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Message_Type type = messageTypeRepository.findById(message.getMessage_type_id()).orElseThrow(() -> new NotFoundException("Not found"));;
            message.setMessage_type_name(type.getName());
            return messageRepository.save(message);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Message getById(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return messageRepository.findById(id).orElseThrow(()-> new NotFoundException("Not found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Message put(Message message, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Message messages = messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
            messages.setMessage_type_id(message.getMessage_type_id());
            messages.setMessage(message.getMessage());
            messages.setReceiver(message.getReceiver());
            messages.setSubject(message.getSubject());
            messages.setSend_as(message.getSend_as());
            return messageRepository.save(messages);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Message putStatus(Message message, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Message messages = messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
            messages.setMessage_status_id(message.getMessage_status_id());

            return messageRepository.save(messages);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Map<String, Boolean> delete(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            try {
                messageRepository.deleteById(id);
                Map<String, Boolean> res = new HashMap<>();
                res.put("Deleted", Boolean.TRUE);
                return res;
            } catch (Exception e) {
                throw new NotFoundException("id not found");
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
}
