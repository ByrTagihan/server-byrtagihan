package serverbyrtagihan.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.Modal.Channel;
import serverbyrtagihan.Modal.Organization;
import serverbyrtagihan.Service.ChannelService;
import serverbyrtagihan.Service.OrganizationService;
import serverbyrtagihan.dto.ChannelDTO;
import serverbyrtagihan.dto.OrganizationDto;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping(path = "/user/organization")
    public CommonResponse<Organization> Post(@RequestBody OrganizationDto organization, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(organizationService.add(organization, jwtToken));
    }
    @PutMapping(path = "/user/organization/{id}")
    public CommonResponse<Organization> Put(@RequestBody OrganizationDto organizationDto,@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(organizationService.put(id , organizationDto , jwtToken));
    }
    @GetMapping(path = "/user/organization/{id}")
    public CommonResponse<Organization> Preview(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(organizationService.preview(id, jwtToken));
    }
    @GetMapping(path = "/user/organization")
    public CommonResponse<List<Organization>> Get(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(organizationService.get(jwtToken));
    }
    @DeleteMapping(path = "/user/organization/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id , HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(organizationService.delete(id ,jwtToken));
    }
}
