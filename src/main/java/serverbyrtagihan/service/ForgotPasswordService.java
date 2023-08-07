package serverbyrtagihan.service;

import serverbyrtagihan.modal.Member;

public interface ForgotPasswordService {
    Member findByHp(String hp);
}
