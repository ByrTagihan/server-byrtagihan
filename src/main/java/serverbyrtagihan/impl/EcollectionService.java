package serverbyrtagihan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import serverbyrtagihan.dto.EcollectionDTO;

@Service
public class EcollectionService {
    private final RestTemplate restTemplate;

    @Autowired
    public EcollectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> sendPayloadToEcollection(String apiUrl, EcollectionDTO payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EcollectionDTO> requestEntity = new HttpEntity<>(payload, headers);

        return restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
    }
}
