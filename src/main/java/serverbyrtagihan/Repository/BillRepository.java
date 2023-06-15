package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

}
