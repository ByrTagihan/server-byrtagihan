package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.Modal.MemberLogin;
import serverbyrtagihan.Modal.MemberPrinciple;
import serverbyrtagihan.Repository.ByrTagihanRepository;
import serverbyrtagihan.Repository.MemberLoginRepository;

import javax.transaction.Transactional;

@Service
public class MemberDetails implements UserDetailsService {
    @Autowired
    MemberLoginRepository adminRepository;
    @Autowired
    private ByrTagihanRepository byrTagihanRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ByrTagihan users = byrTagihanRepository.findByEmail(username);
        MemberLogin admin = adminRepository.memberByUnique(username);
        return MemberPrinciple.build(admin , users);
    }


}