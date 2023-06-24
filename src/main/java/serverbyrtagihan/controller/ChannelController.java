package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.ChannelDTO;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.ChannelService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public CommonResponse<List<Channel>> Get(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(channelService.getAll(jwtToken));
    }
    @DeleteMapping(path = "/user/channel/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id , HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(channelService.delete(id ,jwtToken));
    }
}
