package serverbyrtagihan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.User;
import serverbyrtagihan.modal.Customer;
import serverbyrtagihan.repository.CustomerRepository;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.repository.UserRepository;
import serverbyrtagihan.exception.NotFoundException;

import javax.transaction.Transactional;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        if (memberRepository.findByUniqueId(username).isPresent()) {
            Member member = memberRepository.findByUniqueId(username).orElseThrow(() -> new NotFoundException("Username not found"));;
            return MemberDetailsImpl.buildMember(member);
        } else if (userRepository.existsByEmail(username)){
            User user = userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("Username not found"));;
            return UserDetailsImpl.buildUser(user);
        } else if (customerRepository.existsByEmail(username)) {
            Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("Username not found"));;
            return CustomerDetailsImpl.build(customer);
        }
        throw new NotFoundException("User Not Found with username: " + username);
    }

}
