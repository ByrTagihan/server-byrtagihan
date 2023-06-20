package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.Service.BillService;
import serverbyrtagihan.dto.BillDTO;
import serverbyrtagihan.dto.BillPaidDTO;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.Modal.Bill;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BillController {

    @Autowired
    BillService billService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = "/customer/bill")
    public CommonResponse<List<Bill>> getAll() {
        return ResponseHelper.ok(billService.getAll());
    }

    @GetMapping(path = "/customer/bill/{id}")
    public CommonResponse<Bill> getById(Long id) {
        return ResponseHelper.ok(billService.getById(id));
    }

    @PostMapping(path = "/customer/bill")
    public CommonResponse<Bill> add(@RequestBody BillDTO bill) {
        return ResponseHelper.ok(billService.add(modelMapper.map(bill, Bill.class)));
    }

    @PutMapping(path = "/customer/bill/{id}")
    public CommonResponse<Bill> put(@RequestBody BillDTO bill, Long id) {
        return ResponseHelper.ok(billService.put(modelMapper.map(bill, Bill.class), id));
    }

    @PutMapping(path = "/customer/bill/{id}/paid")
    public CommonResponse<Bill> paid(@RequestBody BillPaidDTO bill, Long id) {
        return ResponseHelper.ok(billService.paid(modelMapper.map(bill, Bill.class), id));
    }

    @PutMapping(path = "/customer/bill/{id}/unpaid")
    public CommonResponse<Bill> unpaid(BillPaidDTO bill, Long id) {
        return ResponseHelper.ok(billService.unpaid(modelMapper.map(bill, Bill.class), id));
    }

    @DeleteMapping(path = "/customer/bill/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id) {
        return ResponseHelper.ok(billService.delete(id));
    }
}
