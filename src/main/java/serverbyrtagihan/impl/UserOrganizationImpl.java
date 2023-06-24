package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.modal.UserOrganizationModel;
import serverbyrtagihan.repository.UserOrganizationRepository;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.UserOrganizationService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserOrganizationImpl implements UserOrganizationService {

    @Autowired
    private UserOrganizationRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserOrganizationModel add(UserOrganizationModel userOrganizationModel, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return userRepository.save(userOrganizationModel);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public List<UserOrganizationModel> getAll(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return userRepository.findAll();
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public UserOrganizationModel preview(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public UserOrganizationModel put(Long id, UserOrganizationModel customer, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            UserOrganizationModel update = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
            update.setName(customer.getName());
            update.setAddres(customer.getAddres());
            update.setHp(customer.getHp());
            update.setEmail(customer.getEmail());
            update.setCity(customer.getCity());
            update.setProvinsi(customer.getProvinsi());
            update.setBalance(customer.getBalance());
            update.setBank_account_name(customer.getBank_account_name());
            update.setBank_account_number(customer.getBank_account_number());
            update.setBank_name(customer.getBank_name());
            return userRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Map<String, Boolean> delete(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            try {
                userRepository.deleteById(id);
                Map<String, Boolean> res = new HashMap<>();
                res.put("Deleted", Boolean.TRUE);
                return res;
            } catch (Exception e) {
                return null;
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
}
