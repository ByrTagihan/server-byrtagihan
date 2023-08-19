package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.modal.Member;
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
    public Page<Organization> getAll(String jwtToken, Long page, Long limit, String sort, String search) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sort.startsWith("-")) {
            sort = sort.substring(1);
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(limit), direction, sort);
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            if (search != null && !search.isEmpty()) {
                return organizationRepository.findAllByKeyword(search, pageable);
            } else {
                return organizationRepository.findAll(pageable);
            }
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
            Organization organizations = organizationRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
            organizations.setName(organization.getName());
            organizations.setCustomer_id(organization.getCustomer_id());
            organizations.setAddress(organization.getAddress());
            organizations.setHp(organization.getHp());
            organizations.setEmail(organization.getEmail());
            organizations.setCity(organization.getCity());
            organizations.setProvinsi(organization.getProvinsi());
            organizations.setBalance(organization.getBalance());
            organizations.setBank_account_number(organization.getBank_account_number());
            organizations.setBank_account_name(organization.getBank_account_name());
            organizations.setBank_name(organization.getBank_name());
           return organizationRepository.save(organizations);
       } else {throw new BadRequestException("Token not valid");
}
    }

    @Override
    public Organization add(Organization organization, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
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

    //Customer
    @Override
    public Organization getByCustomerId(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        String email = claims.getSubject();
        if (typeToken.equals("Customer")) {
            Customer customer = customerRepository.findByEmail(email).get();
            if (customer.getOrganization_id() == null) {
                throw new BadRequestException("Customer tidak punya organization id");
            }
            return organizationRepository.findById(customer.getOrganization_id()).orElseThrow(() -> new NotFoundException("Id not found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Organization putByCustomerId(Organization organization, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        String email = claims.getSubject();
        if (typeToken.equals("Customer")) {
            Customer customer = customerRepository.findByEmail(email).get();
            if (customer.getOrganization_id() == null) {
                throw new BadRequestException("Customer tidak punya organization id");
            }
            Organization organizations = organizationRepository.findById(customer.getOrganization_id()).orElseThrow(() -> new NotFoundException("Id not found"));
            organizations.setName(organization.getName());
            organizations.setAddress(organization.getAddress());
            organizations.setHp(organization.getHp());
            organizations.setEmail(organization.getEmail());
            organizations.setCity(organization.getCity());
            organizations.setProvinsi(organization.getProvinsi());
            organizations.setBank_account_number(organization.getBank_account_number());
            organizations.setBank_account_name(organization.getBank_account_name());
            organizations.setBank_name(organization.getBank_name());
            return organizationRepository.save(organizations);
        } else {throw new BadRequestException("Token not valid");
        }
    }
}
