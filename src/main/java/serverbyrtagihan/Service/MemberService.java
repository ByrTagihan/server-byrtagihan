package serverbyrtagihan.service;


import serverbyrtagihan.Modal.Member;
import serverbyrtagihan.dto.PasswordDTO;

import java.util.*;

public interface MemberService {
    Member add(Member member , String jwtToken);

    Member getById(Long id , String jwtToken);

    Member putPass(PasswordDTO member, String jwtToken);

    List<Member> getAll(String jwtToken);

    Member put(Member member, Long id, String jwtToken);

    Map<String, Boolean> delete(Long id , String jwtToken);
}
