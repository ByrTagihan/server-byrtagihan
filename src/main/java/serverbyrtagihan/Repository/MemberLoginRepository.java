package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.MemberLogin;

import java.util.Optional;

@Repository
public interface MemberLoginRepository extends JpaRepository<MemberLogin, Long> {

   @Query(value = "SELECT * FROM table_member_login  WHERE " +
           "unique_id LIKE CONCAT('%',:unique_id, '%')", nativeQuery = true)
   MemberLogin memberByUnique(String unique);
}
