package serverbyrtagihan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.MessageMember;


@Repository
public interface MessageMemberRepository extends JpaRepository<MessageMember, Long> {
        // Tambahkan jika Anda perlu menambahkan metode khusus lainnya
    }

