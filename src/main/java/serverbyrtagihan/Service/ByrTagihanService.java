package serverbyrtagihan.service;

import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.dto.Login;

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
