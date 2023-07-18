package serverbyrtagihan.service;


import org.springframework.data.domain.Page;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.modal.Member;

import java.util.*;

public interface MemberService {
    Member add(Member member , String jwtToken);

    Member getById(Long id , String jwtToken);

    Member putPass(PasswordDTO member, String jwtToken);


    Page<Member> getAll(String jwtToken, Long page, Long limit, String sort, String search);

    Member put(Member member, Long id, String jwtToken);

    Member putPassword(Member member, Long id, String jwtToken);

    Map<String, Boolean> delete(Long id , String jwtToken);
}