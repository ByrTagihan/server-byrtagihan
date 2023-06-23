package serverbyrtagihan.service;

import serverbyrtagihan.modal.CustomerOrganizationModel;
import serverbyrtagihan.modal.Member;

import java.util.*;

public interface MemberService {
    Member add(Member member);

    Member getById(UUID id);

    List<Member> getAll();

    Member put(Member member, UUID id);

    Map<String, Boolean> delete(UUID id);
}
