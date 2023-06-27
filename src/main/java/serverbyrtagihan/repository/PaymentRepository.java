package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
