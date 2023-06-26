package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(path = "/customer/profile")
    public CommonResponse<Customer> get(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerService.getProfileCustomer(jwtToken));
    }

    @PutMapping(path = "/customer/picture")
    public CommonResponse<Customer> putPicture(HttpServletRequest request, @RequestBody PictureDTO profile) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerService.putPicture(modelMapper.map(profile, Customer.class), jwtToken));
    }

    @PutMapping(path = "/customer/profile")
    public CommonResponse<Customer> put(@RequestBody ProfileDTO profile, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerService.put(modelMapper.map(profile, Customer.class), jwtToken));
    }

    @PutMapping(path = "/customer/password")
    public CommonResponse<Customer> putPassword(@RequestBody PasswordDTO password, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerService.putPassword(password, jwtToken));
    }


    @PostMapping(path = "/customer/verification_code")
    public CommonResponse<ForGotPassword> verificationCode(@RequestBody Verification verification) throws MessagingException {
        return ResponseHelper.ok(customerService.verificationPass(modelMapper.map(verification, ForGotPassword.class)));
    }

    @DeleteMapping(path = "/user/customer/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerService.delete(id, jwtToken));
    }


    @PostMapping("/customer/login")
    public CommonResponse<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        CustomerDetailsImpl userDetails = (CustomerDetailsImpl) authentication.getPrincipal();
        Map<Object, Object> response = new HashMap<>();
        response.put("data", "true");
        response.put("token", jwt);
        response.put("type-token", "Member");
        return ResponseHelper.ok(response);
    }

    @PostMapping("/user/customer")
    public CommonResponse<Customer> registerUser( @RequestBody SignupRequest signUpRequest , HttpServletRequest request) throws MessagingException {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerService.post(modelMapper.map(signUpRequest , Customer.class) , jwtToken));
    }

    @PostMapping("/customer/forgot_password")
    public CommonResponse<ForGotPass> sendEmail(@RequestBody ForGotPass forGotPass) throws MessagingException {
        return ResponseHelper.ok(customerService.sendEmail(forGotPass));

    }

    @GetMapping("/user/customer")
    public CommonResponse<List<Customer>> Get() {
        return ResponseHelper.ok(customerService.getAll());
    }

    @GetMapping("/user/customer/{id}")
    public CommonResponse<Customer> Preview(@PathVariable("id") Long id) {
        return ResponseHelper.ok(customerService.getById(id));
    }

    @PutMapping(path = "/user/customer/{id}")
    public CommonResponse<Customer> put(@RequestBody PutCustomer putCustomer, @PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerService.put2(modelMapper.map(putCustomer, Customer.class), jwtToken, id));
    }

}
