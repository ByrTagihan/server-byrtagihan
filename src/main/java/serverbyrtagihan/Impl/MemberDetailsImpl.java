package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.MemberLogin;
import serverbyrtagihan.Modal.MemberPrinciple;
import serverbyrtagihan.Modal.UserPrinciple;
import serverbyrtagihan.Repository.MemberLoginRepository;

@Service
public class MemberDetailsImpl implements UserDetailsService {

    @Autowired
    MemberLoginRepository memberLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberLogin users = memberLoginRepository.memberByUnique(username);
        return MemberPrinciple.build(users);
    }
}
