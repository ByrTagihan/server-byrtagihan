package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.modal.Template;
import serverbyrtagihan.repository.TemplateRepository;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.TemplateService;

import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    private JwtUtils jwtUtils;

    //Customer
    @Override
    public Page<Template> getAll(String jwtToken, Long page, Long limit, String sort, String search) {

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
                return templateRepository.findAllByKeyword(search, pageable);
            } else {
                return templateRepository.findAll(pageable);
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Template add(Template template, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return templateRepository.save(template);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Template getById(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return templateRepository.findById(id).orElseThrow(()-> new NotFoundException("Not found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Template put(Template template, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Template templates = templateRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
            templates.setName(template.getName());
            templates.setContent(template.getContent());

            return templateRepository.save(templates);
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
                templateRepository.deleteById(id);
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
