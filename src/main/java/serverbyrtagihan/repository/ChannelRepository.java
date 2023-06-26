package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Bill;
import serverbyrtagihan.modal.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    @Query(value = "SELECT * FROM channel  WHERE name  LIKE CONCAT('%',:keyword, '%') OR active LIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
    Page<Channel> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
