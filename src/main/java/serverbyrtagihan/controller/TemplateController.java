package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.TemplateDTO;
import serverbyrtagihan.modal.Template;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.TemplateService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TemplateController {

    @Autowired
    TemplateService templateService;

    @Autowired
    private ModelMapper modelMapper;

    private static final String JWT_PREFIX = "jwt ";

    @GetMapping(path = "/user/template")
    public PaginationResponse<List<Template>> getAll(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
     ) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        Page<Template> templatePage;

        if (search != null && !search.isEmpty()) {
            templatePage = templateService.getAll(jwtToken, page, limit, sort, search);
        } else {
            templatePage = templateService.getAll(jwtToken, page, limit, sort, null);
        }

        List<Template> templates = templatePage.getContent();
        long totalItems = templatePage.getTotalElements();
        int totalPages = templatePage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(templates, pagination);
    }

    @GetMapping(path = "/user/template/{id}")
    public CommonResponse<Template> getById(HttpServletRequest request, @PathVariable("id") Long id) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(templateService.getById(id, jwtToken));
    }

    @PostMapping(path = "/user/template")
    public CommonResponse<Template> add(HttpServletRequest request, @RequestBody TemplateDTO template) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(templateService.add(modelMapper.map(template, Template.class), jwtToken));
    }

    @PutMapping(path = "/user/template/{id}")
    public CommonResponse<Template> put(HttpServletRequest request, @RequestBody TemplateDTO template, @PathVariable("id") Long id) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(templateService.put(modelMapper.map(template, Template.class), id, jwtToken));
    }

    @DeleteMapping(path = "/user/template/{id}")
    public CommonResponse<?> delete(HttpServletRequest request, @PathVariable("id") Long id) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(templateService.delete(id, jwtToken));
    }
}
