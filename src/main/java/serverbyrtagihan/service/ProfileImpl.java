package serverbyrtagihan.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.JwtProvider;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.exception.InternalErrorException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.model.Customer;
import serverbyrtagihan.repository.CustomerRepository;

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
    private CustomerRepository customerRepository;


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
    public Customer add(Customer customer, MultipartFile multipartFile) {
        String picture = imageConverter(multipartFile);
        Customer customer1 = new Customer();
        customer1.setPicture(picture);
        customer1.setEmail(customer.getEmail());
        customer1.setName(customer.getName());
        customer1.setAddress(customer.getAddress());
        customer1.setHp(customer.getHp());
        return customerRepository.save(customer1);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer put(Customer customer, Long id) {
        Customer update = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
        update.setName(customer.getName());
        update.setAddress(customer.getAddress());
        update.setHp(customer.getHp());
        return customerRepository.save(update);
    }

    @Override
    public Customer putPicture(Customer customer, MultipartFile multipartFile, Long id) {
        Customer update = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
        String picture = imageConverter(multipartFile);
        update.setPicture(picture);
        return customerRepository.save(update);
    }

    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            customerRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted" , Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }

    }
}
