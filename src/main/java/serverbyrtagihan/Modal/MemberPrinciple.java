package serverbyrtagihan.Modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class MemberPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String unique_id;

    @JsonIgnore
    private String token;

    private Collection<? extends GrantedAuthority> autority;

    public MemberPrinciple(Long id, String unique_id, String token) {
        this.id = id;
        this.unique_id = unique_id;
        this.token = token;
        this.autority = autority;
    }

    public static MemberPrinciple build(MemberLogin admin) {
        return new MemberPrinciple(
                admin.getId(),
                admin.getUnique_id(),
                admin.getToken());
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return unique_id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return autority;
    }

    @Override
    public String getPassword() {
        return token;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MemberPrinciple admin = (MemberPrinciple) o;
        return Objects.equals(id, admin.id);
    }

}