package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import serverbyrtagihan.Modal.ByrTagihan;

import serverbyrtagihan.Repository.ByrTagihanRepository;
import serverbyrtagihan.Repository.CustomerRepository;
import serverbyrtagihan.swagger.Modal.Customer;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {
    @Autowired
    CustomerRepository adminRepository;
    @Autowired
    private ByrTagihanRepository byrTagihanRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ByrTagihan users = byrTagihanRepository.findByEmail(username);
        Customer admin = adminRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomerDetailsImpl.build(admin , users);
    }


}
