package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.repository.OrganizationRepository;
import serverbyrtagihan.repository.PaymentRepository;
import serverbyrtagihan.dto.PaymentDto;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.modal.Payment;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.PaymentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentImpl implements PaymentService {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public List<Payment> Get(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return paymentRepository.findAll();
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Payment Preview(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return paymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Payment Put(Long id, PaymentDto paymentDto, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Payment update = paymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
            update.setOrganizationId(organizationRepository.findById(paymentDto.getOrganizationId()).orElseThrow(() -> new NotFoundException("Id Organization nOt Found")));
            update.setMemberId(memberRepository.findById(paymentDto.getMemberId()).orElseThrow(() -> new NotFoundException("Member Id Not Found")));
            update.setDescription(paymentDto.getDescription());
            update.setPeriode(paymentDto.getPeriode());
            update.setAmount(paymentDto.getAmount());;
            return paymentRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Payment Add(PaymentDto paymentDto, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Payment update = new Payment();
            update.setOrganizationId(organizationRepository.findById(paymentDto.getOrganizationId()).orElseThrow(() -> new NotFoundException("Organization Not Found")));
            update.setMemberId(memberRepository.findById((paymentDto.getMemberId())).orElseThrow(() -> new NotFoundException("Member Id Not Found")));
            update.setDescription(paymentDto.getDescription());
            update.setPeriode(paymentDto.getPeriode());
            update.setAmount(paymentDto.getAmount());
            return paymentRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Map<String, Boolean> Delete(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            try {
                paymentRepository.deleteById(id);
                Map<String, Boolean> res = new HashMap<>();
                res.put("Deleted", Boolean.TRUE);
                return res;
            } catch (Exception e) {
                return null;
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
}
