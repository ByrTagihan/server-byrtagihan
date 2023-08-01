package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.exception.BadRequestException;
import org.springframework.data.domain.Page;
import serverbyrtagihan.repository.MemberRepository;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.response.*;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.MemberService;
import serverbyrtagihan.util.Pagination;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class MemberController {
    @Autowired
    private MemberService service;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MemberService memberService;


    private static final String JWT_PREFIX = "jwt ";


    @PostMapping("/member/login")
    public CommonResponse<?> authenticate(@RequestBody LoginMemberRequest loginRequest) {
        Member member = memberRepository.findByUniqueId(loginRequest.getUniqueId()).orElseThrow(() -> new NotFoundException("UniqueId not found"));
        boolean conPassword = encoder.matches(loginRequest.getPassword(), member.getPassword());
        if (conPassword) {
            String token = jwtUtils.generateTokenMember(member.getUniqueId());
            Map<Object, Object> response = new HashMap<>();
            response.put("data", member);
            response.put("token", token);
            response.put("type-token", "Member");
            return ResponseHelper.ok(response);
        } else {
            throw new NotFoundException("Password not valid");
        }
    public CommonResponse<?> authenticate( @RequestBody LoginMember loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUniqueId(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateTokenMember(authentication);

        Member member = memberRepository.findByUniqueId(loginRequest.getUniqueId()).orElseThrow(() -> new NotFoundException("Username not found"));
        Map<Object, Object> response = new HashMap<>();
        response.put("data", member);
        response.put("token", jwt);
        response.put("type-token", "Member");
        return ResponseHelper.ok(response);
    }


    @PostMapping("/member/register")
    public CommonResponse<?> registerUser(@Valid @RequestBody SignUpMemberRequest signUpMemberRequest) throws MessagingException {
        String uniqueId = signUpMemberRequest.getUniqueId();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(uniqueId);
        helper.setSubject("Selamat Datang");
        helper.setText("", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "\n" +
                "<head>\n" +
                "  <!--[if gte mso 9]>\n" +
                "<xml>\n" +
                "  <o:OfficeDocumentSettings>\n" +
                "    <o:AllowPNG/>\n" +
                "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "  </o:OfficeDocumentSettings>\n" +
                "</xml>\n" +
                "<![endif]-->\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "  <!--[if !mso]><!-->\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "  <!--<![endif]-->\n" +
                "  <title></title>\n" +
                "\n" +
                "  <style type=\"text/css\">\n" +
                "    a {\n" +
                "      color: #0000ee;\n" +
                "      text-decoration: underline;\n" +
                "    }\n" +
                "\n" +
                "    @media only screen and (min-width: 520px) {\n" +
                "      .u-row {\n" +
                "        width: 500px !important;\n" +
                "      }\n" +
                "      .u-row .u-col {\n" +
                "        vertical-align: top;\n" +
                "      }\n" +
                "      .u-row .u-col-100 {\n" +
                "        width: 500px !important;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    @media (max-width: 520px) {\n" +
                "      .u-row-container {\n" +
                "        max-width: 100% !important;\n" +
                "        padding-left: 0px !important;\n" +
                "        padding-right: 0px !important;\n" +
                "      }\n" +
                "      .u-row .u-col {\n" +
                "        min-width: 320px !important;\n" +
                "        max-width: 100% !important;\n" +
                "        display: block !important;\n" +
                "      }\n" +
                "      .u-row {\n" +
                "        width: calc(100% - 40px) !important;\n" +
                "      }\n" +
                "      .u-col {\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "      .u-col>div {\n" +
                "        margin: 0 auto;\n" +
                "      }\n" +
                "      .no-stack .u-col {\n" +
                "        min-width: 0 !important;\n" +
                "        display: table-cell !important;\n" +
                "      }\n" +
                "      .no-stack .u-col-100 {\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    body {\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "\n" +
                "    table,\n" +
                "    tr,\n" +
                "    td {\n" +
                "      vertical-align: top;\n" +
                "      border-collapse: collapse;\n" +
                "    }\n" +
                "\n" +
                "    p {\n" +
                "      margin: 0;\n" +
                "    }\n" +
                "\n" +
                "    .ie-container table,\n" +
                "    .mso-container table {\n" +
                "      table-layout: fixed;\n" +
                "    }\n" +
                "\n" +
                "    * {\n" +
                "      line-height: inherit;\n" +
                "    }\n" +
                "\n" +
                "    a[x-apple-data-detectors='true'] {\n" +
                "      color: inherit !important;\n" +
                "      text-decoration: none !important;\n" +
                "    }\n" +
                "\n" +
                "    @media (max-width: 480px) {\n" +
                "      .hide-mobile {\n" +
                "        display: none !important;\n" +
                "        max-height: 0px;\n" +
                "        overflow: hidden;\n" +
                "      }\n" +
                "      .hide-desktop {\n" +
                "        display: block !important;\n" +
                "      }\n" +
                "    }\n" +
                "  </style>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <!--[if !mso]><!-->\n" +
                "  <link href=\"https://fonts.googleapis.com/css?family=Montserrat:400,700\" rel=\"stylesheet\" type=\"text/css\">\n" +
                "  <!--<![endif]-->\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"clean-body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #e7e7e7\">\n" +
                "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\n" +
                "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
                "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #e7e7e7;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
                "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #e7e7e7;\"><![endif]-->\n" +
                "\n" +
                "\n" +
                "          <div id=\"u_row_1\" class=\"u-row-container v-row-padding--vertical v-row-background-image--outer v-row-background-color\" style=\"padding: 0px;background-color: transparent\">\n" +
                "            <div class=\"u-row v-row-columns-background-color-background-color\" style=\"Margin: 0 auto;min-width: 320px;max-width: 500px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "              <div class=\"v-row-background-image--inner\" style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\n" +
                "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td class=\"v-row-padding v-row-background-image--outer v-row-background-color\" style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:500px;\"><tr class=\"v-row-background-image--inner v-row-columns-background-color-background-color\" style=\"background-color: transparent;\"><![endif]-->\n" +
                "\n" +
                "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"500\" class=\"v-col-padding v-col-background-color v-col-border\" style=\"background-color: #ffffff;width: 500px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                "                <div id=\"u_column_1\" class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 500px;display: table-cell;vertical-align: top;\">\n" +
                "                  <div class=\"v-col-background-color\" style=\"background-color: #ffffff;width: 100% !important;\">\n" +
                "                    <!--[if (!mso)&(!IE)]><!-->\n" +
                "                    <div class=\"v-col-padding v-col-border\" style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
                "                      <!--<![endif]-->\n" +
                "\n" +
                "                      <table id=\"u_content_image_1\" class=\"u_content_image\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 10px 10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                              <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "                                <tr>\n" +
                "                                  <td class=\"v-text-align\" style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
                "\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "\n" +
                "                      <table id=\"u_content_divider_1\" class=\"u_content_divider\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:13px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                              <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"87%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 2px solid #c2e0f4;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "                                <tbody>\n" +
                "                                  <tr style=\"vertical-align: top\">\n" +
                "                                    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "                                      <span>&#160;</span>\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                </tbody>\n" +
                "                              </table>\n" +
                "\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "\n" +
                "                      <!--[if (!mso)&(!IE)]><!-->\n" +
                "                    </div>\n" +
                "                    <!--<![endif]-->\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "                <!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "          <div id=\"u_row_2\" class=\"u-row-container v-row-padding--vertical v-row-background-image--outer v-row-background-color\" style=\"padding: 0px;background-color: transparent\">\n" +
                "            <div class=\"u-row v-row-columns-background-color-background-color\" style=\"Margin: 0 auto;min-width: 320px;max-width: 500px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "              <div class=\"v-row-background-image--inner\" style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\n" +
                "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td class=\"v-row-padding v-row-background-image--outer v-row-background-color\" style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:500px;\"><tr class=\"v-row-background-image--inner v-row-columns-background-color-background-color\" style=\"background-color: transparent;\"><![endif]-->\n" +
                "\n" +
                "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"500\" class=\"v-col-padding v-col-background-color v-col-border\" style=\"background-color: #ffffff;width: 500px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                "                <div id=\"u_column_2\" class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 500px;display: table-cell;vertical-align: top;\">\n" +
                "                  <div class=\"v-col-background-color\" style=\"background-color: #ffffff;width: 100% !important;\">\n" +
                "                    <!--[if (!mso)&(!IE)]><!-->\n" +
                "                    <div class=\"v-col-padding v-col-border\" style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
                "                      <!--<![endif]-->\n" +
                "\n" +
                "                      <table id=\"u_content_text_1\" class=\"u_content_text\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:32px 46px 10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                              <div class=\"v-color v-text-align v-line-height\" style=\"color: #34495e; line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
                "                                <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 20px; line-height: 28px;\"><strong><span style=\"font-family: Montserrat, sans-serif; line-height: 28px; font-size: 20px;\">" + signUpMemberRequest.getName() + ",</span></strong>\n" +
                "                                  </span><span style=\"font-size: 20px; line-height: 28px;\"><strong><span style=\"font-family: Montserrat, sans-serif; line-height: 28px; font-size: 20px;\">&nbsp;akun</span></strong>\n" +
                "                                  </span><strong style=\"font-size: 20px;\"><span style=\"font-family: Montserrat, sans-serif; line-height: 28px; font-size: 20px;\"> byrtagihan.com Anda telah berhasil dibuat.</span></strong></p>\n" +
                "                              </div>\n" +
                "\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "\n" +
                "                      <table id=\"u_content_text_3\" class=\"u_content_text\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:32px 46px 10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                              <div class=\"v-color v-text-align v-line-height\" style=\"color: #34495e; line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
                "                                <p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-size: 12px; line-height: 16.8px;\">Username:</span>" + signUpMemberRequest.getUniqueId() + "</p>\n" +
                "                                <p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-size: 12px; line-height: 16.8px;\">Password:</span>" + signUpMemberRequest.getPassword() + "</p>\n" +
                "                                <p style=\"line-height: 140%; font-size: 14px;\">&nbsp;</p>\n" +
                "                                <p style=\"line-height: 140%; font-size: 14px;\">Selamat menggunakan aplikasi byrtagihan.com!</p>\n" +
                "                                <p style=\"line-height: 140%; font-size: 14px;\">Semoga kita semua dapat #BerkreasiUntukSesama.</p>\n" +
                "                                <p style=\"line-height: 140%; font-size: 14px;\">&nbsp;</p>\n" +
                "                              </div>\n" +
                "\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "                      <table id=\"u_content_social_1\" class=\"u_content_social\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 25px 45px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                              <div align=\"left\">\n" +
                "                                <div style=\"display: table; max-width:155px;\">\n" +
                "                                  <!--[if (mso)|(IE)]><table width=\"155\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"border-collapse:collapse;\" align=\"left\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse; mso-table-lspace: 0pt;mso-table-rspace: 0pt; width:155px;\"><tr><![endif]-->\n" +
                "\n" +
                "\n" +
                "                                  <!--[if (mso)|(IE)]><td width=\"32\" style=\"width:32px; padding-right: 7px;\" valign=\"top\"><![endif]-->\n" +
                "\n" +
                "                                  <!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "\n" +
                "\n" +
                "                                  <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "                                </div>\n" +
                "                              </div>\n" +
                "\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "\n" +
                "                      <!--[if (!mso)&(!IE)]><!-->\n" +
                "                    </div>\n" +
                "                    <!--<![endif]-->\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "                <!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "          <div id=\"u_row_3\" class=\"u-row-container v-row-padding--vertical v-row-background-image--outer v-row-background-color\" style=\"padding: 0px;background-color: transparent\">\n" +
                "            <div class=\"u-row v-row-columns-background-color-background-color\" style=\"Margin: 0 auto;min-width: 320px;max-width: 500px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "              <div class=\"v-row-background-image--inner\" style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\n" +
                "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td class=\"v-row-padding v-row-background-image--outer v-row-background-color\" style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:500px;\"><tr class=\"v-row-background-image--inner v-row-columns-background-color-background-color\" style=\"background-color: transparent;\"><![endif]-->\n" +
                "\n" +
                "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"500\" class=\"v-col-padding v-col-background-color v-col-border\" style=\"background-color: #34495e;width: 500px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                "                <div id=\"u_column_3\" class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 500px;display: table-cell;vertical-align: top;\">\n" +
                "                  <div class=\"v-col-background-color\" style=\"background-color: #34495e;width: 100% !important;\">\n" +
                "                    <!--[if (!mso)&(!IE)]><!-->\n" +
                "                    <div class=\"v-col-padding v-col-border\" style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
                "                      <!--<![endif]-->\n" +
                "\n" +
                "                      <table id=\"u_content_text_4\" class=\"u_content_text\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 45px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                              <div class=\"v-color v-text-align v-line-height\" style=\"color: #ffffff; line-height: 100%; text-align: left; word-wrap: break-word;\">\n" +
                "                                <p style=\"font-size: 14px; line-height: 100%;\">&nbsp;</p>\n" +
                "                                <p style=\"font-size: 14px; line-height: 100%;\">&nbsp;</p>\n" +
                "                                <p style=\"font-size: 14px; line-height: 100%; text-align: center;\"><span style=\"font-size: 8px; line-height: 8px;\">CONFIDENTIALITY NOTICE: The content of this email and any files transmitted with it are confidential and intended solely for the individual or entity to whom they are addressed. If the reader is not the named recipient or an authorized representative of the intended recipient, you are hereby notified that any use, dissemination, distribution, copying, or printing of any parts of this communication is strictly prohibited. If you have received this message in error, please notify the sender immediately and followed by its immediate deletion. Our company accepts no liability for the content of this email, and/or the consequences of any actions taken on the basis of the information provided, unless that information is subsequently confirmed in writing. Thank you for your cooperation and understanding.</span></p>\n" +
                "                                <p style=\"font-size: 14px; line-height: 100%; text-align: center;\">&nbsp;</p>\n" +
                "                                <p style=\"font-size: 14px; line-height: 100%; text-align: center;\">&nbsp;</p>\n" +
                "                                <p style=\"font-size: 14px; line-height: 100%; text-align: center;\"><span style=\"font-size: 8px; line-height: 8px;\">You are receiving this email because you have visited our site and/or requested to join our community.<br /><br /></span></p>\n" +
                "                              </div>\n" +
                "\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "\n" +
                "                      <!--[if (!mso)&(!IE)]><!-->\n" +
                "                    </div>\n" +
                "                    <!--<![endif]-->\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "                <!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "\n" +
                "          <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <!--[if mso]></div><![endif]-->\n" +
                "  <!--[if IE]></div><![endif]-->\n" +
                "</body>\n" +
                "\n" +
                "</html>");
        if (memberRepository.existsByUniqueId(signUpMemberRequest.getUniqueId())) {
            throw new BadRequestException("Unique id is already");
        }

        String MemberPassword = signUpMemberRequest.getPassword().trim();
        boolean PasswordIsNotValid = !MemberPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");
        if (PasswordIsNotValid) {
            throw new BadRequestException("Password not valid");
        }
        // Create new member's account
        Member member = new Member();
        member.setUniqueId(signUpMemberRequest.getUniqueId());
        member.setPassword(encoder.encode(signUpMemberRequest.getPassword()));
        member.setActive(signUpMemberRequest.isActive());
        member.setHp(signUpMemberRequest.getHp());
        member.setName(signUpMemberRequest.getName());
        member.setAddress(signUpMemberRequest.getAddres());
        member.setToken("Kosong");
        memberRepository.save(member);

//        javaMailSender.send(message);
        return ResponseHelper.ok(new MessageResponse(" Register telah berhasil! "));

    }

    @PostMapping("/customer/member")
    public CommonResponse<Member> registerMember (@RequestBody Member member, HttpServletRequest request){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.add(member, jwtToken));
    }

    @PostMapping("/user/member")
    public CommonResponse<Member> registerMemberInUser (@RequestBody Member member, HttpServletRequest request){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.addInUser(member, jwtToken));
    }


    @GetMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> getByID (@PathVariable("id") Long id, HttpServletRequest request){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.getById(id, jwtToken));
    }

    @GetMapping(path = "/user/member/{id}")
    public CommonResponse<Member> getByIDInUser (@PathVariable("id") Long id, HttpServletRequest request){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.getByIdInUser(id, jwtToken));
    }

    @GetMapping(path = "/customer/member")
    public PaginationResponse<List<Member>> getAll (
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        Page<Member> channelPage;

        if (search != null && !search.isEmpty()) {
            channelPage = service.getAll(jwtToken, page, limit, sort, search);
        } else {
            channelPage = service.getAll(jwtToken, page, limit, sort, null);
        }

        List<Member> channels = channelPage.getContent();
        long totalItems = channelPage.getTotalElements();
        int totalPages = channelPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(channels, pagination);
    }

    @GetMapping(path = "/user/member")
    public PaginationResponse<List<Member>> getAllInUser (
            HttpServletRequest request,
            @RequestParam(defaultValue = Pagination.page, required = false) Long page,
            @RequestParam(defaultValue = Pagination.limit, required = false) Long limit,
            @RequestParam(defaultValue = Pagination.sort, required = false) String sort,
            @RequestParam(required = false) String search
    ){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        Page<Member> channelPage;

        if (search != null && !search.isEmpty()) {
            channelPage = service.getAllInUser(jwtToken, page, limit, sort, search);
        } else {
            channelPage = service.getAllInUser(jwtToken, page, limit, sort, null);
        }

        List<Member> channels = channelPage.getContent();
        long totalItems = channelPage.getTotalElements();
        int totalPages = channelPage.getTotalPages();

        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("total", (int) totalItems);
        pagination.put("page", Math.toIntExact(page));
        pagination.put("total_page", totalPages);

        return ResponseHelper.okWithPagination(channels, pagination);
    }

    @GetMapping(path = "/member/profile")
    public CommonResponse<Member> getById(HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(memberService.getProfileMember(jwtToken));
    }

    @PutMapping(path = "/member/update{id}")
    public CommonResponse<Member> update(@PathVariable("id") Long id, @RequestBody MemberDTO update, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(memberService.update(id, update, jwtToken));
    }


    @PutMapping(path = "/customer/member/{id}")
    public CommonResponse<Member> put (@PathVariable("id") Long id, @RequestBody MemberDTO memberDTO, HttpServletRequest request){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.put(modelMapper.map(memberDTO, Member.class), id, jwtToken));
    }
    @PutMapping(path = "/user/member/{id}")
    public CommonResponse<Member> putInUser (@PathVariable("id") Long id, @RequestBody MemberDTO
            memberDTO, HttpServletRequest request){
    public CommonResponse<Member> putInUser(@PathVariable("id") Long id, @RequestBody MemberUserDto memberDTO, HttpServletRequest request) {
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.putInUser(modelMapper.map(memberDTO, Member.class), id, jwtToken));
    }
    @PutMapping(path = "/customer/member/{id}/password")
    public CommonResponse<Member> putPass (@PathVariable("id") Long id, @RequestBody Password
            memberDTO, HttpServletRequest request){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.putPassword(modelMapper.map(memberDTO, Member.class), id, jwtToken));
    }

    @DeleteMapping(path = "/customer/member/{id}")
    public CommonResponse<?> delete (@PathVariable("id") Long id, HttpServletRequest request){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.delete(id, jwtToken));
    }
    @DeleteMapping(path = "/user/member/{id}")
    public CommonResponse<?> deleteInUser (@PathVariable("id") Long id, HttpServletRequest request){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.deleteInUser(id, jwtToken));
    }
    @PutMapping(path = "/member/password")
    public CommonResponse<Member> putPassword (@RequestBody PasswordDTO password, HttpServletRequest request){
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(service.putPass(password, jwtToken));
    }





}
