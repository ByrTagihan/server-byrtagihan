package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.repository.ChannelRepository;
import serverbyrtagihan.service.ChannelService;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.modal.Channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChannelImpl implements ChannelService {
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Channel add(Channel channel, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return channelRepository.save(channel);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Page<Channel> getAll(String jwtToken, Long page, Long limit, String sort, String search) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sort.startsWith("-")) {
            sort = sort.substring(1);
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(limit), direction, sort);
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            if (search != null && !search.isEmpty()) {
                return channelRepository.findAllByKeyword(search, pageable);
            } else {
                return channelRepository.findAll(pageable);
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }


    @Override
    public Page<Channel> getAllMember(String jwtToken, Long page, Long limit, String sort, String search) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sort.startsWith("-")) {
            sort = sort.substring(1);
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(limit), direction, sort);
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Member")) {
            if (search != null && !search.isEmpty()) {
                return channelRepository.findAllByKeyword(search, pageable);
            } else {
                return channelRepository.findAll(pageable);
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }


    @Override
    public Channel preview(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return channelRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
    @Override
    public List<Channel> preview( String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Member")) {
            return channelRepository.findAll();
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Channel put(Long id, Channel channel, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Channel update = channelRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
            update.setName(channel.getName());
            update.setActive(channel.getActive());
            return channelRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Map<String, Boolean> delete(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            try {
                channelRepository.deleteById(id);
                Map<String, Boolean> res = new HashMap<>();
                res.put("Deleted", Boolean.TRUE);
                return res;
            } catch (Exception e) {
                throw new NotFoundException("id not found");
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
}
