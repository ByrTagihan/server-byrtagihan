package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import serverbyrtagihan.dto.LoginMember;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.modal.Member;
import org.springframework.security.core.Authentication;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.MemberService;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Member add(Member member, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            if (memberRepository.findByUniqueId(member.getUnique_id()).isPresent()) {
                throw new BadRequestException("Unique id telah digunakan");
            }
            String UserPassword = member.getPassword().trim();
            boolean PasswordIsNotValid = !UserPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");
            if (PasswordIsNotValid) {
                throw new BadRequestException("Passowrd tidak valid!!");
            }
            // Create new user's account
            Member admin = new Member();
            admin.setUnique_id(member.getUnique_id());
            admin.setPassword(encoder.encode(member.getPassword()));
            admin.setHp(member.getHp());
            admin.setName(member.getName());
            admin.setAddress(member.getAddress());
            admin.setOrganization_id(0L);
            return memberRepository.save(admin);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Member addInUser(Member member, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            if (memberRepository.findByUniqueId(member.getUnique_id()).isPresent()) {
                throw new BadRequestException("Unique id telah digunakan");
            }
            String UserPassword = member.getPassword().trim();
            boolean PasswordIsNotValid = !UserPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");
            if (PasswordIsNotValid) {
                throw new BadRequestException("Passowrd tidak valid!!");
            }
            // Create new user's account
            Member admin = new Member();
            admin.setUnique_id(member.getUnique_id());
            admin.setPassword(encoder.encode(member.getPassword()));
            admin.setHp(member.getHp());
            admin.setName(member.getName());
            admin.setAddress(member.getAddress());
            admin.setOrganization_id(0L);
            return memberRepository.save(admin);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Member getById(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            return memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
    @Override
    public Member getByIdInUser(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
    @Override
    public Map<Object, Object> login(LoginMember loginRequest) {
        Member member = memberRepository.findByUniqueId(loginRequest.getUnique_id()).orElseThrow(() -> new NotFoundException("Username not found"));
        if (encoder.matches( loginRequest.getPassword(),member.getPassword())) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUnique_id(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenMember(authentication);
            LocalDateTime waktuSaatIni = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String waktuFormatted = waktuSaatIni.format(formatter);
            member.setLast_login(waktuFormatted);
            memberRepository.save(member);
            Map<Object, Object> response = new HashMap<>();
            response.put("data", member);
            response.put("token", jwt);
            response.put("last_login", waktuFormatted);
            response.put("type-token", "Customer");
            return response;
        }
        throw new NotFoundException("Password not found");
    }


    @Override
    public Member putPass(PasswordDTO member, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String uniqueId = claims.getSubject();
        String typeToken = claims.getAudience();
        if (typeToken.equals("Member")) {
            Member update = memberRepository.findByUniqueId(uniqueId).orElseThrow(() -> new NotFoundException("Id Not Found"));
            boolean conPassword = encoder.matches(member.getOld_password(), update.getPassword());
            if (conPassword) {
                if (member.getNew_password().equals(member.getConfirm_new_password())) {
                    update.setPassword(encoder.encode(member.getNew_password()));
                    return memberRepository.save(update);
                } else {
                    throw new BadRequestException("Password tidak sesuai");
                }
            } else {
                throw new NotFoundException("Password lama tidak sesuai");
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Page<Member> getAll(String jwtToken, Long page, Long limit, String sort, String search) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sort.startsWith("-")) {
            sort = sort.substring(1);
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(limit), direction, sort);
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            if (search != null && !search.isEmpty()) {
                return memberRepository.findAllByKeyword(search, pageable);
            } else {
                return memberRepository.findAll(pageable);
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
    @Override
    public Page<Member> getAllInUser(String jwtToken, Long page, Long limit, String sort, String search) {
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
                return memberRepository.findAllByKeyword(search, pageable);
            } else {
                return memberRepository.findAll(pageable);
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }


    @Override
    public Member put(Member member, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Member update = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
            update.setName(member.getName());
            update.setAddress(member.getAddress());
            update.setHp(member.getHp());
            return memberRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
    @Override
    public Member putInUser(Member member, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Member update = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
            update.setName(member.getName());
            update.setAddress(member.getAddress());
            update.setHp(member.getHp());
            return memberRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Member putPassword(Member member, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Member update = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
            update.setPassword(encoder.encode(member.getPassword()));
            return memberRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Map<String, Boolean> delete(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            try {
                memberRepository.deleteById(id);
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
    @Override
    public Map<String, Boolean> deleteInUser(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            try {
                memberRepository.deleteById(id);
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