package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import serverbyrtagihan.dto.ReportTranscation;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.Organization;
import serverbyrtagihan.modal.Transaction;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.repository.OrganizationRepository;
import serverbyrtagihan.repository.TransactionRepository;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.TransactionService;

import java.util.*;

@Service
public class TransactionImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Transaction add(Transaction transaction, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Transaction add = new Transaction();
            Member memberId = memberRepository.findById(transaction.getMember_id()).orElseThrow(() -> new NotFoundException("Id Member not found"));
            Organization organization = organizationRepository.findById(transaction.getOrganization_Id()).orElseThrow(() -> new NotFoundException("Id Organization not found"));
            add.setAmount(transaction.getAmount());
            add.setPriode(transaction.getPriode());
            add.setDescription(transaction.getDescription());
            add.setMember_id(memberId.getId());
            add.setOrganization_Id(organization.getId());
            add.setOrganization_name(organization.getName());
            return transactionRepository.save(add);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Page<Transaction> getAll(String jwtToken, Long page, Long limit, String sort, String search) {

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
                return transactionRepository.findAllByKeyword(search, pageable);
            } else {
                return transactionRepository.findAll(pageable);
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Transaction preview(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            return transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Transaction put(Long id, Transaction transaction, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Transaction update = transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
            update.setDescription(transaction.getDescription());
            update.setPriode(transaction.getPriode());
            update.setAmount(transaction.getAmount());
            return transactionRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
    @Override
    public Map<String, Boolean> delete(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            try {
                transactionRepository.deleteById(id);
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
    public List<ReportTranscation> getReportRecapTrancationMember(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Member")) {
            String unique_id = claims.getSubject();
            Member member = memberRepository.findByUniqueId(unique_id).get();
            String id = String.valueOf( member.getOrganization_id());
            int year = Calendar.getInstance().get(Calendar.YEAR);
            List<Object[]> billingSummaryResults = transactionRepository.getReport(year, id);
            List<ReportTranscation> billingSummaryDTOList = new ArrayList<>();


            for (Object[] result : billingSummaryResults) {
                ReportTranscation dto = new ReportTranscation();
                dto.setPeriode((Date) result[0]);
                dto.setCount_bill(Integer.parseInt(result[1].toString()));
                dto.setTotal_bill(Double.parseDouble(result[2].toString()));
                billingSummaryDTOList.add(dto);
            }

            return billingSummaryDTOList;
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
    @Override
    public List<ReportTranscation> getReportRecapTransactionUser(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            List<Object[]> billingSummaryResults = transactionRepository.getReportRoleUser(year);
            List<ReportTranscation> billingSummaryDTOList = new ArrayList<>();


            for (Object[] result : billingSummaryResults) {
                ReportTranscation dto = new ReportTranscation();
                dto.setPeriode((Date) result[0]);
                dto.setCount_bill(Integer.parseInt(result[1].toString()));
                dto.setTotal_bill(Double.parseDouble(result[2].toString()));
                billingSummaryDTOList.add(dto);
            }

            return billingSummaryDTOList;
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
    @Override
    public List<ReportTranscation> getReportRecapTrancationCustomer(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            String unique = claims.getSubject();
           String id = claims.getId();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            List<Object[]> billingSummaryResults = transactionRepository.getReport(year, id);
            List<ReportTranscation> billingSummaryDTOList = new ArrayList<>();


            for (Object[] result : billingSummaryResults) {
                ReportTranscation dto = new ReportTranscation();
                dto.setPeriode((Date) result[0]);
                dto.setCount_bill(Integer.parseInt(result[1].toString()));
                dto.setTotal_bill(Double.parseDouble(result[2].toString()));
                billingSummaryDTOList.add(dto);
            }

            return billingSummaryDTOList;
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
}
