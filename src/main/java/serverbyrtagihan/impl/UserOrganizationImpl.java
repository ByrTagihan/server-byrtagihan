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
    public UserOrganizationModel put(Long id, UserOrganizationModel user, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            UserOrganizationModel update = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
            update.setName(user.getName());
            update.setAddres(user.getAddres());
            update.setHp(user.getHp());
            update.setEmail(user.getEmail());
            update.setCity(user.getCity());
            update.setProvinsi(user.getProvinsi());
            update.setBalance(user.getBalance());
            update.setBankAccountName(user.getBankAccountName());
            update.setBankAccountNumber(user.getBankAccountNumber());
            update.setBankName(user.getBankName());
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
