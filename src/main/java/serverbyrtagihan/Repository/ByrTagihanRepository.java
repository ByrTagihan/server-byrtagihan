package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.ByrTagihan;

@Repository
public interface ByrTagihanRepository extends JpaRepository<ByrTagihan, Long> {

    @Query(value = "SELECT * FROM table_byr_tagihan WHERE " +
            "email LIKE CONCAT('%',:email, '%')", nativeQuery = true)
    ByrTagihan findByEmail(String email);

}
