package serverbyrtagihan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.MemberLogin;
import serverbyrtagihan.Service.MemberLoginService;
import serverbyrtagihan.dto.*;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberLoginController {

    @Autowired
    MemberLoginService memberLoginService;

    @PostMapping("/api/member/add")
    public CommonResponse<MemberLogin> add (@RequestBody MemberLoginDto memberLoginDto) {
        return ResponseHelper.ok(memberLoginService.addMember(memberLoginDto));
    }

    @PostMapping("/api/member/login")
    public CommonResponse<Map<String, Object>> login(@RequestBody MemberLoginDto login) {
        return ResponseHelper.ok(memberLoginService.loginMember(login));
    }

    @GetMapping("/api/member/{id}")
    public CommonResponse<MemberLogin> getById(@PathVariable("id") Long id) {
        return ResponseHelper.ok(memberLoginService.getById(id));
    }

    @PutMapping(path = "/member/password")
    public CommonResponse<MemberLogin> putPassword(@RequestBody PasswordMemberDto password, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(memberLoginService.putPass(password, jwtToken));
    }

    @PutMapping(path = "/api/member/update/{id}" , consumes = "multipart/form-data")
    public CommonResponse<MemberLogin> update(@PathVariable("id") Long id, UpdateMemberDto update, @RequestPart("file")MultipartFile multipartFile) {
        return ResponseHelper.ok(memberLoginService.put(update, multipartFile , id));
    }
    @GetMapping("/api/member/GetAll")
    public CommonResponse<List<MemberLogin>> getAll() {
        return ResponseHelper.ok(memberLoginService.getAll());
    }
}
