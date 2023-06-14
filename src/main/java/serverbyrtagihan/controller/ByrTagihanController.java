package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.Modal.ByrTagihan;
import serverbyrtagihan.dto.Login;
import serverbyrtagihan.dto.Password;
import serverbyrtagihan.dto.RegisterDto;
import serverbyrtagihan.dto.Update;
import serverbyrtagihan.Service.ByrTagihanService;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "http://localhost:3000")
public class ByrTagihanController {

    @Autowired
    ByrTagihanService byrTagihanService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/api/customer/register")
    public CommonResponse<ByrTagihan> register (@RequestBody RegisterDto byrTagihan) {
        return ResponseHelper.ok(byrTagihanService.register(modelMapper.map(byrTagihan, ByrTagihan.class)));
    }

    @PostMapping("/api/customer/login")
    public CommonResponse<Map<String, Object>> login(@RequestBody Login login) {
        return ResponseHelper.ok(byrTagihanService.login(login));
    }

    @GetMapping("/api/customer/{id}")
    public CommonResponse<ByrTagihan> getById(@PathVariable("id") Long id) {
        return ResponseHelper.ok(byrTagihanService.getById(id));
    }

    @PutMapping(path = "/api/customer/update/{id}")
    public CommonResponse<ByrTagihan> update(@PathVariable("id") Long id,@RequestBody Update update) {
        return ResponseHelper.ok(byrTagihanService.update(id, modelMapper.map(update , ByrTagihan.class)));
    }

    @PutMapping(path = "/api/customer/password/{id}")
    public CommonResponse<ByrTagihan> updatePassword(@PathVariable("id") Long id,@RequestBody Password password) {
        return ResponseHelper.ok(byrTagihanService.updatePassword(id, modelMapper.map(password , ByrTagihan.class)));
    }

    @DeleteMapping("/api/customer/delete/{id}")
    public CommonResponse<Map<String, Boolean>> deleteAkun(@PathVariable("id") Long id) {
        return ResponseHelper.ok(byrTagihanService.deleteRegister(id));
    }
    @GetMapping("/api/customer/GetAll")
    public CommonResponse<List<ByrTagihan>> getAll() {
        return ResponseHelper.ok(byrTagihanService.getAllTagihan());
    }
}
