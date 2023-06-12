package serverbyrtagihan.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.exception.InternalErrorException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.model.Customer;
import serverbyrtagihan.repository.CustomerRepository;
import serverbyrtagihan.security.jwt.JwtUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
import java.util.Random;

@Service
public class ProfileImpl implements ProfileService {
    private static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/byrtagihan-ca34f.appspot.com/o/%s?alt=media";


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    PasswordEncoder encoder;

    private String newPassword() {
        Random random = new Random();
        String result = "";
        String character = "0123456789qwertyuiopasdfghjklzxcvbnm";
        for (int i = 0; i < 9; i++) {
            result += character.charAt(random.nextInt(character.length()));
        }
        return result;
    }


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
    public void sendEmail(String to) throws MessagingException {
        String newPassword = newPassword();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        if (customerRepository.findByEmail(to).isPresent()) {
            Customer update = customerRepository.findByEmail(to).get();
            update.setPassword(encoder.encode(newPassword));
            customerRepository.save(update);
            helper.setTo(to);
            helper.setSubject("Password Baru");
            String messageText = "Terima kasih telah menggunakan layanan Bayar Tagihan.\n" +
                    "Harap gunakan password ini untuk login akun Anda.\n" +
                    "Jangan berikan password ini kepada siapa pun, termasuk pihak Bayar Tagihan.\n" +
                    "Jika Anda tidak meminta password ini, abaikan pesan ini.\n" +
                    "Berikut adalah password baru Anda: " + newPassword + "\nTerima kasih, \n byrtagihan.com";
            helper.setText(messageText);
            javaMailSender.send(message);
        } else {



            throw new NotFoundException("Email not found");
        }
    }

    @Override
    public Customer getProfileCustomer(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        return customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer put(Customer customer, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        Customer update = customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Id Not Found"));
        update.setName(customer.getName());
        update.setAddress(customer.getAddress());
        update.setHp(customer.getHp());
        return customerRepository.save(update);
    }
    @Override
    public Customer putPassword(Customer customer, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        Customer update = customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Id Not Found"));
        update.setPassword(encoder.encode(customer.getPassword()));
        return customerRepository.save(update);
    }

    @Override
    public Customer putPicture(Customer customer, MultipartFile multipartFile, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        Customer update = customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Id Not Found"));
        String picture = imageConverter(multipartFile);
        update.setPicture(picture);
        return customerRepository.save(update);
    }

    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            customerRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }

    }
}
