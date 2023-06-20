package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.swagger.Modal.TemporaryToken;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TemporaryToken, Long> {

    Optional<TemporaryToken> findByToken(String token);

    Optional<TemporaryToken> findByRegisterId(long registerId);

}
