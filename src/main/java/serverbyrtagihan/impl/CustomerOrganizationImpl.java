package serverbyrtagihan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Repository.CustomerOrganizationRepository;
import serverbyrtagihan.modal.CustomerOrganizationModel;
import serverbyrtagihan.service.CustomerOrganizationService;
import serverbyrtagihan.exception.NotFoundException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOrganizationImpl implements CustomerOrganizationService {

    @Autowired
    private CustomerOrganizationRepository customerRepository;

    @Override
    public CustomerOrganizationModel add(CustomerOrganizationModel customer) {

        return customerRepository.save(customer);
    }

    @Override
    public CustomerOrganizationModel getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public List<CustomerOrganizationModel> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public CustomerOrganizationModel put(CustomerOrganizationModel customer, Long id) {
        CustomerOrganizationModel update = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
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
    }


    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            customerRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted" , Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }

    }
}
