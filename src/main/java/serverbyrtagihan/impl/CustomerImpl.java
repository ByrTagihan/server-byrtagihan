package serverbyrtagihan.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import serverbyrtagihan.modal.*;
import serverbyrtagihan.repository.*;
import serverbyrtagihan.dto.ForGotPass;
import serverbyrtagihan.dto.PasswordDTO;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.exception.NotFoundException;
import serverbyrtagihan.exception.VerificationCodeValidator;
import serverbyrtagihan.response.LoginRequest;
import serverbyrtagihan.response.SignupRequest;
import serverbyrtagihan.security.jwt.JwtUtils;
import serverbyrtagihan.service.CustomerService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CustomerImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private GetVerification getVerification;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    private VerificationCodeValidator verificationCodeValidator;


    private String newPassword() {
        Random random = new Random();
        String result = "";
        String character = "0123456789qwertyuiopasdfghjklzxcvbnm";
        for (int i = 0; i < 9; i++) {
            result += character.charAt(random.nextInt(character.length()));
        }
        return result;
    }

    private String code() {
        Random random = new Random();
        String result = "";
        String character = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM";
        for (int i = 0; i < 5; i++) {
            result += character.charAt(random.nextInt(character.length()));
        }
        return result;
    }


    private BindingResult validateVerificationCode(Reset_Password verificationCode) {
        // Membuat BindingResult untuk menampung hasil validasi
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(verificationCode, "verificationCode");

        // Validasi objek VerificationCode menggunakan validator
        verificationCodeValidator.validate(verificationCode, bindingResult);

        return bindingResult;
    }

    @Override
    public Page<Customer> getAll(String jwtToken, Long page, Long limit, String sort, String search) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sort.startsWith("-")) {
            sort = sort.substring(1);
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(limit), direction, sort);
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            if (search != null && !search.isEmpty()) {
                return customerRepository.findAllByKeyword(search, pageable);
            } else {
                return customerRepository.findAll(pageable);
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Map<Object, Object> login(LoginRequest loginRequest) {
        Customer customer = customerRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new NotFoundException("Username not found"));
        if (encoder.matches(loginRequest.getPassword(), customer.getPassword())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            customer.setLast_login(new Date());
            customerRepository.save(customer);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedLastLogin = sdf.format(customer.getLast_login());
            Map<Object, Object> response = new HashMap<>();
            response.put("data", customer);
            response.put("token", jwt);
            response.put("last_login", formattedLastLogin);
            response.put("type_token", "Customer");
            return response;
        }
        throw new NotFoundException("Password not found");
    }

    @Override
    public Map<String, Object> verificationPass(Reset_Password verification) throws MessagingException {
        String newPass = newPassword();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        if (getVerification.findByCode(verification.getCode()).isEmpty()) {
            throw new NotFoundException("Code not valid");
        }
        String token = verification.getCode();
        Customer customer = customerRepository.findByToken(token).orElseThrow(() -> new NotFoundException("code not found"));
        customer.setPassword(encoder.encode(newPass));
        helper.setTo(customer.getEmail());
        helper.setSubject("Password Baru");
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
                "                                <p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-family: Montserrat, sans-serif; font-size: 14px; line-height: 19.6px;\"><span style=\"font-size: 20px; line-height: 28px;\"><strong>Berikut password akun byrtagihan.com Anda:" + newPass + "</strong></span></span>\n" +
                "                                </p>\n" +
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
                "                                <p style=\"line-height: 140%; font-size: 14px;\">Anda dapat login melalui Aplikasi ByrTagihan menggunakan password di atas. Untuk keamanan identitas Anda, jangan pernah memberikan password kepada orang lain atau pihak lain.</p>\n" +
                "                              </div>\n" +
                "\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "\n" +
                "                      <table id=\"u_content_social_1\" class=\"u_content_social\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 25px 45px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                              <div align=\"left\">\n" +
                "                                <div style=\"display: table; max-width:155px;\">\n" +
                "\n" +
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
        customer.setToken("Kosong");
        customerRepository.save(customer);
        javaMailSender.send(message);
        Map<String, Object> res = new HashMap<>();
        res.put("new password" , newPass);
        return res;
    }

    @Override
    public ForGotPass sendEmail(ForGotPass forGotPass) throws MessagingException {
        String code = code();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        if (customerRepository.existsByEmail(forGotPass.getEmail())) {
            helper.setTo(forGotPass.getEmail());
            helper.setSubject("Konfirmasi Reset Password");
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
                    "                                <p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-family: Montserrat, sans-serif; font-size: 14px; line-height: 19.6px;\"><span style=\"font-size: 20px; line-height: 28px;\"><strong>Anda menerima pesan ini karena Anda menekan tombol LUPA PASSWORD.</strong></span></span>\n" +
                    "                                </p>\n" +
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
                    "                                <p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-size: 12px; line-height: 16.8px;\">Klik tombol di bawah untuk menyetel ulang password.</span></p>\n" +
                    "                                <p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-size: 12px; line-height: 16.8px;\">Jika Anda tidak memerlukan penyetelan ulang, abaikan pesan ini.</span></p>\n" +
                    "                                <p style=\"line-height: 140%; font-size: 14px;\">&nbsp;</p>\n" +
                    "                              </div>\n" +
                    "\n" +
                    "                            </td>\n" +
                    "                          </tr>\n" +
                    "                            <tr>\n" +
                    "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:32px 46px ;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "\n" +
                    "                              <div class=\"v-color v-text-align v-line-height\" style=\"color: #34495e; line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
                    "                                <p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-family: Montserrat, sans-serif; font-size: 14px; line-height: 19.6px;\"><span style=\"font-size: 20px; line-height: 28px;\"><strong>Berikut code verifikasi akun byrtagihan.com Anda: " + code + "</strong></span></span>\n" +
                    "                                </p>\n" +
                    "                              </div>\n" +
                    "\n" +
                    "                            </td>\n" +
                    "                          </tr>\n" +
                    "                        </tbody>\n" +
                    "                      </table>\n" +
                    "\n" +
                    "                      <table id=\"u_content_button_1\" class=\"u_content_button\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                    "                        <tbody>\n" +
                    "                          <tr>\n" +
                    "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 10px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "\n" +
                    "                              <div class=\"v-text-align\" align=\"left\">\n" +
                    "                                <!--[if mso]><table width=\"100%\"           cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;font-family:arial,helvetica,sans-serif;\"><tr><td class=\"v-text-align v-button-colors\" style=\"font-family:arial,helvetica,sans-serif;\" align=\"left\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"\" style=\"height:37px; v-text-anchor:middle; width:178px;\" arcsize=\"16%\" stroke=\"f\" fillcolor=\"#00afef\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:arial,helvetica,sans-serif;\"><![endif]-->\n" +
                    "                              \n" +
                    "                                <!--[if mso]></center></v:roundrect></td></tr></table><![endif]-->\n" +
                    "                              </div>\n" +
                    "\n" +
                    "                            </td>\n" +
                    "                          </tr>\n" +
                    "                        </tbody>\n" +
                    "                      </table>\n" +
                    "\n" +
                    "                      <table id=\"u_content_social_1\" class=\"u_content_social\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                    "                        <tbody>\n" +
                    "                          <tr>\n" +
                    "                            <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 25px 45px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "\n" +
                    "                              <div align=\"left\">\n" +
                    "                                <div style=\"display: table; max-width:155px;\">\n" +
                    "                                  \n" +
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
            if (customerRepository.existsByEmail(forGotPass.getEmail())) {
                Customer customer = customerRepository.findByEmail(forGotPass.getEmail()).get();
                customer.setToken(code);
                if (getVerification.findByEmail(forGotPass.getEmail()).isPresent()) {
                    Reset_Password pass = getVerification.findByEmail(customer.getEmail()).orElseThrow(() -> new NotFoundException("Email not found"));
                    pass.setEmail(forGotPass.getEmail());
                    customer.setToken(code);
                    pass.setCode(code);
                    getVerification.save(pass);
                    customerRepository.save(customer);
                } else {
                    Reset_Password pass = new Reset_Password();
                    pass.setEmail(forGotPass.getEmail());
                    pass.setCode(code);
                    getVerification.save(pass);
                }
            }
            javaMailSender.send(message);
        } else {
            throw new NotFoundException("Email not found");
        }
        return forGotPass;
    }

    @Override
    public Customer getProfileCustomer(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            return customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Id Not Found"));
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public Customer post(SignupRequest signupRequest, String jwtToken) throws MessagingException {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            String email = signupRequest.getEmail();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
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
                    "                                <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 20px; line-height: 28px;\"><strong><span style=\"font-family: Montserrat, sans-serif; line-height: 28px; font-size: 20px;\">" + signupRequest.getName() + ",</span></strong>\n" +
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
                    "                                <p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-size: 12px; line-height: 16.8px;\">Username:</span>" + signupRequest.getEmail() + "</p>\n" +
                    "                                <p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-size: 12px; line-height: 16.8px;\">Password:</span>" + signupRequest.getPassword() + "</p>\n" +
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
            if (customerRepository.existsByEmail(signupRequest.getEmail())) {
                throw new NotFoundException(" Email telah digunakan!");
            }
            String UserEmail = signupRequest.getEmail().trim();
            boolean EmailIsNotValid = !UserEmail.matches("^(.+)@(\\S+)$");
            if (EmailIsNotValid) {
                throw new BadRequestException(" Email tidak valid");
            }
            String UserPassword = signupRequest.getPassword().trim();
            boolean PasswordIsNotValid = !UserPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");
            if (PasswordIsNotValid) {
                throw new BadRequestException(" Password tidak valid");
            }
            // Create new user's account
            Customer admin = new Customer();
            admin.setEmail(signupRequest.getEmail());
            admin.setPassword(encoder.encode(signupRequest.getPassword()));
            admin.setActive(signupRequest.isActive());
            admin.setHp(signupRequest.getHp());
            admin.setName(signupRequest.getName());
            admin.setAddress(signupRequest.getAddress());
            admin.setOrganization_id(null);
            admin.setToken("Kosong");
            javaMailSender.send(message);
            return customerRepository.save(admin);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }


    @Override
    public Customer put(Customer customer, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Customer update = customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Id Not Found"));
            update.setName(customer.getName());
            update.setAddress(customer.getAddress());
            update.setHp(customer.getHp());
            update.setPicture(customer.getPicture());
            return customerRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Customer put2(Customer customer, String jwtToken, Long id) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            Customer update = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
            update.setOrganization_id(customer.getOrganization_id());
            update.setName(customer.getName());
            update.setAddress(customer.getAddress());
            update.setHp(customer.getHp());
            update.setPassword(encoder.encode(customer.getPassword()));
            update.setActive(customer.isActive());
            String UserPassword = customer.getPassword().trim();
            boolean PasswordIsNotValid = !UserPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");
            if (PasswordIsNotValid) {
                throw new BadRequestException("Kesalahan: Password tidak valid");
            }
            return customerRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Customer putPassword(PasswordDTO passwordDTO, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Customer update = customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Id Not Found"));
            boolean conPassword = encoder.matches(passwordDTO.getOld_password(), update.getPassword());
            if (conPassword) {
                if (passwordDTO.getNew_password().equals(passwordDTO.getConfirm_new_password())) {
                    update.setPassword(encoder.encode(passwordDTO.getNew_password()));
                    return customerRepository.save(update);
                } else {
                    throw new BadRequestException("Password tidak sesuai");
                }
            } else {
                throw new NotFoundException("Password lama tidak sesuai");
            }
        } else {
            throw new BadRequestException("Token not valid");
        }
    }

    @Override
    public Customer putPicture(Customer customer, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        String typeToken = claims.getAudience();
        if (typeToken.equals("Customer")) {
            Customer update = customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Id Not Found"));
            update.setPicture(customer.getPicture());
            return customerRepository.save(update);
        } else {
            throw new BadRequestException("Token not valid");
        }
    }


    @Override
    public Map<String, Boolean> delete(Long id, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        if (typeToken.equals("User")) {
            try {
                customerRepository.deleteById(id);
                Map<String, Boolean> res = new HashMap<>();
                res.put("Deleted", Boolean.TRUE);
                return res;
            } catch (Exception e) {
                throw new NotFoundException("Customer not found");
            }
        } else {
            throw new BadRequestException("Token not valid");
        }

    }

    @Override
    public Map<String, Integer> getRecapTotal(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String typeToken = claims.getAudience();
        String organizationId = claims.getId();

        if (typeToken.equals("Customer")) {
            List<Member> members = memberRepository.findByOrganizationId(organizationId);
            List<Bill> bills = billRepository.findByOrganizationId(organizationId);
            List<Transaction> transactions = transactionRepository.findByOrganizationId(organizationId);

            Map<String, Integer> res = new HashMap<>();
            res.put("member", members.size());
            res.put("bill", bills.size());
            res.put("transaction", transactions.size());

            return res;
        } else {
            throw new BadRequestException("Token not valid");
        }
    }


}
