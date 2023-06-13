package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.ByrTagihan;

@Repository
public interface ByrTagihanRepository extends JpaRepository<ByrTagihan, Long> {

    ByrTagihan findByEmail(String email);
}
