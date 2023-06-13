package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.UserPrinciple;
import serverbyrtagihan.Repository.ByrTagihanRepository;

import java.util.regex.Pattern;

@Service
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    private ByrTagihanRepository byrTagihanRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //        mengecek email
       boolean isEmail = Pattern.compile("^(.+)@(\\S+)$")
              .matcher(username).matches();
        ByrTagihan users = byrTagihanRepository.findByEmail(username);
        System.out.println("is Email " + isEmail);
        return UserPrinciple.build(users);
    }
}
