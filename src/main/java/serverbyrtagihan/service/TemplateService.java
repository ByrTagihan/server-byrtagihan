package serverbyrtagihan.service;

import org.springframework.data.domain.Page;
import serverbyrtagihan.modal.Template;

import java.util.Map;

public interface TemplateService {

    //User
    Page<Template> getAll(String jwtToken, Long page, Long limit, String sort, String search);

    Template add(Template template, String jwtToken);

    Template getById(Long id, String jwtToken);

    Template put(Template template, Long id, String jwtToken);

    Map<String, Boolean> delete(Long id, String jwtToken);
}
