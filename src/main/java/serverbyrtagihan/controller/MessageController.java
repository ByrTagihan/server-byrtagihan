package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.MessageDTO;
import serverbyrtagihan.dto.MessageStatusDTO;
import serverbyrtagihan.modal.Message;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.MessageService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    private ModelMapper modelMapper;

    private static final String JWT_PREFIX = "jwt ";

    @GetMapping(path = "/user/message")
    public PaginationResponse<List<Message>> getAll(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(name = "message_type_id", required = false) Long message_type_id,
            @RequestParam(name = "message_status_id", required = false) Long message_status_id,
            @RequestParam(name = "send_as", required = false) String send_as
            ) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());


        Page<Message> messagePage;

        messagePage = messageService.getAll(jwtToken, page, limit, sort, message_type_id, message_status_id, send_as);

        List<Message> messages = messagePage.getContent();
        long totalItems = messagePage.getTotalElements();
        int totalPages = messagePage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(messages, pagination);
    }

    @GetMapping(path = "/user/message/{id}")
    public CommonResponse<Message> getById(HttpServletRequest request, @PathVariable("id") Long id) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        return ResponseHelper.ok(messageService.getById(id, jwtToken));
    }

    @PostMapping(path = "/user/message")
    public CommonResponse<Message> add(HttpServletRequest request, @RequestBody MessageDTO message) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        return ResponseHelper.ok(messageService.add(modelMapper.map(message, Message.class), jwtToken));
    }

    @PutMapping(path = "/user/message/{id}")
    public CommonResponse<Message> put(HttpServletRequest request, @RequestBody MessageDTO message, @PathVariable("id") Long id) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        return ResponseHelper.ok(messageService.put(modelMapper.map(message, Message.class), id, jwtToken));
    }

    @PutMapping(path = "/user/message/{id}/status")
    public CommonResponse<Message> putStatus(HttpServletRequest request, @RequestBody MessageStatusDTO message, @PathVariable("id") Long id) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        return ResponseHelper.ok(messageService.putStatus(modelMapper.map(message, Message.class), id, jwtToken));
    }

    @DeleteMapping(path = "/user/message/{id}")
    public CommonResponse<?> delete(HttpServletRequest request, @PathVariable("id") Long id) {
                String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        return ResponseHelper.ok(messageService.delete(id, jwtToken));
    }
}
