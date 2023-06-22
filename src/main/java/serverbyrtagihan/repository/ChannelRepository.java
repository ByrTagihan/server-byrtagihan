package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
}
