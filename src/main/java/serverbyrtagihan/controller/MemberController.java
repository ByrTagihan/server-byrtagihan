package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.Password;
import serverbyrtagihan.impl.CustomerDetailsServiceImpl;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.dto.LoginMember;
import serverbyrtagihan.dto.MemberDTO;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.response.*;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class MemberController {
    @Autowired
    private MemberService service;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    CustomerDetailsServiceImpl detailsService;

    @PostMapping("/member/login")
    public CommonResponse<?> authenticate( @RequestBody LoginMember loginRequest) {
        Member member = memberRepository.findByUniqueId(loginRequest.getUniqueId()).orElseThrow(() -> new NotFoundException("Email not found"));
        boolean conPassword = encoder.matches(loginRequest.getPassword() , member.getPassword());
        if (conPassword) {
            String token = jwtUtils.generateTokenmember(member.getUniqueId());
            Map<Object, Object> response = new HashMap<>();
            response.put("data", "true");
            response.put("token", token);
            response.put("type-token", "Member");
            return ResponseHelper.ok(response);
        } else {
            throw new NotFoundException("Password not valid");
        }
    }

    @PostMapping("/customer/member")
    public CommonResponse<Member> registerMember(@RequestBody Member member ,HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(service.add(member , jwtToken));
    }


    @GetMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> getByID(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(service.getById(id, jwtToken));
    }

    @GetMapping(path = "/customer/member")
    public CommonResponse<List<Member>> getAll(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(service.getAll(jwtToken));
    }

    @PutMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> put(@PathVariable("id") Long id, @RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(service.put(modelMapper.map(memberDTO, Member.class), id, jwtToken));
    }
    @PutMapping(path = "/customer/member/{id}/password")
    public CommonResponse<Member> putPass(@PathVariable("id") Long id, @RequestBody Password memberDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(service.putPassword(modelMapper.map(memberDTO, Member.class), id, jwtToken));
    }

    @DeleteMapping(path = "/customer/member/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(service.delete(id, jwtToken));
    }
    @PutMapping(path = "/member/password")
    public CommonResponse<Member> putPassword(@RequestBody PasswordDTO password, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(service.putPass(password, jwtToken));
    }


}