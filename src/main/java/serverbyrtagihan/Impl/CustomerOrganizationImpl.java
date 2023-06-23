package serverbyrtagihan.Impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Modal.CustomerOrganizationModel;
import serverbyrtagihan.Repository.CustomerOrganizationRepository;
import serverbyrtagihan.Service.CustomerOrganizationService;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.security.jwt.JwtUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOrganizationImpl implements CustomerOrganizationService {

    @Autowired
    private CustomerOrganizationRepository customerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public CustomerOrganizationModel add(CustomerOrganizationModel customerOrganizationModel, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            return customerRepository.save(customerOrganizationModel);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public List<CustomerOrganizationModel> getAll(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            return customerRepository.findAll();
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public CustomerOrganizationModel preview(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public CustomerOrganizationModel put(Long id, CustomerOrganizationModel customer, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            CustomerOrganizationModel update = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
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
            return customerRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Map<String, Boolean> delete(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            try {
                customerRepository.deleteById(id);
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
