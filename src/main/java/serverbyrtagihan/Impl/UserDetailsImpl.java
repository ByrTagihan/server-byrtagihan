package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.UserPrinciple;
import serverbyrtagihan.Repository.ByrTagihanRepository;
import serverbyrtagihan.Repository.MemberLoginRepository;

import java.util.regex.Pattern;

@Service
@Primary
@Component("user1")
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
     ByrTagihanRepository byrTagihanRepository;

    @Override
    @Qualifier("user")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ByrTagihan users = byrTagihanRepository.findByEmail(username);
        return UserPrinciple.build(users);
    }
}
