package serverbyrtagihan.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import serverbyrtagihan.modal.Customer;
import java.util.Collection;
import java.util.Objects;

public class CustomerDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private Long organizationId;

    @JsonIgnore
    private String password;

    public CustomerDetailsImpl(Long id, String email, String password, Long organizationId) {
        this.id = id;
        this.username = email;
        this.password = password;
        this.organizationId = organizationId;
    }

    public static CustomerDetailsImpl build(Customer admin) {
        return new CustomerDetailsImpl(
                admin.getId(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getOrganization_id());
    }


    public Long getId() {
        return id;
    }

    public Long getOrganizationIdId() {
        return organizationId;
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
        CustomerDetailsImpl admin = (CustomerDetailsImpl) o;
        return Objects.equals(id, admin.id);
    }

}
