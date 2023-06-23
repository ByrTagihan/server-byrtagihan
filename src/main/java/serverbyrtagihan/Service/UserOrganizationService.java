package serverbyrtagihan.Service;

import serverbyrtagihan.Modal.UserOrganizationModel;

import java.util.*;

public interface UserOrganizationService {
    UserOrganizationModel add(UserOrganizationModel user, String JwtToken);

    UserOrganizationModel preview(Long id,String JwtToken);

    List<UserOrganizationModel> getAll(String JwtToken);

    UserOrganizationModel put(Long id,UserOrganizationModel user, String JwtToken);

    Map<String, Boolean> delete(Long id,String JwtToken);
}
