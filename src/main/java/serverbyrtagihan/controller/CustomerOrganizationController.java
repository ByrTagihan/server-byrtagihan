package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.modal.CustomerOrganizationModel;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.service.CustomerOrganizationService;
import serverbyrtagihan.dto.CustomerOrganizationDTO;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organization")
public class CustomerOrganizationController{

    @Autowired
    private CustomerOrganizationService customerOrganizationService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "/customer/organization")
    public CommonResponse<CustomerOrganizationModel> post(@RequestBody CustomerOrganizationDTO organizationDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerOrganizationService.add(modelMapper.map(organizationDTO , CustomerOrganizationModel.class), jwtToken));
    }
    @PutMapping(path = "/customer/organization/{id}")
    public CommonResponse<CustomerOrganizationModel> Put(@RequestBody CustomerOrganizationDTO organizationDTO,@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerOrganizationService.put(id,modelMapper.map(organizationDTO , CustomerOrganizationModel.class), jwtToken));
    }
    @GetMapping(path = "/customer/organization/{id}")
    public CommonResponse<CustomerOrganizationModel> Preview(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerOrganizationService.preview(id, jwtToken));
    }
    @GetMapping(path = "/user/organization")
    public PaginationResponse<List<CustomerOrganizationModel>> getAll(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("Authorization").substring(7);

        Page<CustomerOrganizationModel> channelPage;

        if (search != null && !search.isEmpty()) {
            channelPage = customerOrganizationService.getAll(jwtToken, page, limit, sort, search);
        } else {
            channelPage = customerOrganizationService.getAll(jwtToken, page, limit, sort, null);
        }

        List<CustomerOrganizationModel> channels = channelPage.getContent();
        long totalItems = channelPage.getTotalElements();
        int totalPages = channelPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(channels, pagination);
    }
    @DeleteMapping(path = "/customer/organization/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id , HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(customerOrganizationService.delete(id ,jwtToken));
    }


}
