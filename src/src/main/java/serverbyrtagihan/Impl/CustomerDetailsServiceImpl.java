package serverbyrtagihan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import serverbyrtagihan.modal.User;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.repository.CustomerRepository;
import serverbyrtagihan.repository.UserRepository;
import serverbyrtagihan.exception.NotFoundException;

import javax.transaction.Transactional;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {
    @Autowired
    CustomerRepository adminRepository;

    @Autowired
     UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User users = userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("User Not Found with username"));
        Customer admin = adminRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomerDetailsImpl.build(admin , users);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        // Implement the logic to load user details from the database based on the provided email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Create an instance of UserDetails using the retrieved user details
        return UserDetailsImpl.buildUser(user);
    }

}
