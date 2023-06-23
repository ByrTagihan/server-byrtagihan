package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import serverbyrtagihan.Modal.Member;
import serverbyrtagihan.Modal.Customer;
import serverbyrtagihan.Modal.User;
import serverbyrtagihan.repository.CustomerRepository;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {
    @Autowired
    CustomerRepository adminRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = adminRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomerDetailsImpl.build(customer);
    }

}
