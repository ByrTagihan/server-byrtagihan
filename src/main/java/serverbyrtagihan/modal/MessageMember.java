package serverbyrtagihan.modal;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
    public class MessageMember {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "member_id", referencedColumnName = "id")
        private Member member;

        @Column(name = "message_content")
        private String messageContent;

        @Column(name = "sent_date")
        private LocalDateTime sentDate;

        // Getter and setter methods


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }
}

