package serverbyrtagihan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serverbyrtagihan.dto.EcollectionResponseDTO;
import serverbyrtagihan.impl.EcollectionService;
import serverbyrtagihan.dto.EcollectionDTO;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class EcollectionController {

    private final EcollectionService ecollectionService;

    private static final String JWT_PREFIX = "jwt ";

    @Autowired
    public EcollectionController(EcollectionService ecollectionService) {
        this.ecollectionService = ecollectionService;
    }

    @PostMapping("/sendToEcollection")
    public ResponseEntity<EcollectionResponseDTO> sendToEcollection(@RequestBody EcollectionDTO payload, HttpServletRequest request) {
        String apiUrl = "https://apibeta.bni-ecollection.com/";
        String jwtToken = request.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        EcollectionResponseDTO response = ecollectionService.sendPayloadToEcollection(apiUrl, payload, jwtToken);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

