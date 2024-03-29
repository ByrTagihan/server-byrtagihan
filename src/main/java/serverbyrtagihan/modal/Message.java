package serverbyrtagihan.modal;

import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class Message extends DateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_type_id")
    private int message_type_id;

    @Column(name = "message_type_name")
    private String message_type_name = "";

    @Column(name = "subject")
    private String subject;

    @Lob
    @Column(name = "message")
    private String message;

    @Column(name = "message_status_id")
    private int message_status_id = 0;

    @Column(name = "message_status_name")
    private String message_status_name = "";

    @Column(name = "receiver")
    private String receiver = "";

    @Column(name = "receiver_cc")
    private String receiver_cc = "";

    @Column(name = "attachment")
    private String attachment = "";

    @Column(name = "resend")
    private int resend  = 0;

    @Column(name = "send_as")
    private String send_as;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "content")
    private String content;


    public Message() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMessage_type_id() {
        return message_type_id;
    }

    public void setMessage_type_id(int message_type_id) {
        this.message_type_id = message_type_id;
    }

    public String getMessage_type_name() {
        return message_type_name;
    }

    public void setMessage_type_name(String message_type_name) {
        this.message_type_name = message_type_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessage_status_id() {
        return message_status_id;
    }

    public void setMessage_status_id(int message_status_id) {
        this.message_status_id = message_status_id;
    }

    public String getMessage_status_name() {
        return message_status_name;
    }

    public void setMessage_status_name(String message_status_name) {
        this.message_status_name = message_status_name;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver_cc() {
        return receiver_cc;
    }

    public void setReceiver_cc(String receiver_cc) {
        this.receiver_cc = receiver_cc;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public int getResend() {
        return resend;
    }

    public void setResend(int resend) {
        this.resend = resend;
    }

    public String getSend_as() {
        return send_as;
    }

    public void setSend_as(String send_as) {
        this.send_as = send_as;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
