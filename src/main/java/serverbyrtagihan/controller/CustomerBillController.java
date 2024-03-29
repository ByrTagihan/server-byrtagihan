package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.BillDTO;
import serverbyrtagihan.dto.BillPaidDTO;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.BillService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CustomerBillController {

    @Autowired
    BillService billService;

    @Autowired
    private ModelMapper modelMapper;

    private static final String JWT_PREFIX = "jwt ";

    @GetMapping(path = "/customer/bill")
    public PaginationResponse<List<Bill>> getAll(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        Page<Bill> billPage;

        if (search != null && !search.isEmpty()) {
            billPage = billService.getAll(jwtToken, page, limit, sort, search);
        } else {
            billPage = billService.getAll(jwtToken, page, limit, sort, null);
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

    @GetMapping(path = "/customer/bill/{id}")
    public CommonResponse<Bill> getById(HttpServletRequest request, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.getById(id, jwtToken));
    }

    @PostMapping(path = "/customer/bill")
    public CommonResponse<Bill> add(HttpServletRequest request, @RequestBody BillDTO bill) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.add(modelMapper.map(bill, Bill.class), jwtToken));
    }

    @PutMapping(path = "/customer/bill/{id}")
    public CommonResponse<Bill> put(HttpServletRequest request, @RequestBody BillDTO bill, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.put(modelMapper.map(bill, Bill.class), id, jwtToken));
    }

    @PutMapping(path = "/customer/bill/{id}/paid")
    public CommonResponse<Bill> paid(HttpServletRequest request, BillPaidDTO bill, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.paid(modelMapper.map(bill, Bill.class), id, jwtToken));
    }

    @PutMapping(path = "/customer/bill/{id}/unpaid")
    public CommonResponse<Bill> unpaid(HttpServletRequest request, BillPaidDTO bill, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.unpaid(modelMapper.map(bill, Bill.class), id, jwtToken));
    }

    @DeleteMapping(path = "/customer/bill/{id}")
    public CommonResponse<?> delete(HttpServletRequest request, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.delete(id, jwtToken));
    }

    @GetMapping("/customer/report/recap/bill")
    public CommonResponse<?> getReportRecapBillCustomer(HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.getReportRecapBillCustomer(jwtToken));
    }

    @GetMapping("/member/report/recap/bill")
    public CommonResponse<?> getReportRecapBillMember(HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.getReportRecapBillMember(jwtToken));
    }
    @GetMapping("/user/report/recap/bill")
    public CommonResponse<?> getReportRecapBillUser(HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(billService.getReportRecapBillUser(jwtToken));
    }
}
