package serverbyrtagihan.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.PaymentDto;
import serverbyrtagihan.modal.Payment;
import serverbyrtagihan.modal.Transaction;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.PaymentService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping(path = "/user/payment")
    public CommonResponse<Payment> post(@RequestBody PaymentDto paymentDto, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(paymentService.Add(paymentDto, jwtToken));
    }
    @PutMapping(path = "/user/payment/{id}")
    public CommonResponse<Payment> Put(@RequestBody PaymentDto paymentDto, @PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(paymentService.Put(id,paymentDto, jwtToken));
    }

    @GetMapping(path = "/user/payment/{id}")
    public CommonResponse<Payment> Preview(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(paymentService.Preview(id, jwtToken));
    }
    @GetMapping(path = "/user/payment")
    public PaginationResponse<List<Payment>> getAll(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("Authorization").substring(7);

        Page<Payment> paymentPage;

        if (search != null && !search.isEmpty()) {
            paymentPage = paymentService.getAll(jwtToken, page, limit, sort, search);
        } else {
            paymentPage = paymentService.getAll(jwtToken, page, limit, sort, null);
        }

        List<Payment> payments = paymentPage.getContent();
        long totalItems = paymentPage.getTotalElements();
        int totalPages = paymentPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(payments, pagination);
    }
    @DeleteMapping(path = "/user/payment/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id , HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(paymentService.Delete(id ,jwtToken));
    }
}
