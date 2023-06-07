package serverbyrtagihan.Service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.Login;

import java.util.List;
import java.util.Map;

public interface ByrTagihanService {
    ByrTagihan register(ByrTagihan byrTagihan);

    Map<String, Object> login (Login login);

    ByrTagihan getById(Long id);

    ByrTagihan update(Long id, ByrTagihan byrTagihan);

    ByrTagihan updatePassword(Long id, ByrTagihan byrTagihan);

    Map<String, Boolean> deleteRegister(Long id);

    List<ByrTagihan> getAllTagihan();
}
