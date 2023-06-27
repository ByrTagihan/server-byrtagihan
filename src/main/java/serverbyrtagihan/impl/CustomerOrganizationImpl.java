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

}
