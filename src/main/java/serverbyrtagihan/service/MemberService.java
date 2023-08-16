package serverbyrtagihan.service;


import org.springframework.data.domain.Page;
import serverbyrtagihan.dto.LoginMember;
import serverbyrtagihan.dto.MemberDTO;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.response.SignupRequest;

import javax.mail.MessagingException;
import java.util.*;

public interface MemberService {
    Member add(MemberDTO memberDTO, String jwtToken) throws MessagingException;

    Member addInUser(MemberDTO member, String jwtToken);

    Member getById(Long id , String jwtToken);

    Member getByIdInUser(Long id, String jwtToken);

    Map<Object, Object> login(LoginMember loginRequest);

    Member putPass(PasswordDTO member, String jwtToken);


    Page<Member> getAll(String jwtToken, Long page, Long limit, String sort, String search);

    Page<Member> getAllInUser(String jwtToken, Long page, Long limit, String sort, String search);

    Member put(Member member, Long id, String jwtToken);

    Member putInUser(Member member, Long id, String jwtToken);

    Member putPassword(Member member, Long id, String jwtToken);

    Map<String, Boolean> delete(Long id , String jwtToken);

    Map<String, Boolean> deleteInUser(Long id, String jwtToken);

    Member getProfile(String jwtToken);

    Member putProfile(Member member, String jwtToken);
}