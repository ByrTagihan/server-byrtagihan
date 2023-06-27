package serverbyrtagihan.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.PaymentDto;
import serverbyrtagihan.modal.Payment;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.PaymentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public CommonResponse<List<Payment>> Get(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(paymentService.Get(jwtToken));
    }
    @DeleteMapping(path = "/user/payment/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id , HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(paymentService.Delete(id ,jwtToken));
    }
}
