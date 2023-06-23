package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.ForGotPassword;

import java.util.Optional;

@Repository
public interface GetVerification extends JpaRepository<ForGotPassword, Long> {
    Optional<ForGotPassword> findByEmail(String email);

    @Query(value = "SELECT * FROM reset_password  WHERE code_verification = :code", nativeQuery = true)
    Optional<ForGotPassword> findByCode(String code);

}
