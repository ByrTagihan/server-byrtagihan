package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.ChannelDTO;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.ChannelService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChannelController {
    @Autowired
    ChannelService channelService;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping(path = "/user/channel")
    public CommonResponse<Channel> Post(@RequestBody ChannelDTO channelDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(channelService.add(modelMapper.map(channelDTO , Channel.class), jwtToken));
    }
    @PutMapping(path = "/user/channel/{id}")
    public CommonResponse<Channel> Put(@RequestBody ChannelDTO channelDTO,@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(channelService.put(id,modelMapper.map(channelDTO , Channel.class), jwtToken));
    }
    @GetMapping(path = "/user/channel/{id}")
    public CommonResponse<Channel> Preview(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(channelService.preview(id, jwtToken));
    }
    @GetMapping(path = "/user/channel")
    public PaginationResponse<List<Channel>> getAll(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(value = "limit", defaultValue = Pagination.size, required = false) Long pageSize,
            @RequestParam(defaultValue = Pagination.sortBy, required = false) String sortBy,
            @RequestParam(defaultValue = Pagination.sortDir) String sortDirection,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("Authorization").substring(7);

        Page<Channel> channelPage;

        if (search != null && !search.isEmpty()) {
            channelPage = channelService.searchChannelWithPagination(jwtToken, search, page, pageSize, sortBy, sortDirection);
        } else {
            channelPage = channelService.getAll(jwtToken, page, pageSize, sortBy, sortDirection);
        }

        List<Channel> channels = channelPage.getContent();
        long totalItems = channelPage.getTotalElements();
        int totalPages = channelPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(channels, pagination);
    }
//    @GetMapping(path = "/member/channel")
//    public PaginationResponse<List<Channel>> getAllMember(
//            HttpServletRequest request,
//            @RequestParam(value = "page", defaultValue = Pagination.page, required = false) Long page,
//            @RequestParam(value = "limit", defaultValue = Pagination.size, required = false) Long pageSize,
//            @RequestParam(defaultValue = Pagination.sortBy, required = false) String sortBy,
//            @RequestParam(defaultValue = Pagination.sortDir) String sortDirection,
//            @RequestParam(required = false) String search
//    ) {
//        String jwtToken = request.getHeader("Authorization").substring(7);
//
//        Page<Channel> channelPage;
//
//        if (search != null && !search.isEmpty()) {
//            channelPage = channelService.searchChannelMemberWithPagination(jwtToken, search, page, pageSize, sortBy, sortDirection);
//        } else {
//            channelPage = channelService.getAll(jwtToken, page, pageSize, sortBy, sortDirection);
//        }
//
//        List<Channel> channels = channelPage.getContent();
//        long totalItems = channelPage.getTotalElements();
//        int totalPages = channelPage.getTotalPages();
//
//        Map<String, Integer> pagination = new HashMap<>();
//        pagination.put("total", (int) totalItems);
//        pagination.put("page", Math.toIntExact(page));
//        pagination.put("total_page", totalPages);
//
//        return ResponseHelper.okWithPagination(channels, pagination);
//    }

    @DeleteMapping(path = "/user/channel/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id , HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(channelService.delete(id ,jwtToken));
    }
    @GetMapping(path = "/member/channel")
    public CommonResponse<List<Channel>> getAll( HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(channelService.preview(jwtToken));
    }
}
