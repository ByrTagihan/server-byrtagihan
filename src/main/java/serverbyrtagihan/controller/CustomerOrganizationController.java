package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.OrganizationDto;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.modal.Organization;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.dto.CustomerOrganizationDTO;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.OrganizationService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CustomerOrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ModelMapper modelMapper;

    private static final String JWT_PREFIX = "jwt ";
    @PutMapping(path = "/customer/organization")
    public CommonResponse<Organization> Put(@RequestBody CustomerOrganizationDTO organizationDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(organizationService.putByCustomerId(modelMapper.map(organizationDTO, Organization.class), jwtToken));
    }

    @GetMapping(path = "/customer/organization")
    public CommonResponse<Organization> Get(HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(organizationService.getByCustomerId(jwtToken));
    }
}
