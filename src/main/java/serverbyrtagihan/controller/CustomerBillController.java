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
public class CustomerBillController {

    @Autowired
    BillService billService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = "/customer/bill")
    public CommonResponse<List<Bill>> getAll(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(billService.getAll(jwtToken));
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
