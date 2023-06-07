package serverbyrtagihan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.Modal.Profile;

@Repository
public interface ProfileRepository  extends JpaRepository<Profile, Long> {
}
