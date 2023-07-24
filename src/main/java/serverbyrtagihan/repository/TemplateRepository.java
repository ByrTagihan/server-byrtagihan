package serverbyrtagihan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverbyrtagihan.modal.Template;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    @Query("SELECT b FROM Template b WHERE LOWER(b.name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(b.content) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Template> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
