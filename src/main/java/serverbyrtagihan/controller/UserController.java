package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Impl.CustomerDetailsImpl;
import serverbyrtagihan.Modal.Customer;
import serverbyrtagihan.Modal.User;
import serverbyrtagihan.Modal.UserPrinciple;
import serverbyrtagihan.Repository.CustomerRepository;
import serverbyrtagihan.Service.UserService;
import serverbyrtagihan.dto.Login;
import serverbyrtagihan.dto.Password;
import serverbyrtagihan.dto.ProfileDTO;
import serverbyrtagihan.dto.Update;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.EmailRequest;
import serverbyrtagihan.response.JwtResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.security.jwt.JwtUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping(path = "/customer/add")
    public CommonResponse<User> register (@RequestBody User user) {
        return ResponseHelper.ok(userService.register(user));
    }

    @PostMapping(path = "/user/login")
    public ResponseEntity<?> authenticate ( Login login) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername()
        ));
    }

    @GetMapping(path = "/user/profile")
    public CommonResponse<User> getById(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(userService.getProfileUser(jwtToken));
    }

    @PutMapping(path = "/api/user/update{id}" , consumes = "multipart/form-data")
    public CommonResponse<User> update(@PathVariable("id") Long id, @RequestBody ProfileDTO update, @RequestPart("file") MultipartFile multipartFile, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(userService.update(id, update, multipartFile , jwtToken));
    }

    @PutMapping(path = "/user/password{id}")
    public CommonResponse<User> updatePassword(@PathVariable("id") Long id, @RequestBody Password password) {
        return ResponseHelper.ok(userService.updatePassword(id, modelMapper.map(password , User.class)));
    }

    @GetMapping("/user/profile/all")
    public CommonResponse<List<User>> getAll() {
        return ResponseHelper.ok(userService.getAllTagihan());
    }

    @PostMapping("/user/forgot_password")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            userService.sendEmail(emailRequest.getEmail());
            return "Email sent successfully";
        } catch (MessagingException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
