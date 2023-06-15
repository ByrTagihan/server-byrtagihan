package serverbyrtagihan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Impl.MemberDetails;
import serverbyrtagihan.Jwt.JwtUtils;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.MemberLogin;
import serverbyrtagihan.Modal.MemberPrinciple;
import serverbyrtagihan.Repository.MemberLoginRepository;
import serverbyrtagihan.Service.MemberLoginService;
import serverbyrtagihan.dto.*;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberLoginController {

    @Autowired
    MemberLoginService memberLoginService;


    @PostMapping("/member/add")
    public CommonResponse<MemberLogin> add (@RequestBody MemberLoginDto memberLoginDto) {
        return ResponseHelper.ok(memberLoginService.addMember(memberLoginDto));
    }

    @PostMapping("/member/login")
    public CommonResponse<Map<String, Object>> login(@RequestBody MemberLoginDto login) {
        return ResponseHelper.ok(memberLoginService.loginMember(login));
    }

    @GetMapping("/member/{id}")
    public CommonResponse<MemberLogin> getById(@PathVariable("id") Long id) {
        return ResponseHelper.ok(memberLoginService.getById(id));
    }

    @PutMapping(path = "/member/password")
    public CommonResponse<MemberLogin> putPassword(@RequestBody PasswordMemberDto password, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(memberLoginService.putPass(password, jwtToken));
    }

    @PutMapping(path = "/member/update/{id}" , consumes = "multipart/form-data")
    public CommonResponse<MemberLogin> update(@PathVariable("id") Long id, UpdateMemberDto update, @RequestPart("file")MultipartFile multipartFile) {
        return ResponseHelper.ok(memberLoginService.put(update, multipartFile , id));
    }
    @GetMapping("/member/GetAll")
    public CommonResponse<List<MemberLogin>> getAll() {
        return ResponseHelper.ok(memberLoginService.getAll());
    }
}
