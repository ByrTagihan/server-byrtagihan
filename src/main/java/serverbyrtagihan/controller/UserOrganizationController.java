package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.UserOrganizationDTO;
import serverbyrtagihan.modal.CustomerOrganizationModel;
import serverbyrtagihan.modal.UserOrganizationModel;
import serverbyrtagihan.dto.OrganizationDto;
import serverbyrtagihan.modal.Organization;

import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.OrganizationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserOrganizationController{

    @Autowired
    private OrganizationService userOrganizationService;
    @Autowired
    private ModelMapper modelMapper;

    private static final String JWT_PREFIX = "jwt ";

    @PostMapping(path = "/user/organization")
    public CommonResponse<Organization> post(@RequestBody OrganizationDto organizationDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(userOrganizationService.add(organizationDTO, jwtToken));
    }
    @PutMapping(path = "/user/organization/{id}")
    public CommonResponse<Organization> Put(@RequestBody OrganizationDto organizationDTO, @PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(userOrganizationService.put(id,organizationDTO, jwtToken));
    }

    @GetMapping(path = "/user/organization/{id}")
    public CommonResponse<Organization> Preview(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(userOrganizationService.preview(id, jwtToken));
    }
    @GetMapping(path = "/user/organization")
    public CommonResponse<List<Organization>> Get(HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(userOrganizationService.get(jwtToken));
    }
    @DeleteMapping(path = "/user/organization/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id , HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(userOrganizationService.delete(id ,jwtToken));
    }


}
