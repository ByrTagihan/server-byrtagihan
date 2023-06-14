package serverbyrtagihan.Modal;

import serverbyrtagihan.Auditing.DateConfig;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "temporary_token")
public class TemporaryToken extends DateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(name = "expired_date")
    private Date expiredDate;

    @Column(name = "register_id")
    private long registerId;

    @Column(name = "member_id")
    private long memberId;

    public TemporaryToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(long registerId) {
        this.registerId = registerId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }
}
