package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    public static final String DEFAULT_PAGE_NUMBER = "1";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";


    @GetMapping(path = "/customer/bill")
    public PaginationResponse<List<Bill>> getAll(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(value = "limit", defaultValue = Pagination.size, required = false) Long pageSize,
            @RequestParam(defaultValue = Pagination.sortBy, required = false) String sortBy,
            @RequestParam(defaultValue = Pagination.sortDir) String sortDirection,
            @RequestParam(required = false) String search
     ) {
        String jwtToken = request.getHeader("Authorization").substring(7);

        Page<Bill> billPage;

        if (search != null && !search.isEmpty()) {
            billPage = billService.searchBillsWithPagination(jwtToken, search, page, pageSize, sortBy, sortDirection);
        } else {
            billPage = billService.getAll(jwtToken, page, pageSize, sortBy, sortDirection);
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
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.getById(id, jwtToken));
    }

    @PostMapping(path = "/customer/bill")
    public CommonResponse<Bill> add(HttpServletRequest request, @RequestBody BillDTO bill) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.add(modelMapper.map(bill, Bill.class), jwtToken));
    }

    @PutMapping(path = "/customer/bill/{id}")
    public CommonResponse<Bill> put(HttpServletRequest request, @RequestBody BillDTO bill, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.put(modelMapper.map(bill, Bill.class), id, jwtToken));
    }

    @PutMapping(path = "/customer/bill/{id}/paid")
    public CommonResponse<Bill> paid(HttpServletRequest request, @RequestBody BillPaidDTO bill, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.paid(modelMapper.map(bill, Bill.class), id, jwtToken));
    }

    @PutMapping(path = "/customer/bill/{id}/unpaid")
    public CommonResponse<Bill> unpaid(HttpServletRequest request, @RequestBody BillPaidDTO bill, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.unpaid(modelMapper.map(bill, Bill.class), id, jwtToken));
    }

    @DeleteMapping(path = "/customer/bill/{id}")
    public CommonResponse<?> delete(HttpServletRequest request, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.delete(id, jwtToken));
    }
}
