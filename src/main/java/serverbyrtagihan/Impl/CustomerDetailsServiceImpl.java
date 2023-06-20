package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import serverbyrtagihan.Repository.CustomerRepository;
import serverbyrtagihan.swagger.Modal.Customer;
import serverbyrtagihan.Repository.UserRepository;
import serverbyrtagihan.swagger.Modal.User;

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
        Customer admin = adminRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomerDetailsImpl.build(admin);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        // Implement the logic to load user details from the database based on the provided email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Create an instance of UserDetails using the retrieved user details
        return UserDetailsImpl.buildUser(user);
    }

}
