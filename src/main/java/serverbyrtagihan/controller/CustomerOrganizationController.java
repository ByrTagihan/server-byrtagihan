package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.OrganizationDto;
import serverbyrtagihan.modal.Organization;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.OrganizationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerOrganizationController {

    @Autowired
    private OrganizationService userOrganizationService;
    @Autowired
    private ModelMapper modelMapper;

    private static final String JWT_PREFIX = "jwt ";

    @PutMapping(path = "/customer/organization")
    public CommonResponse<Organization> Put(@RequestBody OrganizationDto organizationDTO, @PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(userOrganizationService.putOrganizationCustomer(id,modelMapper.map(organizationDTO, Organization.class), jwtToken));
    }

    @GetMapping(path = "/customer/organization")
    public CommonResponse<List<Organization>> Get(HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(userOrganizationService.getOrganizationCustomer(jwtToken));
    }
}
