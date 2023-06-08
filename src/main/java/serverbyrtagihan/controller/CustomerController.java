package serverbyrtagihan.controller;

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
import serverbyrtagihan.dto.PictureDTO;
import serverbyrtagihan.model.Customer;
import serverbyrtagihan.dto.ProfileDTO;
import serverbyrtagihan.repository.CustomerRepository;
import serverbyrtagihan.response.*;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.CustomerDetailsImpl;
import serverbyrtagihan.service.ProfileService;

import javax.validation.Valid;
import java.util.List;

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


    @GetMapping(path = "/customer/profile/{id}")
    public CommonResponse<Customer> getByID(@PathVariable("id") Long id) {
        return ResponseHelper.ok(profileService.getById(id));
    }

    @GetMapping(path ="/customer/profile")
    public CommonResponse<List<Customer>> getAll() {
        return ResponseHelper.ok(profileService.getAll());
    }

    @PutMapping(path = "/customer/picture/{id}", consumes = "multipart/form-data")
    public CommonResponse<Customer> putPicture(@PathVariable("id") Long id, PictureDTO profile, @RequestPart("file") MultipartFile multipartFile) {
        return ResponseHelper.ok(profileService.putPicture(modelMapper.map(profile, Customer.class), multipartFile, id));
    }
    @PutMapping(path = "/customer/profile")
    public CommonResponse<Customer> put(Long id , @RequestBody ProfileDTO profile){
        return ResponseHelper.ok(profileService.put(modelMapper.map(profile, Customer.class), id));
    }
    @DeleteMapping(path = "/customer/profile/{id}")
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (adminRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Kesalahan: Email telah digunakan!"));
        }
        String UserPassword = signUpRequest.getPassword().trim();
        boolean PasswordIsNotValid = !UserPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");
        if (PasswordIsNotValid){
            return ResponseEntity.badRequest().body(new MessageResponse("Kesalahan: Password tidak valid"));
        }
        // Create new user's account
        Customer admin = new Customer(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()),signUpRequest.getName()  , signUpRequest.getAddress() , signUpRequest.getHp() , signUpRequest.isActive());
        adminRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse(" Register telah berhasil!"));
    }


}
