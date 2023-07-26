package serverbyrtagihan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import serverbyrtagihan.impl.EcollectionService;
import serverbyrtagihan.dto.EcollectionDTO;

@RestController
public class EcollectionController {

    private final EcollectionService ecollectionService;

    @Autowired
    public EcollectionController(EcollectionService ecollectionService) {
        this.ecollectionService = ecollectionService;
    }

    @PostMapping("/sendToEcollection")
    public ResponseEntity<String> sendToEcollection(@RequestBody EcollectionDTO payload) {
        String apiUrl = "https://apibeta.bni-ecollection.com/";

        ResponseEntity<String> response = ecollectionService.sendPayloadToEcollection(apiUrl, payload);

        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }
}

