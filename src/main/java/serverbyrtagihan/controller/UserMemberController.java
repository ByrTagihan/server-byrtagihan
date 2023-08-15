package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.*;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.MemberService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class UserMemberController {
    @Autowired
    private MemberService service;

    @Autowired
    private ModelMapper modelMapper;


    private static final String JWT_PREFIX = "jwt ";

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

    @PostMapping("/user/member")
    public CommonResponse<Member> registerMemberInUser(@RequestBody Member member, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.addInUser(member, jwtToken));
    }

    @GetMapping(path = "/user/member/{id}")
    public CommonResponse<Member> getByIDInUser(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.getByIdInUser(id, jwtToken));
    }

    @PutMapping(path = "/user/member/{id}")
    public CommonResponse<Member> putInUser(@PathVariable("id") Long id, @RequestBody MemberUserDto memberDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.putInUser(modelMapper.map(memberDTO, Member.class), id, jwtToken));
    }

    @DeleteMapping(path = "/user/member/{id}")
    public CommonResponse<?> deleteInUser(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.deleteInUser(id, jwtToken));
    }

}