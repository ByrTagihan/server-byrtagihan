package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.dto.BillDTO;
import serverbyrtagihan.dto.BillPaidDTO;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.BillService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CustomerMemberBillController {

    @Autowired
    BillService billService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = "/customer/member/{memberid}/bill")
    public PaginationResponse<List<Bill>> getAll(
            HttpServletRequest request, @PathVariable("memberid") Long memberId,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("Authorization").substring(7);

        Page<Bill> billPage;

        if (search != null && !search.isEmpty()) {
            billPage = billService.getByMemberId(memberId, jwtToken, page, limit, sort, search);
        } else {
            billPage = billService.getByMemberId(memberId, jwtToken, page, limit, sort, null);
        }

        List<Bill> bills = billPage.getContent();
        long totalItems = billPage.getTotalElements();
        int totalPages = billPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(bills, pagination);
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
    public CommonResponse<Bill> paid(HttpServletRequest request, @PathVariable("memberid") Long memberId, @PathVariable("id") Long id, BillPaidDTO bill) {
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
