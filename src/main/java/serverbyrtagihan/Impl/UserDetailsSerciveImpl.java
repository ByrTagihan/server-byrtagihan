package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import serverbyrtagihan.Modal.User;
import serverbyrtagihan.Repository.UserRepository;
import serverbyrtagihan.exception.NotFoundException;

public class UserDetailsSerciveImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User users = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username + " + username));
        return UserDetailsImpl.buildUser(users);
    }
}
