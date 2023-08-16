package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.repository.CustomerOrganizationRepository;
import serverbyrtagihan.repository.CustomerRepository;
import serverbyrtagihan.dto.*;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.dto.PictureDTO;
import serverbyrtagihan.dto.ProfileDTO;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.response.*;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.impl.CustomerDetailsImpl;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.modal.ForGotPassword;
import serverbyrtagihan.service.CustomerService;
import serverbyrtagihan.util.Pagination;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class CustomerController {
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

    @GetMapping(path = "/customer/profile")
    public CommonResponse<Customer> get(HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(customerService.getProfileCustomer(jwtToken));
    }

    @PutMapping(path = "/customer/picture")
    public CommonResponse<Customer> putPicture(HttpServletRequest request, @RequestBody PictureDTO profile) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(customerService.putPicture(modelMapper.map(profile, Customer.class), jwtToken));
    }

    @PutMapping(path = "/customer/profile")
    public CommonResponse<Customer> put(@RequestBody ProfileDTO profile, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(customerService.put(modelMapper.map(profile, Customer.class), jwtToken));
    }

    @PutMapping(path = "/customer/password")
    public CommonResponse<Customer> putPassword(@RequestBody PasswordDTO password, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(customerService.putPassword(password, jwtToken));
    }


    @PostMapping(path = "/customer/verification_code")
    public CommonResponse<ForGotPassword> verificationCode(@RequestBody Verification verification) throws MessagingException {
        return ResponseHelper.ok(customerService.verificationPass(modelMapper.map(verification, ForGotPassword.class)));
    }


    @PostMapping("/customer/login")
    public CommonResponse<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseHelper.ok(customerService.login(loginRequest));
    }

    @PostMapping("/customer/forgot_password")
    public CommonResponse<ForGotPass> sendEmail(@RequestBody ForGotPass forGotPass) throws MessagingException {
        return ResponseHelper.ok(customerService.sendEmail(forGotPass));

    }

}
