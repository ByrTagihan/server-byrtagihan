package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.Modal.CustomerOrganizationModel;
import serverbyrtagihan.Service.CustomerOrganizationService;
import serverbyrtagihan.dto.CustomerOrganizationDTO;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;

import java.util.List;

@RestController
@RequestMapping("/api/organization")
public class CustomerOrganizationController{

    public static final Logger logger = LoggerFactory.getLogger(CustomerOrganizationController.class);
    @Autowired
    private CustomerOrganizationService customerOrganization;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "/customer/organization")
    public CommonResponse<CustomerOrganizationModel> add(@RequestBody CustomerOrganizationDTO customerOrganizationDTO) {
        return ResponseHelper.ok(customerOrganization.add(modelMapper.map(customerOrganizationDTO, CustomerOrganizationModel.class)));
    }

    @GetMapping(path = "/customer/organization/{id}")
    public CommonResponse<CustomerOrganizationModel> getByID(@PathVariable("id") Long id) {
        return ResponseHelper.ok(customerOrganization.getById(id));
    }

    @GetMapping(path ="/customer/organization")
    public CommonResponse<List<CustomerOrganizationModel>> getAll() {
        return ResponseHelper.ok(customerOrganization.getAll());
    }

    @PutMapping(path = "/customer/organization/{id}")
    public CommonResponse<CustomerOrganizationModel> put(  @PathVariable("id") Long id ,@RequestBody CustomerOrganizationDTO customerOrganizationDTO){
        return ResponseHelper.ok(customerOrganization.put(modelMapper.map(customerOrganizationDTO, CustomerOrganizationModel.class), id));
    }
    @DeleteMapping(path = "/customer/organization/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id) {
        return ResponseHelper.ok(customerOrganization.delete(id));
    }


}
