package serverbyrtagihan.service;


import serverbyrtagihan.dto.ForGotPass;
import org.springframework.data.domain.Page;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.modal.ForGotPasswordMember;
import serverbyrtagihan.modal.Member;

import javax.mail.MessagingException;
import java.util.*;

public interface MemberService {
    Member add(Member member , String jwtToken);

    Member addInUser(Member member, String jwtToken);

    Member getById(Long id , String jwtToken);

    Member getByIdInUser(Long id, String jwtToken);

    Member putPass(PasswordDTO member, String jwtToken);


    Page<Member> getAll(String jwtToken, Long page, Long limit, String sort, String search);

    Page<Member> getAllInUser(String jwtToken, Long page, Long limit, String sort, String search);

    Member put(Member member, Long id, String jwtToken);

    Member putInUser(Member member, Long id, String jwtToken);

    Member putPassword(Member member, Long id, String jwtToken);

    Map<String, Boolean> delete(Long id , String jwtToken);

    Map<String, Boolean> deleteInUser(Long id, String jwtToken);
}