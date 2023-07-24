package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.repository.CustomerOrganizationRepository;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.modal.CustomerOrganizationModel;
import serverbyrtagihan.service.CustomerOrganizationService;
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
            update.setBankAccountName(customer.getBankAccountName());
            update.setBankAccountNumber(customer.getBankAccountNumber());
            update.setBankName(customer.getBankName());
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
