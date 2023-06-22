package serverbyrtagihan.service;

import serverbyrtagihan.modal.Bill;

import java.util.List;
import java.util.Map;

public interface MemberBillService {

    List<Bill> getAll(String jwtToken);

    Bill add(Bill bill, String jwtToken);

    Bill getById(Long id, String jwtToken);

    List<Bill> getByMemberId(Long memberId, String jwtToken);

    Bill put(Bill bill, Long id, String jwtToken);

    Bill paid(Bill bill, Long id, String jwtToken);

    Bill unpaid(Bill bill, Long id, String jwtToken);

    Map<String, Boolean> delete(Long id, String jwtToken);
}
