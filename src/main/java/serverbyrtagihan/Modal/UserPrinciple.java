package serverbyrtagihan.Modal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrinciple implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> autority;

    public UserPrinciple(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.autority = autority;
    }

    public static UserPrinciple build(User register) {
        return new UserPrinciple(
                register.getId(),
                register.getEmail(),
                register.getPassword()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return autority;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
