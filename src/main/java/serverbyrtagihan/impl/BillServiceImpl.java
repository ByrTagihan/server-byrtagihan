package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.modal.Payment;
import serverbyrtagihan.repository.BillRepository;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.repository.ChannelRepository;
import serverbyrtagihan.repository.PaymentRepository;
import serverbyrtagihan.service.BillService;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.security.jwt.JwtUtils;

import java.util.*;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    BillRepository billRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    private JwtUtils jwtUtils;

    //Customer
    @Override
    public Page<Bill> getAll(String jwtToken, Long page, Long limit, String sort, String search) {

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
                return billRepository.findAllByKeyword(search, pageable);
            } else {
                return billRepository.findAll(pageable);
            }
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
    public Page<Bill> getByMemberId(Long memberId, String jwtToken, Long page, Long limit, String sort, String search) {

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
                return billRepository.findAllByKeywordMember(memberId, search, pageable);
            } else {
                return billRepository.findByMemberId(memberId, pageable);
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Page<Bill> getBillsByMemberId(String jwtToken, Long page, Long limit, String sort, String search) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sort.startsWith("-")) {
            sort = sort.substring(1);
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(limit), direction, sort);
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        String memberId = claims.getId();
        if (typeToken.equals("Member")) {
            if (search != null && !search.isEmpty()) {
                return billRepository.findAllByKeywordMember(memberId, search, pageable);
            } else {
                return billRepository.findByMemberId(memberId, pageable);
            }
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

    @Override
    public Payment paymentById(Payment payment, Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        Long memberId = Long.valueOf(claims.getId());
        if (typeToken.equals("Member")) {
            Bill bills = billRepository.findByIdInMember(memberId, id);
            if (bills.getPaid_id() != 0) {
                throw new NotFoundException("Tagihan Sudah Dibayar");
            }
            Channel channels = channelRepository.findById(payment.getChannel_id()).orElseThrow(() -> new NotFoundException("Channel Not found"));
            bills.setPaid_id(2L);
            bills.setPaid_date(new Date());
            bills.setPaid_amount(bills.getAmount());
            bills.setPayment_id(payment.getId());
            billRepository.save(bills);

            payment.setOrganization_id(bills.getOrganization_id());
            payment.setOrganization_name(bills.getOrganization_name());
            payment.setMember_id(bills.getMember_id());
            payment.setDescription(bills.getDescription());
            payment.setPeriode(bills.getPeriode());
            payment.setAmount(bills.getAmount());
            payment.setChannel_id(payment.getChannel_id());
            payment.setChannel_name(channels.getName());
            payment.setBill_ids(String.valueOf(bills.getId()));
            payment.setVa_number("12345");
            payment.setVa_expired_date(new Date(System.currentTimeMillis() + 3600 * 1000));
            payment.setFee_admin(5000.0);

            return paymentRepository.save(payment);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
}
