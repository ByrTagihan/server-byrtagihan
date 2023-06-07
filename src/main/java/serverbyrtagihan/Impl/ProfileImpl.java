package serverbyrtagihan.Impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Modal.Profile;
import serverbyrtagihan.Repository.ProfileRepository;
import serverbyrtagihan.Service.ProfileService;
import serverbyrtagihan.exception.InternalErrorException;
import serverbyrtagihan.exception.NotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileImpl implements ProfileService {
    private static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/byrtagihan-ca34f.appspot.com/o/%s?alt=media";


    @Autowired
    private ProfileRepository profileRepository;

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("byrtagihan-ca34f.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/serviceAccount.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private String getExtentions(String fileName) {
        return fileName.split("\\.")[0];
    }

    private String imageConverter(MultipartFile multipartFile) {
        try {
            String fileName = getExtentions(multipartFile.getOriginalFilename());
            File file = convertToFile(multipartFile, fileName);
            var RESPONSE_URL = uploadFile(file, fileName);
            file.delete();
            return RESPONSE_URL;
        } catch (Exception e) {
            e.getStackTrace();
            throw new InternalErrorException("Error upload file");
        }
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return file;
    }


    @Override
    public Profile add(Profile profile, MultipartFile multipartFile) {
        String picture = imageConverter(multipartFile);
        Profile profile1 = new Profile();
        profile1.setPicture(picture);
        profile1.setEmail(profile.getEmail());
        profile1.setName(profile.getName());
        profile1.setAddress(profile.getAddress());
        profile1.setHp(profile.getHp());
        return profileRepository.save(profile1);
    }

    @Override
    public Profile getById(Long id) {
        return profileRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public List<Profile> getAll() {
        return profileRepository.findAll();
    }

    @Override
    public Profile put(Profile profile, Long id) {
        Profile update = profileRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
        update.setName(profile.getName());
        update.setAddress(profile.getAddress());
        update.setHp(profile.getHp());
        return profileRepository.save(update);
    }

    @Override
    public Profile putPicture(Profile profile, MultipartFile multipartFile, Long id) {
        Profile update = profileRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
        String picture = imageConverter(multipartFile);
        update.setPicture(picture);
        return profileRepository.save(update);
    }

    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            profileRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted" , Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }

    }
}
