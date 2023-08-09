package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.BNIRequestDTO;
import serverbyrtagihan.dto.BillDTO;
import serverbyrtagihan.dto.BillMemberDTO;
import serverbyrtagihan.dto.MemberPaymentDTO;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Payment;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.service.BillService;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class MemberBillController {
    @Autowired
    BillService billService;

    @Autowired
    private ModelMapper modelMapper;


    private static final String JWT_PREFIX = "jwt ";

    @GetMapping(path = "/member/bill")
    public PaginationResponse<List<Bill>> getAll(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(value = "limit", defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        Page<Bill> billPage;

        if (search != null && !search.isEmpty()) {
            billPage = billService.getBillsByMemberId(jwtToken, page, limit, sort, search);
        } else {
            billPage = billService.getBillsByMemberId(jwtToken, page, limit, sort, null);
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

    @PostMapping(path = "/member/bill/{bill_id}/payment")
    public CommonResponse<BNIRequestDTO> paymentById(HttpServletRequest request, @PathVariable("bill_id") Long bill_id, @RequestBody MemberPaymentDTO payment) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.paymentById(modelMapper.map(payment, Payment.class), bill_id, jwtToken));
    }

    @PostMapping(path = "/member/bill/all/{bill_id}/payment")
    public CommonResponse<BNIRequestDTO> paymentByAll(HttpServletRequest request, @PathVariable("bill_id") Long bill_id, @RequestBody MemberPaymentDTO payment) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.paymentById(modelMapper.map(payment, Payment.class), bill_id, jwtToken));
    }
}
