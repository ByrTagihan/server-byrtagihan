package serverbyrtagihan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.MessageMember;
import serverbyrtagihan.repository.MessageMemberRepository;

import java.time.LocalDateTime;

@Service
public class MessageMemberImpl {
    @Autowired
    private MessageMemberRepository messageMemberRepository;

    public void sendForgotPasswordSMS(String hp, Member member) {
        // Implement logic to send the SMS to the provided phone number

        // After sending the SMS, save the message to the "message_member" table
        MessageMember messageMember = new MessageMember();
        messageMember.setMember(member); // Set the member associated with the message
        messageMember.setMessageContent("Forgot password SMS content"); // Set the message content
        messageMember.setSentDate(LocalDateTime.now()); // Set the sent date
        messageMemberRepository.save(messageMember); // Save the message to the database
    }
}
