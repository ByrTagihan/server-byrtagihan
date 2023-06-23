package serverbyrtagihan.service;

import org.springframework.data.domain.Page;
import serverbyrtagihan.modal.Bill;

import java.util.Map;

public interface BillService {

    //Customer
    Page<Bill> getAll(String jwtToken, Long page, Long pageSize, String sortBy, String sortDirection);

    Page<Bill> searchBillsWithPagination(String jwtToken, String search, Long page, Long pageSize, String sortBy, String sortDirection);

    Bill add(Bill bill, String jwtToken);

    Bill getById(Long id, String jwtToken);

    Bill put(Bill bill, Long id, String jwtToken);

    Bill paid(Bill bill, Long id, String jwtToken);

    Bill unpaid(Bill bill, Long id, String jwtToken);

    Map<String, Boolean> delete(Long id, String jwtToken);

    //Member
    Page<Bill> getByMemberId(Long memberId, String jwtToken, Long page, Long pageSize, String sortBy, String sortDirection);

    Page<Bill> getBillsByMemberId(String jwtToken, Long page, Long pageSize, String sortBy, String sortDirection);

    Bill addByMemberId(Bill bill, Long memberId, String jwtToken);

    Bill getByIdInMember(Long memberId, Long id, String jwtToken);

    Bill putByIdInMember(Bill bill, Long memberId, Long id, String jwtToken);

    Bill paidByIdInMember(Bill bill, Long memberId, Long id, String jwtToken);

    Bill unpaidByIdInMember(Bill bill, Long memberId, Long id, String jwtToken);

    Map<String, Boolean> deleteByIdInMember(Long memberId, Long id, String jwtToken);
}
