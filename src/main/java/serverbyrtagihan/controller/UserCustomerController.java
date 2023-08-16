package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.*;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.modal.ForGotPassword;
import serverbyrtagihan.repository.CustomerOrganizationRepository;
import serverbyrtagihan.repository.CustomerRepository;
import serverbyrtagihan.response.*;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.CustomerService;
import serverbyrtagihan.util.Pagination;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class UserCustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerRepository adminRepository;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    CustomerOrganizationRepository organizationRepository;
    @Autowired
    CustomerRepository customerRepository;
    private static final String JWT_PREFIX = "jwt ";


    @GetMapping(path = "/user/customer")
    public PaginationResponse<List<Customer>> getAll(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        Page<Customer> customerPage;

        if (search != null && !search.isEmpty()) {
            customerPage = customerService.getAll(jwtToken, page, limit, sort, search);
        } else {
            customerPage = customerService.getAll(jwtToken, page, limit, sort, null);
        }

        List<Customer> customers = customerPage.getContent();
        long totalItems = customerPage.getTotalElements();
        int totalPages = customerPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(customers, pagination);
    }

    @PostMapping("/user/customer")
    public CommonResponse<Customer> registerUser(@RequestBody SignupRequest customer, HttpServletRequest request) throws MessagingException {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(customerService.post(customer, jwtToken));
    }

    @GetMapping("/user/customer/{id}")
    public CommonResponse<Customer> Preview(@PathVariable("id") Long id) {
        return ResponseHelper.ok(customerService.getById(id));
    }

    @PutMapping(path = "/user/customer/{id}")
    public CommonResponse<Customer> put(@RequestBody PutCustomer putCustomer, @PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(customerService.put2(modelMapper.map(putCustomer, Customer.class), jwtToken, id));
    }

    @DeleteMapping(path = "/user/customer/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(customerService.delete(id, jwtToken));
    }

}
