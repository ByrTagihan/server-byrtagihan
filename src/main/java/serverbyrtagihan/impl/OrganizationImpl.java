package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.repository.OrganizationRepository;
import serverbyrtagihan.dto.OrganizationDto;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.modal.Organization;
import serverbyrtagihan.repository.CustomerRepository;
import serverbyrtagihan.service.OrganizationService;
import serverbyrtagihan.security.jwt.JwtUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrganizationImpl implements OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public List<Organization> get(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
       if (typeToken.equals("User")) {
          return organizationRepository.findAll();
       } else {
          throw new BadRequestException("Token not valid");
       }
    }

    @Override
    public Organization preview(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
       String typeToken = claims.getAudience();
      if (typeToken.equals("User")) {
           return organizationRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
       } else {
          throw new BadRequestException("Token not valid");
       }
    }

    @Override
    public Organization put(Long id, OrganizationDto organization, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
      String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Organization update = organizationRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
            update.setName(organization.getName());
           update.setCustomerId(customerRepository.findById(organization.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer Id Not Found")));
           update.setAddres(organization.getAddres());
            update.setHp(organization.getHp());
            update.setEmail(organization.getEmail());
           update.setCity(organization.getCity());
            update.setProvinsi(organization.getProvinsi());
           update.setBalance(organization.getBalance());
           update.setBank_account_number(organization.getBank_acount_number());
           update.setBank_account_name(organization.getBank_account_name());
            update.setBank_name(organization.getBank_name());
           return organizationRepository.save(update);
       } else {throw new BadRequestException("Token not valid");
}
    }

    @Override
    public Organization add(OrganizationDto organization, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Organization update = new Organization();
            update.setName(organization.getName());
            update.setCustomerId(customerRepository.findById(organization.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer Id Not Found")));
           update.setAddres(organization.getAddres());
           update.setHp(organization.getHp());
            update.setEmail(organization.getEmail());
            update.setCity(organization.getCity());
            update.setProvinsi(organization.getProvinsi());
            update.setBalance(organization.getBalance());
           update.setBank_account_number(organization.getBank_acount_number());
            update.setBank_account_name(organization.getBank_account_name());
            update.setBank_name(organization.getBank_name());
           return organizationRepository.save(update);
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
               organizationRepository.deleteById(id);
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
