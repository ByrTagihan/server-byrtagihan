package serverbyrtagihan.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import serverbyrtagihan.dto.PictureDTO;
import serverbyrtagihan.model.Profile;
import serverbyrtagihan.dto.ProfileDTO;
import serverbyrtagihan.service.ProfileService;
import serverbyrtagihan.response.CommonResponse;
import serverbyrtagihan.response.ResponseHelper;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://127.0.0.1:5173")
public class ProfileCustomerController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "/customer/profile",consumes = "multipart/form-data")
    public CommonResponse<Profile> add(ProfileDTO profileDTO, @RequestPart("file") MultipartFile multipartFile) {
        return ResponseHelper.ok(profileService.add(modelMapper.map(profileDTO, Profile.class), multipartFile));
    }

    @GetMapping(path = "/customer/profile/{id}")
    public CommonResponse<Profile> getByID(@PathVariable("id") Long id) {
        return ResponseHelper.ok(profileService.getById(id));
    }

    @GetMapping(path ="/customer/profile")
    public CommonResponse<List<Profile>> getAll() {
        return ResponseHelper.ok(profileService.getAll());
    }

    @PutMapping(path = "/customer/picture/{id}", consumes = "multipart/form-data")
    public CommonResponse<Profile> putPicture(@PathVariable("id") Long id, PictureDTO profile, @RequestPart("file") MultipartFile multipartFile) {
        return ResponseHelper.ok(profileService.putPicture(modelMapper.map(profile, Profile.class), multipartFile, id));
    }
    @PutMapping(path = "/customer/profile/{id}")
    public CommonResponse<Profile> put(@PathVariable("id") Long id , @RequestBody ProfileDTO profile){
        return ResponseHelper.ok(profileService.put(modelMapper.map(profile, Profile.class), id));
    }
    @DeleteMapping(path = "/customer/profile/{id}")
    public CommonResponse<?> delete(@PathVariable("id") Long id) {
        return ResponseHelper.ok(profileService.delete(id));
    }


}
