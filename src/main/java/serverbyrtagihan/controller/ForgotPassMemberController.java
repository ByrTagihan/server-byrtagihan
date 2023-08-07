package serverbyrtagihan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.MemberDTO;
import serverbyrtagihan.impl.ForgotPasswordMemberImpl;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.response.CustomResponse;
import serverbyrtagihan.service.ForgotPasswordService;



@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class ForgotPassMemberController {

    @Autowired
    private ForgotPasswordMemberImpl forgotPasswordService;

    @Autowired
    private ForgotPasswordService forgotService;

    @PostMapping("/member/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody MemberDTO memberDTO) {

        // Ambil nomor telepon dari objek DTO
        String hp = memberDTO.getHp();

        Member member = forgotService.findByHp(hp);
        if (member != null) {
            // Jika pengguna ditemukan, lakukan proses pengiriman kode reset password
            // ... implementasi proses pengiriman kode reset password ...

            // Jika berhasil, buat objek CustomResponse dengan nilai yang sesuai
            int status = 200;
            String code = "SUCCESS";
            Object data = member;
            String message = "Kode reset telah dikirim ke nomor telepon Anda";

            CustomResponse response = new CustomResponse(status, code, data, message);

            // Kembalikan respons dengan objek CustomResponse sebagai body dan status 200 OK
            return ResponseEntity.ok(response);
        } else {
            // Jika pengguna tidak ditemukan, berikan respons error
            int status = 400;
            String code = "ERROR";
            Object data = null;
            String message = "User with the provided phone number not found.";

            CustomResponse response = new CustomResponse(status, code, data, message);

            // Kembalikan respons dengan objek CustomResponse sebagai body dan status 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
