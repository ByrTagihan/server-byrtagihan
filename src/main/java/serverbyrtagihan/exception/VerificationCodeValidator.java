package serverbyrtagihan.exception;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import serverbyrtagihan.Modal.ForGotPassword;

@Component
public class VerificationCodeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ForGotPassword.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ForGotPassword verificationCode = (ForGotPassword) target;
       if (!verificationCode.getCode().equals(verificationCode.getCode())) {
            errors.rejectValue("code", "verificationCode.mismatch", "Kode verifikasi tidak sesuai");
        }
    }
}
