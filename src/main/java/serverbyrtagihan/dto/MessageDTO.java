package serverbyrtagihan.dto;

public class MessageDTO {
    private Long message_type_id;

    private String message;

    private String receiver;

    private String subject;

    private String send_as;

    public Long getMessage_type_id() {
        return message_type_id;
    }

    public void setMessage_type_id(Long message_type_id) {
        this.message_type_id = message_type_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSend_as() {
        return send_as;
    }

    public void setSend_as(String send_as) {
        this.send_as = send_as;
    }
}
