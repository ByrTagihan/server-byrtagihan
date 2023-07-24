package serverbyrtagihan.auditing;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class DateConfig {
    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
