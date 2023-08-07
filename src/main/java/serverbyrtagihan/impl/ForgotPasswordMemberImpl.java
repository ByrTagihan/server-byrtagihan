package serverbyrtagihan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.modal.Message;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.repository.MessageMemberRepository;
import serverbyrtagihan.service.ForgotPasswordService;

@Service
public class ForgotPasswordMemberImpl implements ForgotPasswordService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageMemberRepository messageRepository;

    @Override
    public Member findByHp(String hp) {
        return memberRepository.findByHp(hp);
    }

    public void forgotPassword(@RequestParam("hp")String hp) {

        // Cari user berdasarkan nomor telepon
        Member member = memberRepository.findByHp(hp);
        if (member != null) {
            // Generate kode reset password
            String resetCode = generateResetCode();

            // Simpan pesan di tabel "Message"
            Message message = new Message();
            message.setMember(member);
            message.setContent("Kode reset password Anda: " + resetCode);
            messageRepository.save(message);

            // Lakukan logika pengiriman pesan ke nomor telepon, seperti menggunakan layanan SMS gateway
            sendResetCodeViaSMS(hp, resetCode);
        } else {
            // Jika nomor telepon tidak ditemukan, lakukan penanganan kesalahan yang sesuai.
            // Contoh: lempar exception atau kirim pesan bahwa nomor telepon tidak valid.
            throw new NotFoundException("Nomor telepon tidak ditemukan.");
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
}