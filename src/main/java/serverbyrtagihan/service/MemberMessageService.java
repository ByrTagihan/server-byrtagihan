package serverbyrtagihan.service;

import serverbyrtagihan.modal.MessageMember;

import java.util.List;

public interface MemberMessageService {

    void saveMessage(String numberPhone, String content);

    List<MessageMember> getAllMessages();

    List<MessageMember> getMessagesByRecipientPhone(String numberPhone);

    // Tambahkan method untuk mengakses data tabel message

    MessageMember getMessageById(Long messageId);

    void deleteMessage(Long messageId);


    }


