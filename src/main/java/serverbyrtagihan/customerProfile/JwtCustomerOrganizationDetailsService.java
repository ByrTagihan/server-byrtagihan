package serverbyrtagihan.customerProfile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class JwtCustomerOrganizationDetailsService {

    @Autowired
    private CustomerOrganizationRepository CustomerOrganizationDao;
    private long id;

    public JwtCustomerOrganizationDetailsService() {
    }

    public CustomerOrganizationModel save(CustomerOrganizationDTO customerOrganization) {

        CustomerOrganizationModel newCustomerOrganization = new CustomerOrganizationModel();
        newCustomerOrganization.setName(customerOrganization.getName());
        newCustomerOrganization.setAddres(customerOrganization.getAddres());
        newCustomerOrganization.setHp(customerOrganization.getHp());
        newCustomerOrganization.setEmail(customerOrganization.getEmail());
        newCustomerOrganization.setCity(customerOrganization.getCity());
        newCustomerOrganization.setProvinsi(customerOrganization.getProvinsi());
        newCustomerOrganization.setBalance(customerOrganization.getBalance());
        newCustomerOrganization.setBank_acount_number(customerOrganization.getBank_acount_number());
        newCustomerOrganization.setBank_account_name(customerOrganization.getBank_account_name());
        newCustomerOrganization.setBank_name(customerOrganization.getBank_name());



        return CustomerOrganizationDao.save(newCustomerOrganization);
    }


    public Optional<CustomerOrganizationModel> findById(Long id) {
        return Optional.ofNullable(CustomerOrganizationDao.findById(id));
    }


    public List<CustomerOrganizationModel> findAll() {
        List<CustomerOrganizationModel> customers = new ArrayList<>();
        CustomerOrganizationDao.findAll().forEach(customers::add);
        return customers;
    }


    public void delete(Long id) {
        CustomerOrganizationModel customerOrganization = CustomerOrganizationDao.findById(id);
        CustomerOrganizationDao.delete(customerOrganization);
    }


    public CustomerOrganizationModel update(Long id) {
        CustomerOrganizationModel customerOrganization = CustomerOrganizationDao.findById(id);
        return CustomerOrganizationDao.save(customerOrganization);
    }

}