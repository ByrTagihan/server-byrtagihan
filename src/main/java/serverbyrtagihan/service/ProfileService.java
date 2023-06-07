package serverbyrtagihan.service;

import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.model.Profile;

import java.util.List;
import java.util.Map;

public interface ProfileService {
    Profile add(Profile profile, MultipartFile multipartFile);

    Profile getById(Long id);

    List<Profile> getAll();

    Profile put(Profile profile, Long id);

    Profile putPicture(Profile profile, MultipartFile multipartFile, Long id);

    Map<String, Boolean> delete(Long id);
}
