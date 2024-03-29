package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.*;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.response.SignupRequest;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.MemberService;
import serverbyrtagihan.util.Pagination;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class CustomerMemberController {
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

    @PostMapping("/customer/member")
    public CommonResponse<Member> registerMember(@RequestBody MemberDTO member, HttpServletRequest request) throws MessagingException{
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.add(member, jwtToken));
    }

    @GetMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> getByID(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.getById(id, jwtToken));
    }

    @PutMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> put(@PathVariable("id") Long id, @RequestBody MemberUpdateDTO memberDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.put(modelMapper.map(memberDTO, Member.class), id, jwtToken));
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

}