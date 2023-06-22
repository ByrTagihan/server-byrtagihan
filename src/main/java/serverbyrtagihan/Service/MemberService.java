package serverbyrtagihan.service;


import serverbyrtagihan.Modal.Member;

import java.util.*;

public interface MemberService {
    Member add(Member member , String jwtToken);

    Member getById(Long id , String jwtToken);

    List<Member> getAll(String jwtToken);

    Member put(Member member, Long id, String jwtToken);

    Map<String, Boolean> delete(Long id , String jwtToken);
}
