package serverbyrtagihan.Service;


import serverbyrtagihan.swagger.Modal.Member;

import java.util.*;

public interface MemberService {
    Member add(Member member);

    Member getById(Long id);

    List<Member> getAll();

    Member put(Member member, Long id);

    Map<String, Boolean> delete(Long id);
}
