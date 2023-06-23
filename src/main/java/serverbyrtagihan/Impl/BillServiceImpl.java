package serverbyrtagihan.Impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Modal.Bill;
import serverbyrtagihan.Repository.BillRepository;
import serverbyrtagihan.Service.BillService;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.security.jwt.JwtUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    BillRepository billRepository;
    @Autowired
    private JwtUtils jwtUtils;

    //Customer
    @Override
    public Page<Bill> getAll(String jwtToken, Long page, Long pageSize) {
        Pageable pageable = PageRequest.of(Math.toIntExact(page) - 1, Math.toIntExact(pageSize));
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            return billRepository.findAll(pageable);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill add(Bill bill, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            return billRepository.save(bill);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill getById(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            return billRepository.findById(id).orElseThrow(()-> new NotFoundException("Not found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill put(Bill bill, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Bill bills = billRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
            bills.setDescription(bill.getDescription());
            bills.setPeriode(bill.getPeriode());
            bills.setAmount(bill.getAmount());

            return billRepository.save(bills);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill paid(Bill bill, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Bill bills = billRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
            bills.setPaid_id(1L);
            bills.setPaid_date(bill.getPaid_date());
            bills.setPaid_amount(bill.getPaid_amount());

            return billRepository.save(bills);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill unpaid(Bill bill, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Bill bills = billRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
            bills.setPaid_id(0L);
            bills.setPaid_date(null);
            bills.setPaid_amount(0.0);

            return billRepository.save(bills);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Map<String, Boolean> delete(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            try {
                billRepository.deleteById(id);
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

    //Member
    @Override
    public Page<Bill> getByMemberId(Long memberId, String jwtToken, Long page, Long pageSize) {
        Pageable pageable = PageRequest.of(Math.toIntExact(page) - 1, Math.toIntExact(pageSize));
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            return billRepository.findByMemberId(memberId, pageable);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill getByIdInMember(Long memberId, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Bill bills = billRepository.findByIdInMember(memberId, id);
            if (bills == null) {
                throw new NotFoundException("Member Not Found");
            }
            return bills;
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill addByMemberId(Bill bill, Long memberId, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            bill.setMember_id(memberId);
            return billRepository.save(bill);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill putByIdInMember(Bill bill, Long memberId, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Bill bills = billRepository.findByIdInMember(memberId, id);
            if (bills == null) {
                throw new NotFoundException("Member Not Found");
            }
            bills.setDescription(bill.getDescription());
            bills.setPeriode(bill.getPeriode());
            bills.setAmount(bill.getAmount());
            return billRepository.save(bills);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill paidByIdInMember(Bill bill, Long memberId, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Bill bills = billRepository.findByIdInMember(memberId, id);
            if (bills == null) {
                throw new NotFoundException("Member Not Found");
            }
            bills.setPaid_id(1L);
            bills.setPaid_date(bill.getPaid_date());
            bills.setPaid_amount(bill.getPaid_amount());
            return billRepository.save(bills);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Bill unpaidByIdInMember(Bill bill, Long memberId, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Bill bills = billRepository.findByIdInMember(memberId, id);
            if (bills == null) {
                throw new NotFoundException("Member Not Found");
            }
            bills.setPaid_id(0L);
            bills.setPaid_date(null);
            bills.setPaid_amount(0.0);

            return billRepository.save(bills);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Map<String, Boolean> deleteByIdInMember(Long memberId, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            try {
                billRepository.deleteById(id);
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
