package serverbyrtagihan.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import serverbyrtagihan.dto.BNIRequestDTO;
import serverbyrtagihan.dto.EcollectionDTO;

@Service
public class EcollectionService {
    private final RestTemplate restTemplate;

    @Autowired
    public EcollectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> sendPayloadToEcollection(String apiUrl, EcollectionDTO payload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();
            String payloadJson = objectMapper.writeValueAsString(payload);

            BniEncryption hash = new BniEncryption();
            String cid = "99129"; // from BNI
            String key = "6ae8bbf31b9629a62940aab16ca386cc"; // from BNI
            String prefix = "988"; // from BNI

            String parsedData = hash.hashData(payloadJson, cid, key);
            String decodeData = hash.parseData(parsedData, cid, key);
            System.out.println(parsedData);
            System.out.println("B" + decodeData);

            BNIRequestDTO bniRequestDTO = new BNIRequestDTO();
            bniRequestDTO.setClient_id(cid);
            bniRequestDTO.setPrefix(prefix);
            bniRequestDTO.setData(parsedData);

            HttpEntity<BNIRequestDTO> requestEntity = new HttpEntity<>(bniRequestDTO, headers);
            System.out.println("Percobaan");
            return restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();

            // If an exception occurs, return an appropriate ResponseEntity indicating the failure.
            // You can customize the response based on your requirements.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send payload to Ecollection: " + e.getMessage());
        }
    }
}
