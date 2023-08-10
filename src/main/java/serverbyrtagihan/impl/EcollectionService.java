package serverbyrtagihan.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import serverbyrtagihan.dto.BNIRequestDTO;
import serverbyrtagihan.dto.EcollectionDTO;
import serverbyrtagihan.dto.EcollectionResponseDTO;
import serverbyrtagihan.exception.BadRequestException;
import serverbyrtagihan.security.jwt.JwtUtils;

@Service
public class EcollectionService {
    private final RestTemplate restTemplate;

    @Autowired
    public EcollectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EcollectionResponseDTO sendPayloadToEcollection(String apiUrl, EcollectionDTO payload, String jwtToken) {
        try {
            Claims claims = JwtUtils.decodeJwt(jwtToken);
            String typeToken = claims.getAudience();

            if (typeToken.equals("User")) {

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);


                ObjectMapper objectMapper = new ObjectMapper();
                String payloadJson = objectMapper.writeValueAsString(payload);

                BniEncryption hash = new BniEncryption();
                String cid = "99129"; // from BNI
                String key = "6ae8bbf31b9629a62940aab16ca386cc"; // from BNI
                String prefix = "988"; // from BNI

                String parsedData = hash.hashData(payloadJson, cid, key);

                BNIRequestDTO bniRequestDTO = new BNIRequestDTO();
                bniRequestDTO.setClient_id(cid);
                bniRequestDTO.setPrefix(prefix);
                bniRequestDTO.setData(parsedData);

                HttpEntity<BNIRequestDTO> requestEntity = new HttpEntity<>(bniRequestDTO, headers);

                ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
                String responseBody = responseEntity.getBody();

                EcollectionResponseDTO responseDTO = objectMapper.readValue(responseBody, EcollectionResponseDTO.class);
                if (responseDTO.getData() != null){
                    String decryptedData = hash.parseData(responseDTO.getData(), cid, key);
                    JsonNode decryptedDataJson = objectMapper.readTree(decryptedData);
                    responseDTO.setData(decryptedDataJson.toString());
                }

                return responseDTO;
            } else {
                throw new BadRequestException("Token not valid");
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new BadRequestException("Failed to send payload to Ecollection");
        }
    }
}
