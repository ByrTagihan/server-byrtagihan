package serverbyrtagihan.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.Repository.MemberRepository;
import serverbyrtagihan.Service.MemberService;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.swagger.Modal.Member;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MemberImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Member add(Member member ) {

        Member member1 = new Member();
        member1.setName(member.getName());
        member1.setAddres(member.getAddres());
        member1.setHp(member.getHp());
        member1.setPassword(member.getPassword());
        return memberRepository.save(member1);
    }



    @Override
    public Member getById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member put(Member member, Long id) {
        Member update = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
        update.setName(member.getName());
        update.setAddres(member.getAddres());
        update.setHp(member.getHp());
        update.setPassword(member.getPassword());

        return memberRepository.save(update);
    }


    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            memberRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted" , Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }

    }
}
