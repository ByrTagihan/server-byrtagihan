package serverbyrtagihan.Service;

import serverbyrtagihan.Modal.CustomerOrganizationModel;
import serverbyrtagihan.Modal.Member;

import java.util.*;

public interface MemberService {
    Member add(Member member);

    Member getById(UUID id);

    List<Member> getAll();

    Member put(Member member, UUID id);

    Map<String, Boolean> delete(UUID id);
}