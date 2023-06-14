package serverbyrtagihan.Modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import serverbyrtagihan.Impl.UserDetailsImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MemberPrinciple implements UserDetails {


    private String unique_id;

    private String token;

    private Collection<? extends GrantedAuthority> autority;

    public MemberPrinciple(String unique_id, String token) {
        this.unique_id = unique_id;
        this.token = token;
        this.autority = autority;
    }

    public static MemberPrinciple build(MemberLogin register) {
        return new MemberPrinciple(
                register.getUnique_id(),
                register.getToken()
        );
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
    public String getUsername() {
        return unique_id;
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
}
