package serverbyrtagihan.exception;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import serverbyrtagihan.modal.Reset_Password;

@Component
public class VerificationCodeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Reset_Password.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reset_Password verificationCode = (Reset_Password) target;
       if (!verificationCode.getCode().equals(verificationCode.getCode())) {
            errors.rejectValue("code", "verificationCode.mismatch", "Kode verifikasi tidak sesuai");
        }
    }
}
