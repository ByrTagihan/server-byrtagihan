package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Repository.BillRepository;
import serverbyrtagihan.Service.BillService;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.Modal.Bill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    BillRepository billRepository;

    @Override
    public List<Bill> getAll() {
        return billRepository.findAll();
    }

    @Override
    public Bill add(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill getById(Long id) {
        return billRepository.findById(id).orElseThrow(()-> new NotFoundException("Not found"));
    }

    @Override
    public Bill put(Bill bill, Long id) {
        Bill bills = billRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
        bills.setDescription(bill.getDescription());
        bills.setPeriode(bill.getPeriode());
        bills.setAmount(bill.getAmount());

        return billRepository.save(bills);
    }

    @Override
    public Bill paid(Bill bill, Long id) {
        return null;
    }

    @Override
    public Bill unpaid(Bill bill, Long id) {
        return null;
    }

    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            billRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }

    }
}
