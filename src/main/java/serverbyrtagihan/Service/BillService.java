package serverbyrtagihan.Service;

import serverbyrtagihan.Modal.Bill;

import java.util.List;
import java.util.Map;

public interface BillService {

    List<Bill> getAll();

    Bill add(Bill bill);

    Bill getById(Long id);

    Bill put(Bill bill, Long id);

    Bill paid(Bill bill, Long id);

    Bill unpaid(Bill bill, Long id);

    Map<String, Boolean> delete(Long id);
}
