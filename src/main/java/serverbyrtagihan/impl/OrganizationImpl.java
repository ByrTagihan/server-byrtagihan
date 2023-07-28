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
    public Organization put(Long id, Organization organization, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
      String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            organization.setName(organization.getName());
           organization.setCustomer_id(organization.getCustomer_id());
           organization.setAddress(organization.getAddress());
            organization.setHp(organization.getHp());
            organization.setEmail(organization.getEmail());
           organization.setCity(organization.getCity());
            organization.setProvinsi(organization.getProvinsi());
           organization.setBalance(organization.getBalance());
           organization.setBank_account_number(organization.getBank_account_number());
           organization.setBank_account_name(organization.getBank_account_name());
            organization.setBank_name(organization.getBank_name());
           return organizationRepository.save(organization);
       } else {throw new BadRequestException("Token not valid");
}
    }

    @Override
    public Organization add(Organization organization, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            organization.setName(organization.getName());
            organization.setCustomer_id(organization.getCustomer_id());
            organization.setAddress(organization.getAddress());
            organization.setHp(organization.getHp());
            organization.setEmail(organization.getEmail());
            organization.setCity(organization.getCity());
            organization.setProvinsi(organization.getProvinsi());
            organization.setBalance(organization.getBalance());
            organization.setBank_account_number(organization.getBank_account_number());
            organization.setBank_account_name(organization.getBank_account_name());
            organization.setBank_name(organization.getBank_name());
           return organizationRepository.save(organization);
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
                throw new NotFoundException("id not found");
           }
       } else {
            throw new BadRequestException("Token not valid");
        }
    }
}
