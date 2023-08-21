package serverbyrtagihan.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String unique_id) {
        super("Member not found with unique_id: " + unique_id);
    }
}

