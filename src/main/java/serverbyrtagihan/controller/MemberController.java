package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.Service.MemberService;
import serverbyrtagihan.dto.MemberDTO;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.swagger.Modal.Member;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class MemberController {
    @Autowired
    private MemberService member;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "/customer/member")
    public CommonResponse<Member> add(@RequestBody MemberDTO memberDTO) {
        return ResponseHelper.ok(member.add(modelMapper.map(memberDTO, Member.class)));
    }

    @GetMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> getByID(@PathVariable("id") UUID id) {
        return ResponseHelper.ok(member.getById(id));
    }

    @GetMapping(path ="/customer/member")
    public CommonResponse<List<Member>> getAll() {
        return ResponseHelper.ok(member.getAll());
    }

    @PutMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> put(  @PathVariable("id") UUID id ,@RequestBody MemberDTO memberDTO){
        return ResponseHelper.ok(member.put(modelMapper.map(memberDTO, Member.class), id));
    }
    @DeleteMapping(path = "/customer/member/{id}")
    public CommonResponse<?> delete(@PathVariable("id") UUID id) {
        return ResponseHelper.ok(member.delete(id));
    }


}
