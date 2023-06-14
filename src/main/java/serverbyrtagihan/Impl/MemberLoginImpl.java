package serverbyrtagihan.Impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.Jwt.JwtProvider;
import serverbyrtagihan.Jwt.JwtUtils;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.MemberLogin;
import serverbyrtagihan.Modal.MemberTypeToken;
import serverbyrtagihan.Repository.MemberLoginRepository;
import serverbyrtagihan.Service.MemberLoginService;
import serverbyrtagihan.dto.MemberLoginDto;
import serverbyrtagihan.dto.PasswordMemberDto;
import serverbyrtagihan.dto.UpdateMemberDto;
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
import java.util.regex.Pattern;

@Service
public class MemberLoginImpl implements MemberLoginService {

    private static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/byrtagihan-ca34f.appspot.com/o/%s?alt=media";

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberLoginRepository memberLoginRepository;

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

    private String authories(String unique_id, String token) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(unique_id, token));
        } catch (BadCredentialsException e) {
            throw new InternalErrorException("email or password not found");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(unique_id);
        return jwtUtils.generateJwtToken((Authentication) userDetails);
    }

    @Override
    public MemberLogin addMember(MemberLoginDto memberLoginDto) {
        MemberLogin memberLogin = new MemberLogin();
        memberLogin.setUnique_id(memberLoginDto.getUnique_id());
        memberLogin.setToken(passwordEncoder.encode(memberLoginDto.getToken()));
        memberLogin.setTypeToken(MemberTypeToken.member);
        return memberLoginRepository.save(memberLogin);
    }

    @Override
    public Map<String, Object> loginMember(MemberLoginDto login) {
        String token = authories(login.getUnique_id(), login.getToken());
        MemberLogin memberLogin = null;

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("expired", "15 menit");
        response.put("user", memberLogin);
        return response;
    }

    @Override
    public MemberLogin getById(Long id) {
        return memberLoginRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public List<MemberLogin> getAll() {
        return memberLoginRepository.findAll();
    }

    @Override
    public MemberLogin put(UpdateMemberDto memberDto, MultipartFile multipartFile, Long id) {
        String picture = imageConverter(multipartFile);
        MemberLogin memberLogin = memberLoginRepository.findById(id).orElseThrow(() -> new NotFoundException("id not found"));
        memberLogin.setName(memberDto.getName());
        memberLogin.setHp(memberDto.getHp());
        memberLogin.setAddress(memberDto.getAddress());
        memberLogin.setPicture(picture);
        return memberLoginRepository.save(memberLogin);
    }

    @Override
    public MemberLogin putPass(PasswordMemberDto passwordMemberDto, String jwt) {
        MemberLogin update = new MemberLogin();
        boolean conPassword = passwordEncoder.matches(passwordMemberDto.getOld_pass() , update.getToken());
        if (conPassword) {
            if (passwordMemberDto.getNew_pass().equals(passwordMemberDto.getCon_pass())) {
                update.setToken(passwordEncoder.encode(passwordMemberDto.getNew_pass()));
                return memberLoginRepository.save(update);
            } else {
                throw new NotFoundException("Password tidak sesuai");
            }
        } else {
            throw new NotFoundException("Password lama tidak sesuai");
        }
    }
}
