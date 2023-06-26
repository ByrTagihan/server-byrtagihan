package serverbyrtagihan.service;

import org.springframework.data.domain.Page;
import serverbyrtagihan.modal.Channel;

import java.util.List;
import java.util.Map;

public interface ChannelService {
    Channel add(Channel channel, String jwtToken);


    Page<Channel> getAll(String jwtToken, Long page, Long pageSize, String sort, String sortDirection);

    Page<Channel> getAllMember(String jwtToken, Long page, Long pageSize, String sort, String sortDirection);

    Page<Channel> searchChannelWithPagination(String jwtToken, String search, Long page, Long pageSize, String sort, String sortDirection);


    Page<Channel> searchChannelMemberWithPagination(String jwtToken, String search, Long page, Long pageSize, String sort, String sortDirection);

    Channel preview(Long id, String jwtToken);

    Channel put(Long id, Channel channel, String jwtToken);

    Map<String, Boolean> delete(Long id, String jwtToken);
}
