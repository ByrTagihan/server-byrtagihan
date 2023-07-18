package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.ChannelDTO;
import serverbyrtagihan.dto.PutTransactionDTO;
import serverbyrtagihan.dto.TransactionDTO;
import serverbyrtagihan.modal.Channel;
import serverbyrtagihan.modal.Transaction;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.PaginationResponse;
import serverbyrtagihan.response.ResponseHelper;
import serverbyrtagihan.service.TransactionService;
import serverbyrtagihan.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping(path = "/user/transaction")
    public CommonResponse<Transaction> Post(@RequestBody TransactionDTO transactionDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(transactionService.add(modelMapper.map(transactionDTO, Transaction.class), jwtToken));
    }

    @PutMapping(path = "/user/transaction/{id}")
    public CommonResponse<Transaction> Put(@PathVariable("id") Long id, @RequestBody PutTransactionDTO transactionDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(transactionService.put(id, modelMapper.map(transactionDTO, Transaction.class), jwtToken));
    }

    @GetMapping(path = "/user/transaction/{id}")
    public CommonResponse<Transaction> Preview(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(transactionService.preview(id, jwtToken));
    }

    @DeleteMapping(path = "/user/transaction/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        return ResponseHelper.ok(transactionService.delete(id, jwtToken));
    }

    @GetMapping(path = "/user/transaction")
    public PaginationResponse<List<Transaction>> getAll(
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ) {
        String jwtToken = request.getHeader("Authorization").substring(7);

        Page<Transaction> transactionPage;

        if (search != null && !search.isEmpty()) {
            transactionPage = transactionService.getAll(jwtToken, page, limit, sort, search);
        } else {
            transactionPage = transactionService.getAll(jwtToken, page, limit, sort, null);
        }

        List<Transaction> transactions = transactionPage.getContent();
        long totalItems = transactionPage.getTotalElements();
        int totalPages = transactionPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(transactions, pagination);
    }
}
