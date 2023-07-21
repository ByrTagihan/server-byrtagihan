package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.dto.Password;
import serverbyrtagihan.impl.CustomerDetailsServiceImpl;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.dto.LoginMember;
import serverbyrtagihan.dto.MemberDTO;
import serverbyrtagihan.dto.PasswordDTO;
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
    public CommonResponse<?> authenticate( @RequestBody LoginMember loginRequest) {
        Member member = memberRepository.findByUniqueId(loginRequest.getUniqueId()).orElseThrow(() -> new NotFoundException("UniqueId not found"));
        boolean conPassword = encoder.matches(loginRequest.getPassword() , member.getPassword());
        if (conPassword) {
            String token = jwtUtils.generateTokenMember(member.getUniqueId());
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
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.add(member , jwtToken));
    }
    @PostMapping("/user/member")
    public CommonResponse<Member> registerMemberInUser(@RequestBody Member member ,HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.addInUser(member , jwtToken));
    }


    @GetMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> getByID(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.getById(id, jwtToken));
    }

    @GetMapping(path = "/user/member/{id}")
    public CommonResponse<Member> getByIDInUser(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.getByIdInUser(id, jwtToken));
    }

    @GetMapping(path = "/customer/member")
    public PaginationResponse<List<Member>> getAll(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        Page<Member> channelPage;

        if (search != null && !search.isEmpty()) {
            channelPage = service.getAll(jwtToken, page, limit, sort, search);
        } else {
            channelPage = service.getAll(jwtToken, page, limit, sort, null);
        }

        List<Member> channels = channelPage.getContent();
        long totalItems = channelPage.getTotalElements();
        int totalPages = channelPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(channels, pagination);
    }
    @GetMapping(path = "/user/member")
    public PaginationResponse<List<Member>> getAllInUser(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        Page<Member> channelPage;

        if (search != null && !search.isEmpty()) {
            channelPage = service.getAllInUser(jwtToken, page, limit, sort, search);
        } else {
            channelPage = service.getAllInUser(jwtToken, page, limit, sort, null);
        }

        List<Member> channels = channelPage.getContent();
        long totalItems = channelPage.getTotalElements();
        int totalPages = channelPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(channels, pagination);
    }

    @PutMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> put(@PathVariable("id") Long id, @RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.put(modelMapper.map(memberDTO, Member.class), id, jwtToken));
    }
    @PutMapping(path = "/user/member/{id}")
    public CommonResponse<Member> putInUser(@PathVariable("id") Long id, @RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.putInUser(modelMapper.map(memberDTO, Member.class), id, jwtToken));
    }
    @PutMapping(path = "/customer/member/{id}/password")
    public CommonResponse<Member> putPass(@PathVariable("id") Long id, @RequestBody Password memberDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.putPassword(modelMapper.map(memberDTO, Member.class), id, jwtToken));
    }

    @DeleteMapping(path = "/customer/member/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.delete(id, jwtToken));
    }
    @DeleteMapping(path = "/user/member/{id}")
    public CommonResponse<?> deleteInUser(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.deleteInUser(id, jwtToken));
    }
    @PutMapping(path = "/member/password")
    public CommonResponse<Member> putPassword(@RequestBody PasswordDTO password, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.putPass(password, jwtToken));
    }


}