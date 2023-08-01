package serverbyrtagihan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serverbyrtagihan.dto.MemberDTO;
import serverbyrtagihan.dto.MessageMemberDTO;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.MessageMember;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.repository.MessageMemberRepository;

@Service
public class ForgotPasswordMemberImpl {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageMemberRepository messageRepository;

    public void forgotPassword(String hp) {

        // Cari user berdasarkan nomor telepon
        Member member = memberRepository.findByHp(hp);
        if (member != null) {
            // Generate kode reset password
            String resetCode = generateResetCode();

            // Simpan pesan di tabel "Message"
            MessageMember messageMember = new MessageMember();
            messageMember.setMember(member);
            messageMember.setContent("Kode reset password Anda: " + resetCode);
            messageRepository.save(messageMember);

            // Lakukan logika pengiriman pesan ke nomor telepon, seperti menggunakan layanan SMS gateway
            sendResetCodeViaSMS(hp, resetCode);
        } else {
            // Jika nomor telepon tidak ditemukan, lakukan penanganan kesalahan yang sesuai.
            // Contoh: lempar exception atau kirim pesan bahwa nomor telepon tidak valid.
            throw new RuntimeException("Nomor telepon tidak valid.");
        }
    }

    private String generateResetCode() {
        // Implementasikan metode untuk menghasilkan kode reset yang unik, seperti menggunakan UUID atau sejenisnya.
        // Anda juga bisa mengatur batas waktu kode reset berlaku di sini.
        return "YOUR_GENERATED_RESET_CODE";
    }

    private void sendResetCodeViaSMS(String hp, String resetCode) {
        // Implementasikan logika pengiriman SMS ke nomor telepon tertentu menggunakan layanan SMS gateway.
        // Contoh: kirim HTTP request ke layanan SMS gateway yang terintegrasi dengan aplikasi Anda.
        System.out.println("Mengirim SMS ke " + hp + ": " + resetCode);
    }

    // Konversi entitas "Member" dan "Message" menjadi DTO
    private MemberDTO convertToMemberDTO(MemberDTO member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setHp(member.getHp());
        return memberDTO;
    }

    private MessageMemberDTO convertToMessageMemberDTO(MessageMember messageMember) {
        MessageMemberDTO messageDTO = new MessageMemberDTO();
        messageDTO.setContent(messageMember.getContent());
        return messageDTO;
    }
}