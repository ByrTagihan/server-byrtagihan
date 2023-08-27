package serverbyrtagihan.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import serverbyrtagihan.dto.BNIRequestDTO;
import serverbyrtagihan.dto.EcollectionDTO;
import serverbyrtagihan.dto.EcollectionResponseDTO;
import serverbyrtagihan.dto.ReportBill;
import serverbyrtagihan.modal.*;
import serverbyrtagihan.repository.*;
import serverbyrtagihan.service.BillService;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.util.PhoneNumberUtil;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BillServiceImpl implements BillService {

    private final RestTemplate restTemplate;

    @Autowired
    public BillServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    BillRepository billRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    TransactionRepository transactionRepository;
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
        String email = claims.getSubject();
        if (typeToken.equals("Customer")) {
            Member members = memberRepository.findById(bill.getMember_id()).orElseThrow(() -> new NotFoundException("Not found"));
            if (members.getId() == null) {
                throw new NotFoundException("Member Id tidak ditemukan");
            }
            bill.setMember_name(members.getName());
            Customer customer = customerRepository.findByEmail(email).get();
            if (customer.getOrganization_id() == null) {
                throw new BadRequestException("Customer tidak punya organization id");
            }
            bill.setOrganization_id(customer.getOrganization_id());
            Organization organization = organizationRepository.findById(customer.getOrganization_id()).orElseThrow(() -> new NotFoundException("Id Organization not found"));
            bill.setOrganization_name(organization.getName());
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
            return billRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
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
                throw new NotFoundException("id not found");
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
        String email = claims.getSubject();
        if (typeToken.equals("Customer")) {
            Member members = memberRepository.findById(bill.getMember_id()).orElseThrow(() -> new NotFoundException("Not found"));
            if (members.getId() == null) {
                throw new NotFoundException("Member Id tidak ditemukan");
            }
            bill.setMember_name(members.getName());
            Customer customer = customerRepository.findByEmail(email).get();
            if (customer.getOrganization_id() == null) {
                throw new BadRequestException("Customer tidak punya organization id");
            }
            bill.setOrganization_id(customer.getOrganization_id());
            Organization organization = organizationRepository.findById(customer.getOrganization_id()).orElseThrow(() -> new NotFoundException("Id Organization not found"));
            bill.setOrganization_name(organization.getName());
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
                throw new NotFoundException("Bill Not Found");
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
                throw new NotFoundException("id not found");
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public EcollectionResponseDTO paymentById(Payment payment, Long id, String jwtToken) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        String uniqueId = claims.getSubject();
        if (typeToken.equals("Member")) {

            BniEncryption hash = new BniEncryption();
            String cid = "99129"; // from BNI
            String key = "6ae8bbf31b9629a62940aab16ca386cc"; // from BNI
            String prefix = "988"; // from BNI

            Member members = memberRepository.findByUniqueId(uniqueId).orElseThrow(() -> new NotFoundException("Member Not found"));
            Bill bills = billRepository.findByIdInMember(members.getId(), id);

            if (bills.getPaid_id() != 0) {
                throw new NotFoundException("Tagihan Sudah Dibayar");
            }
            Channel channels = channelRepository.findById(payment.getChannel_id()).orElseThrow(() -> new NotFoundException("Channel Not found"));
            Organization organizations = organizationRepository.findById(bills.getOrganization_id()).orElseThrow(() -> new NotFoundException("Organization Not found"));

            String va_number = prefix + cid + PhoneNumberUtil.extractLastEightDigits(members.getHp());

            payment.setOrganization_id(bills.getOrganization_id());
            payment.setOrganization_name(bills.getOrganization_name());
            payment.setMember_id(bills.getMember_id());
            payment.setDescription(bills.getDescription());
            payment.setPeriode(bills.getPeriode());
            payment.setAmount(bills.getAmount());
            payment.setChannel_id(payment.getChannel_id());
            payment.setChannel_name(channels.getName());
            payment.setBill_ids(String.valueOf(bills.getId()));
            payment.setVa_number(va_number);

            ZonedDateTime currentDateTime = ZonedDateTime.now();
            ZonedDateTime newDateTime = currentDateTime.plusHours(1);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            String outputDateTime = newDateTime.format(outputFormatter);

            payment.setVa_expired_date(outputDateTime);
            payment.setFee_admin(5000.0);

            paymentRepository.save(payment);

            bills.setPaid_id(2L);
            bills.setPaid_date(new Date());
            bills.setPaid_amount(bills.getAmount());
            bills.setPayment_id(payment.getId());
            billRepository.save(bills);

            try {
                EcollectionDTO ecollectionDTO = new EcollectionDTO();

                ecollectionDTO.setClient_id(cid);
                String amount = "" + (payment.getAmount() + organizations.getFee_admin());
                ecollectionDTO.setTrx_amount(amount.substring(0, amount.length() - 2 ));
                ecollectionDTO.setCustomer_name(members.getName());
                ecollectionDTO.setCustomer_email("");
                ecollectionDTO.setCustomer_phone(members.getHp());
                ecollectionDTO.setVirtual_account(payment.getVa_number());
                ecollectionDTO.setDatetime_expired(payment.getVa_expired_date());
                ecollectionDTO.setDescription(payment.getDescription());
                if (members.getVa_bni() == null) {
                    members.setVa_bni(va_number);
                    memberRepository.save(members);
                    ecollectionDTO.setType("createbilling");
                } else if (members.getVa_bni() != null) {
                    ecollectionDTO.setVirtual_account("");
                    ecollectionDTO.setType("updatebilling");
                } else if (memberRepository.existsByVa_number(members.getVa_bni())) {
                    String integer = "1";
                    String va_num = members.getVa_bni();
                    members.setVa_bni(va_num + integer);
                    memberRepository.save(members);
                }
                ecollectionDTO.setTrx_id(String.valueOf(payment.getId()));
                ecollectionDTO.setBilling_type("c");

                String payloadJson = objectMapper.writeValueAsString(ecollectionDTO);
                String parsedData = hash.hashData(payloadJson, cid, key);

                BNIRequestDTO bniRequestDTO = new BNIRequestDTO();
                bniRequestDTO.setClient_id(cid);
                bniRequestDTO.setPrefix(prefix);
                bniRequestDTO.setData(parsedData);

                String apiUrl = "https://apibeta.bni-ecollection.com/";

                HttpEntity<BNIRequestDTO> requestEntity = new HttpEntity<>(bniRequestDTO, headers);
                ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
                String responseBody = responseEntity.getBody();

                EcollectionResponseDTO responseDTO = objectMapper.readValue(responseBody, EcollectionResponseDTO.class);
                if (responseDTO.getData() != null) {
                    String decryptedData = hash.parseData(responseDTO.getData(), cid, key);
                    JsonNode decryptedDataJson = objectMapper.readTree(decryptedData);
                    responseDTO.setData(decryptedData);
                    responseDTO.setDatas(decryptedDataJson);

                    Transaction transaction = new Transaction();
                    transaction.setOrganization_Id(bills.getOrganization_id());
                    transaction.setOrganization_name(bills.getOrganization_name());
                    transaction.setDescription(bills.getDescription());
                    transaction.setAmount(bills.getAmount());
                    transaction.setMember_id(bills.getMember_id());
                    transaction.setPayment_id(bills.getPayment_id());
                    transaction.setPayload_webhook(decryptedData);
                    transaction.setPeriode(bills.getPeriode());
                    transactionRepository.save(transaction);
                }
                return responseDTO;
            } catch (Exception e) {
                e.printStackTrace();

                throw new BadRequestException("Failed to send payload to Ecollection");
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public List<ReportBill> getReportRecapBillCustomer(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            String id = claims.getId();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            List<Object[]> billingSummaryResults = billRepository.getReport(year, id);
            List<ReportBill> billingSummaryDTOList = new ArrayList<>();


            for (Object[] result : billingSummaryResults) {
                ReportBill dto = new ReportBill();
                dto.setPeriode((Date) result[0]);
                dto.setCount_bill(Integer.parseInt(result[1].toString()));
                dto.setTotal_bill(Double.parseDouble(result[2].toString()));
                dto.setUnpaid_bill(Double.parseDouble(result[3].toString()));
                dto.setPaid_bill(Double.parseDouble(result[4].toString()));
                billingSummaryDTOList.add(dto);
            }

            return billingSummaryDTOList;
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public List<ReportBill> getReportRecapBillUser(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            List<Object[]> billingSummaryResults = billRepository.getReportRoleUser(year);
            List<ReportBill> billingSummaryDTOList = new ArrayList<>();


            for (Object[] result : billingSummaryResults) {
                ReportBill dto = new ReportBill();
                dto.setPeriode((Date) result[0]);
                dto.setCount_bill(Integer.parseInt(result[1].toString()));
                dto.setTotal_bill(Double.parseDouble(result[2].toString()));
                dto.setUnpaid_bill(Double.parseDouble(result[3].toString()));
                dto.setPaid_bill(Double.parseDouble(result[4].toString()));
                billingSummaryDTOList.add(dto);
            }

            return billingSummaryDTOList;
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public List<ReportBill> getReportRecapBillMember(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("Member")) {
            String unique = claims.getSubject();
            Member member = memberRepository.findByUniqueId(unique).get();
            String id = String.valueOf(member.getOrganization_id());
            int year = Calendar.getInstance().get(Calendar.YEAR);
            List<Object[]> billingSummaryResults = billRepository.getReport(year, id);
            List<ReportBill> billingSummaryDTOList = new ArrayList<>();


            for (Object[] result : billingSummaryResults) {
                ReportBill dto = new ReportBill();
                dto.setPeriode((Date) result[0]);
                dto.setCount_bill(Integer.parseInt(result[1].toString()));
                dto.setTotal_bill(Double.parseDouble(result[2].toString()));
                dto.setUnpaid_bill(Double.parseDouble(result[3].toString()));
                dto.setPaid_bill(Double.parseDouble(result[4].toString()));
                billingSummaryDTOList.add(dto);
            }

            return billingSummaryDTOList;
        } else {
            throw new BadRequestException("Token not valid");
        }
    }
}
