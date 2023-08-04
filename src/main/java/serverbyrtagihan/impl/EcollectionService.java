package serverbyrtagihan.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import serverbyrtagihan.dto.BNIRequestDTO;
import serverbyrtagihan.dto.EcollectionDTO;
import serverbyrtagihan.dto.ReportBill;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.modal.Member;
import serverbyrtagihan.security.jwt.JwtUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EcollectionService {
    private final RestTemplate restTemplate;

    @Autowired
    public EcollectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> sendPayloadToEcollection(String apiUrl, EcollectionDTO payload, String jwtToken) {
        try {
            Claims claims = JwtUtils.decodeJwt(jwtToken);
            String typeToken = claims.getAudience();

            if (typeToken.equals("User")) {

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                payload.setDatetime_expired(new Date(System.currentTimeMillis() + 3600 * 1000));

                ObjectMapper objectMapper = new ObjectMapper();
                String payloadJson = objectMapper.writeValueAsString(payload);

                System.out.println(payload.getDatetime_expired());

                BniEncryption hash = new BniEncryption();
                String cid = "99129"; // from BNI
                String key = "6ae8bbf31b9629a62940aab16ca386cc"; // from BNI
                String prefix = "988"; // from BNI

                String va_number = prefix + cid + "69813549";

                String parsedData = hash.hashData(payloadJson, cid, key);
                String decodeData = hash.parseData(parsedData, cid, key);
                System.out.println(parsedData);
                System.out.println(decodeData);
                System.out.println(va_number);

                BNIRequestDTO bniRequestDTO = new BNIRequestDTO();
                bniRequestDTO.setClient_id(cid);
                bniRequestDTO.setPrefix(prefix);
                bniRequestDTO.setData(parsedData);


                HttpEntity<BNIRequestDTO> requestEntity = new HttpEntity<>(bniRequestDTO, headers);
                System.out.println("Percobaan");
                return restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
            } else {
                throw new BadRequestException("Token not valid");
            }
        } catch (Exception e) {
            e.printStackTrace();

            // If an exception occurs, return an appropriate ResponseEntity indicating the failure.
            // You can customize the response based on your requirements.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send payload to Ecollection: " + e.getMessage());
        }
    }
}
