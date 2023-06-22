package serverbyrtagihan.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import serverbyrtagihan.modal.Member;

import java.util.Collection;
import java.util.Objects;

public class MemberDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String type;

    private String username;

    @JsonIgnore
    private String password;

    public MemberDetailsImpl(Long id, String uniqueId, String password ,String type) {
        this.id = id;
        this.username = uniqueId;
        this.password = password;
        this.type = type;
    }

    public static MemberDetailsImpl buildMember(Member admin) {
        return new MemberDetailsImpl(
                admin.getId(),
                admin.getUniqueId(),
                admin.getPassword(),
                admin.getTypeToken());
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
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
        MemberDetailsImpl admin = (MemberDetailsImpl) o;
        return Objects.equals(id, admin.id);
    }
}
