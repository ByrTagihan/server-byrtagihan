package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class CustomerBillController {

    @Autowired
    BillService billService;

    @Autowired
    private ModelMapper modelMapper;

    public static final String DEFAULT_PAGE_NUMBER = "1";
    public  static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";


    @GetMapping(path = "/customer/bill")
    public CommonResponse<Page<Bill>> getAll(HttpServletRequest request, @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER, required = false) Long page, @RequestParam(value = "limit", defaultValue = DEFAULT_PAGE_SIZE, required = false) Long pageSize) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.getAll(jwtToken, page, pageSize));
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
    public CommonResponse<Bill> unpaid(HttpServletRequest request, BillPaidDTO bill, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.unpaid(modelMapper.map(bill, Bill.class), id, jwtToken));
    }

    @DeleteMapping(path = "/customer/bill/{id}")
    public CommonResponse<?> delete(HttpServletRequest request, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.delete(id, jwtToken));
    }
}
