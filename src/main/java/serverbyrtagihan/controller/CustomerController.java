package serverbyrtagihan.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.dto.PictureDTO;
import serverbyrtagihan.model.Customer;
import serverbyrtagihan.dto.ProfileDTO;
import serverbyrtagihan.repository.CustomerRepository;
import serverbyrtagihan.response.*;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.CustomerDetailsImpl;
import serverbyrtagihan.service.ProfileService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class CustomerController {
    @Autowired
    private ProfileService profileService;
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


    @GetMapping(path = "/customer/profile")
    public CommonResponse<Customer> getByID(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(profileService.getProfileCustomer(jwtToken));
    }

    @PutMapping(path = "/customer/picture", consumes = "multipart/form-data")
    public CommonResponse<Customer> putPicture(HttpServletRequest request, PictureDTO profile, @RequestPart("file") MultipartFile multipartFile) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(profileService.putPicture(modelMapper.map(profile, Customer.class), multipartFile, jwtToken));
    }
    @PutMapping(path = "/customer/profile")
    public CommonResponse<Customer> put(@RequestBody ProfileDTO profile , HttpServletRequest request){
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(profileService.put(modelMapper.map(profile, Customer.class) , jwtToken));
    }
    @PutMapping(path = "/customer/password")
    public CommonResponse<Customer> putPassword(@RequestBody PasswordDTO password , HttpServletRequest request){
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(profileService.putPassword(modelMapper.map(password, Customer.class) , jwtToken));
    }
    @DeleteMapping(path = "/customer/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id) {
        return ResponseHelper.ok(profileService.delete(id));
    }


    @PostMapping("/customer/login")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomerDetailsImpl userDetails = (CustomerDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername()
        ));
    }
    @PostMapping("/customer/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws FirebaseAuthException {
        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(signUpRequest.getEmail())
                .setPassword(encoder.encode(signUpRequest.getPassword()))
                .setDisplayName(signUpRequest.getName());

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(createRequest);
        if (adminRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Kesalahan: Email telah digunakan!"));
        }
        String UserEmail = signUpRequest.getEmail().trim();
        boolean EmailIsNotValid = !UserEmail.matches("^(.+)@(\\S+)$");
        if (EmailIsNotValid){
            return ResponseEntity.badRequest().body(new MessageResponse("Kesalahan: Email tidak valid"));
        }
        String UserPassword = signUpRequest.getPassword().trim();
        boolean PasswordIsNotValid = !UserPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");
        if (PasswordIsNotValid){
            return ResponseEntity.badRequest().body(new MessageResponse("Kesalahan: Password tidak valid"));
        }
        // Create new user's account
        Customer admin = new Customer(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()),signUpRequest.getName()  , signUpRequest.getHp() , signUpRequest.getAddress() , signUpRequest.isActive());
        adminRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse(" Register telah berhasil!  "  +" UID pengguna:  " + userRecord));
    }

    @PostMapping("/customer/forgot_password")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            profileService.sendEmail(emailRequest.getEmail());
            return "Email sent successfully";
        } catch (MessagingException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
