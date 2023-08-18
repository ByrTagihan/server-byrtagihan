package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Reset_Password;

import java.util.Optional;

@Repository
public interface GetVerification extends JpaRepository<Reset_Password, Long> {
    Optional<Reset_Password> findByEmail(String email);

    @Query(value = "SELECT * FROM reset_password  WHERE code_verification = :code", nativeQuery = true)
    Optional<Reset_Password> findByCode(String code);

}
