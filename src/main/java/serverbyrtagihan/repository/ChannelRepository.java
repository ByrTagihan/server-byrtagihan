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
    @Query("SELECT b FROM channel b WHERE LOWER(b.name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.active) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Channel> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
