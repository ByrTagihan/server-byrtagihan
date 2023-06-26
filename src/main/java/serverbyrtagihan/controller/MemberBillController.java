package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.modal.Bill;
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

    @GetMapping(path = "/member/bill")
    public PaginationResponse<List<Bill>> getAll(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(value = "limit", defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("Authorization").substring(7);
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
}
