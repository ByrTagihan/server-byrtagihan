package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.OrganizationDto;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Organization;

import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.OrganizationService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserOrganizationController{

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ModelMapper modelMapper;

    private static final String JWT_PREFIX = "jwt ";

    @PostMapping(path = "/user/organization")
    public CommonResponse<Organization> post(@RequestBody OrganizationDto organizationDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(organizationService.add(modelMapper.map(organizationDTO , Organization.class), jwtToken));
    }
    @PutMapping(path = "/user/organization/{id}")
    public CommonResponse<Organization> Put(@RequestBody OrganizationDto organizationDTO, @PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(organizationService.put(id,modelMapper.map(organizationDTO, Organization.class), jwtToken));
    }

    @GetMapping(path = "/user/organization/{id}")
    public CommonResponse<Organization> Preview(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(organizationService.preview(id, jwtToken));
    }
    @GetMapping(path = "/user/organization")
    public PaginationResponse<List<Organization>> getAll(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        Page<Organization> organizationPage;

        if (search != null && !search.isEmpty()) {
            organizationPage = organizationService.getAll(jwtToken, page, limit, sort, search);
        } else {
            organizationPage = organizationService.getAll(jwtToken, page, limit, sort, null);
        }

        List<Organization> organizations = organizationPage.getContent();
        long totalItems = organizationPage.getTotalElements();
        int totalPages = organizationPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(organizations, pagination);
    }
    @DeleteMapping(path = "/user/organization/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id , HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(organizationService.delete(id ,jwtToken));
    }


}
