package serverbyrtagihan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import serverbyrtagihan.Repository.CustomerRepository;
import serverbyrtagihan.Repository.MemberRepository;
import serverbyrtagihan.Repository.UserRepository;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.User;

import javax.transaction.Transactional;

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
