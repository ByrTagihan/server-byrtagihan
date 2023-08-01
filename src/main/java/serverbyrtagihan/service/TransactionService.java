package serverbyrtagihan.service;

import org.springframework.data.domain.Page;
import serverbyrtagihan.dto.ReportTranscation;
import serverbyrtagihan.modal.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    Transaction add(Transaction transaction, String jwtToken);

    Page<Transaction> getAll(String jwtToken, Long page, Long limit, String sort, String search);

    Transaction preview(Long id, String jwtToken);

    Transaction put(Long id, Transaction transaction, String jwtToken);

    Map<String, Boolean> delete(Long id, String jwtToken);

    List<ReportTranscation> getReportRecapTrancationMember(String jwtToken);

    List<ReportTranscation> getReportRecapTransactionUser(String jwtToken);

    List<ReportTranscation> getReportRecapTrancationCustomer(String jwtToken);
}
