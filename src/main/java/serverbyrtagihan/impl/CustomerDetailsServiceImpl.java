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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
       Customer admin = adminRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("Email not found"));
        return CustomerDetailsImpl.build(admin);
    }

}
