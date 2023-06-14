package serverbyrtagihan.Service;

import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Modal.MemberLogin;
import serverbyrtagihan.dto.Login;
import serverbyrtagihan.dto.MemberLoginDto;
import serverbyrtagihan.dto.UpdateMemberDto;

import java.util.List;
import java.util.Map;

public interface MemberLoginService {

    MemberLogin addMember(MemberLoginDto memberLoginDto);

    Map<String, Object> loginMember (MemberLoginDto login);

    MemberLogin getById(Long id);

    List<MemberLogin> getAll();

    MemberLogin put(UpdateMemberDto memberLogin, MultipartFile multipartFile, Long id);

}
