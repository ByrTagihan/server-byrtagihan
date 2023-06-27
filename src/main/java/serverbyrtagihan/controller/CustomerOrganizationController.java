package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.modal.CustomerOrganizationModel;
import serverbyrtagihan.service.CustomerOrganizationService;
import serverbyrtagihan.dto.CustomerOrganizationDTO;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerOrganizationController{

    @Autowired
    private CustomerOrganizationService customerOrganizationService;
    @Autowired
    private ModelMapper modelMapper;

    @PutMapping(path = "/customer/organization/{id}")
    public CommonResponse<CustomerOrganizationModel> Put(@RequestBody CustomerOrganizationDTO organizationDTO,@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerOrganizationService.put(id,modelMapper.map(organizationDTO , CustomerOrganizationModel.class), jwtToken));
    }
    @GetMapping(path = "/customer/organization")
    public CommonResponse<List<CustomerOrganizationModel>> Get(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerOrganizationService.getAll(jwtToken));
    }
}
