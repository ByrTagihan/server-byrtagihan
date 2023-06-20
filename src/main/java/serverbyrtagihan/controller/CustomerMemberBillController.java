package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.Modal.Bill;
import serverbyrtagihan.Service.BillService;
import serverbyrtagihan.dto.BillDTO;
import serverbyrtagihan.dto.BillPaidDTO;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerMemberBillController {

    @Autowired
    BillService billService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = "/customer/member/{memberid}/bill")
    public CommonResponse<List<Bill>> getAll(HttpServletRequest request, @PathVariable("memberid") Long memberId) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.getByMemberId(memberId, jwtToken));
    }

    @GetMapping(path = "/customer/member/{memberid}/bill/{id}")
    public CommonResponse<Bill> getById(HttpServletRequest request, @PathVariable("memberid") Long memberId, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.getByIdInMember(memberId, id, jwtToken));
    }

    @PostMapping(path = "/customer/member/{memberid}/bill")
    public CommonResponse<Bill> add(HttpServletRequest request, @PathVariable("memberid") Long memberId, @RequestBody BillDTO bill) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.addByMemberId(modelMapper.map(bill, Bill.class), memberId, jwtToken));
    }

    @PutMapping(path = "/customer/member/{memberid}/bill/{id}")
    public CommonResponse<Bill> put(HttpServletRequest request, @PathVariable("memberid") Long memberId, @PathVariable("id") Long id, @RequestBody BillDTO bill) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.putByIdInMember(modelMapper.map(bill, Bill.class), memberId, id, jwtToken));
    }

    @PutMapping(path = "/customer/member/{memberid}/bill/{id}/paid")
    public CommonResponse<Bill> paid(HttpServletRequest request, @PathVariable("memberid") Long memberId, @PathVariable("id") Long id, @RequestBody BillPaidDTO bill) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.paidByIdInMember(modelMapper.map(bill, Bill.class), memberId, id, jwtToken));
    }

    @PutMapping(path = "/customer/member/{memberid}/bill/{id}/unpaid")
    public CommonResponse<Bill> unpaid(HttpServletRequest request, @PathVariable("memberid") Long memberId, @PathVariable("id") Long id, BillPaidDTO bill) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.unpaidByIdInMember(modelMapper.map(bill, Bill.class), memberId, id, jwtToken));
    }

    @DeleteMapping(path = "/customer/member/{memberid}/bill/{id}")
    public CommonResponse<?> delete(HttpServletRequest request, @PathVariable("memberid") Long memberId, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.deleteByIdInMember(memberId, id, jwtToken));
    }
}
