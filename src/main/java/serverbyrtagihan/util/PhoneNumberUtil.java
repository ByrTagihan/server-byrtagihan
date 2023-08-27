package serverbyrtagihan.util;

public class PhoneNumberUtil {
    public static String extractLastEightDigits(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() >= 8) {
            return phoneNumber.substring(phoneNumber.length() - 8);
        }
        return phoneNumber;
    }
}
