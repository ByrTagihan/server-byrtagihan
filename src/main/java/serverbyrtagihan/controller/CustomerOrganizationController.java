package serverbyrtagihan.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import serverbyrtagihan.dto.CustomerOrganizationDTO;
import serverbyrtagihan.Modal.CustomerOrganizationModel;
import serverbyrtagihan.Impl.JwtCustomerOrganizationDetailsService;
import serverbyrtagihan.util.CustomErrorType;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerOrganizationController{

    public static final Logger logger = LoggerFactory.getLogger(CustomerOrganizationController.class);

    @Autowired
    private JwtCustomerOrganizationDetailsService customerOrganizationDetailsService;


    //--------------------- Create a CustomerOrganization ---------------------------------

    @RequestMapping(value = "/customer/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createprofile(@RequestBody CustomerOrganizationDTO customerOrganization) throws SQLException, ClassNotFoundException {
        logger.info("Creating Profile : {}",customerOrganization);

        customerOrganizationDetailsService.save(customerOrganization);

        return new ResponseEntity<>(customerOrganization, HttpStatus.CREATED);
    }

    // -------------------Retrieve All Profile--------------------------------------------

    @RequestMapping(value = "/customer", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CustomerOrganizationModel>> listAllCustomerOrganization() throws SQLException, ClassNotFoundException {

        List<CustomerOrganizationModel> customers = customerOrganizationDetailsService.findAll();

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // -------------------Retrieve Single customers organization By Id------------------------------------------

    @RequestMapping(value = "/customer/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerOrganization(@PathVariable("id") long id) throws SQLException, ClassNotFoundException {
        logger.info("Fetching Profile with id {}", id);

        Optional<CustomerOrganizationModel> customerOrganization = customerOrganizationDetailsService.findById(id);

        if (customerOrganization == null) {
            logger.error("customerOrganization with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("customerOrganization with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customerOrganization, HttpStatus.OK);
    }

    // ------------------- Update customerOrganization ------------------------------------------------
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCustomerOrganization(@PathVariable("id") long id, @RequestBody CustomerOrganizationDTO customerOrganization) throws SQLException, ClassNotFoundException {
        logger.info("Updating Profile with id {}", id);

        Optional<CustomerOrganizationModel> currentCustomerOrganization = customerOrganizationDetailsService.findById(id);

        if (currentCustomerOrganization == null) {
            logger.error("Unable to update. CustomerOrganization with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Unable to update. CustomerOrganization with id " + id + " not found."), HttpStatus.NOT_FOUND);
        }
        currentCustomerOrganization.orElseThrow().setName(customerOrganization.getName());
        currentCustomerOrganization.orElseThrow().setAddres(customerOrganization.getAddres());
        currentCustomerOrganization.orElseThrow().setHp(customerOrganization.getHp());
        currentCustomerOrganization.orElseThrow().setEmail(customerOrganization.getEmail());
        currentCustomerOrganization.orElseThrow().setCity(customerOrganization.getCity());
        currentCustomerOrganization.orElseThrow().setProvinsi(customerOrganization.getProvinsi());
        currentCustomerOrganization.orElseThrow().setBalance(customerOrganization.getBalance());
        currentCustomerOrganization.orElseThrow().setBank_acount_number(customerOrganization.getBank_acount_number());
        currentCustomerOrganization.orElseThrow().setBank_account_name(customerOrganization.getBank_account_name());
        currentCustomerOrganization.orElseThrow().setBank_name(customerOrganization.getBank_name());





        customerOrganizationDetailsService.update(currentCustomerOrganization.get().getId());
        return new ResponseEntity<>(currentCustomerOrganization, HttpStatus.OK);

    }

    // ------------------- Delete CustomerOrganization-----------------------------------------

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCustomerOrganization(@PathVariable("id") long id) throws SQLException, ClassNotFoundException {
        logger.info("Fetching & Deleting CustomerOrganization with id {}", id);

        customerOrganizationDetailsService.delete(id);
        return new ResponseEntity<CustomerOrganizationModel>(HttpStatus.NO_CONTENT);
    }
}
