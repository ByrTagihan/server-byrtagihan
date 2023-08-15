package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.*;
import serverbyrtagihan.impl.MemberDetailsImpl;
import serverbyrtagihan.impl.UserDetailsImpl;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.impl.CustomerDetailsServiceImpl;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.response.*;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.MemberService;
import serverbyrtagihan.util.Pagination;

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


    private static final String JWT_PREFIX = "jwt ";

    @PostMapping("/member/login")
    public CommonResponse<?> authenticate(@RequestBody LoginMember loginRequest) {
            return ResponseHelper.ok(service.login(loginRequest));

    }

    @GetMapping(path = "/member/profile")
    public CommonResponse<Member> getById(HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.getProfile(jwtToken));
    }

    @PutMapping(path = "/member/profile")
    public CommonResponse<Member> put(HttpServletRequest request, @RequestBody MemberDTO memberDTO) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.putProfile(modelMapper.map(memberDTO, Member.class), jwtToken));
    }

    @PutMapping(path = "/member/password")
    public CommonResponse<Member> putPassword(@RequestBody PasswordDTO password, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.putPass(password, jwtToken));
    }


}