package serverbyrtagihan.Service;

import serverbyrtagihan.Modal.Channel;

import java.util.List;
import java.util.Map;

public interface ChannelService {
    Channel add(Channel channel, String jwtToken);

    List<Channel> getAll(String jwtToken);

    Channel preview(Long id, String jwtToken);

    Channel put(Long id, Channel channel, String jwtToken);

    Map<String, Boolean> delete(Long id, String jwtToken);
}
