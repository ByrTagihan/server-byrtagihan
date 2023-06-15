package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Jwt.JwtProvider;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.dto.Login;
import serverbyrtagihan.Repository.ByrTagihanRepository;
import serverbyrtagihan.exception.InternalErrorException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.Service.ByrTagihanService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ByrTagihanImpl implements ByrTagihanService {

    @Autowired
    ByrTagihanRepository byrTagihanRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String authories(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new InternalErrorException("email or password not found");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtProvider.generateToken(userDetails);
    }

    @Override
    public ByrTagihan register(ByrTagihan byrTagihan) {
        String UserPassword = byrTagihan.getPassword().trim();
        boolean PasswordIsNotValid = !UserPassword.matches("^(?=.*[0-8])(?=.*[a-z])(?=\\S+$).{8,20}");
        if (PasswordIsNotValid) throw new InternalErrorException("password not valid");
        boolean isEmail = Pattern.compile("^(.+)@(\\S+)$")
                .matcher(byrTagihan.getEmail()).matches();
        if (!isEmail) throw new InternalErrorException("Email Not Valid");
        byrTagihan.setPassword(passwordEncoder.encode(byrTagihan.getPassword()));
        return byrTagihanRepository.save(byrTagihan);
    }

    @Override
    public Map<String, Object> login(Login login) {
        String token = authories(login.getEmail(), login.getPassword());
        ByrTagihan byrTagihan = null;

//        mengecek email
        boolean isEmail = Pattern.compile("^(.+)@(\\S+)$")
                .matcher(login.getEmail()).matches();
        System.out.println("is Email " + isEmail);

//        jika true, akan menjalankan sistem if
        if(isEmail) {
            byrTagihan = byrTagihanRepository.findByEmail(login.getEmail());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("expired", "15 menit");
        response.put("user", byrTagihan);
        return response;
    }

    @Override
    public ByrTagihan getById(Long id) {
        return byrTagihanRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public ByrTagihan update(Long id, ByrTagihan byrTagihan) {
        ByrTagihan register = byrTagihanRepository.findById(id).orElseThrow(() -> new NotFoundException("id not found"));
        register.setEmail(byrTagihan.getEmail());
        boolean isEmail = Pattern.compile("^(.+)@(\\S+)$")
                .matcher(register.getEmail()).matches();
        if (!isEmail)throw new InternalErrorException("Email Not Valid");
        return byrTagihanRepository.save(register);
    }

    @Override
    public ByrTagihan updatePassword(Long id, ByrTagihan byrTagihan) {
        ByrTagihan register = byrTagihanRepository.findById(id).orElseThrow(() -> new NotFoundException("id not found"));
        String UserPassword = byrTagihan.getPassword().trim();
        boolean PasswordIsNOtValid = !UserPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");
        if (PasswordIsNOtValid) throw new InternalErrorException("password not valid");
        register.setPassword(passwordEncoder.encode(byrTagihan.getPassword()));
        return byrTagihanRepository.save(register);
    }

    @Override
    public Map<String, Boolean> deleteRegister(Long id) {
        try {
            byrTagihanRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            throw new NotFoundException("Id Not Found");
        }
    }

    @Override
    public List<ByrTagihan> getAllTagihan() {
        return byrTagihanRepository.findAll();
    }
}
