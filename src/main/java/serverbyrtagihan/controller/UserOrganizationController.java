package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.Modal.CustomerOrganizationModel;
import serverbyrtagihan.Modal.UserOrganizationModel;
import serverbyrtagihan.Service.CustomerOrganizationService;
import serverbyrtagihan.Service.UserOrganizationService;
import serverbyrtagihan.dto.CustomerOrganizationDTO;
import serverbyrtagihan.dto.UserOrganizationDTO;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/organization")
public class UserOrganizationController{

    @Autowired
    private UserOrganizationService userOrganizationService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "/user/organization")
    public CommonResponse<UserOrganizationModel> post(@RequestBody UserOrganizationDTO organizationDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(userOrganizationService.add(modelMapper.map(organizationDTO , UserOrganizationModel.class), jwtToken));
    }
    @PutMapping(path = "/user/organization/{id}")
    public CommonResponse<UserOrganizationModel> Put(@RequestBody UserOrganizationDTO organizationDTO, @PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(userOrganizationService.put(id,modelMapper.map(organizationDTO , UserOrganizationModel.class), jwtToken));
    }
    @GetMapping(path = "/user/organization{id}")
    public CommonResponse<UserOrganizationModel> Preview(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(userOrganizationService.preview(id, jwtToken));
    }
    @GetMapping(path = "/user/organization")
    public CommonResponse<List<UserOrganizationModel>> Get(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(userOrganizationService.getAll(jwtToken));
    }
    @DeleteMapping(path = "/user/organization/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id , HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(userOrganizationService.delete(id ,jwtToken));
    }


}
