package serverbyrtagihan.service;

import serverbyrtagihan.dto.OrganizationDto;
import serverbyrtagihan.dto.PaymentDto;
import serverbyrtagihan.modal.Organization;
import serverbyrtagihan.modal.Payment;

import java.util.List;
import java.util.Map;

public interface PaymentService {

    List<Payment> Get(String jwtToken);

    Payment Preview(Long id, String jwtToken);

    Payment Put(Long id , PaymentDto paymentDto , String jwtToken);

    Payment Add(PaymentDto paymentDto, String jwtToken);

    Map<String, Boolean> Delete(Long id , String jwtToken);

}
