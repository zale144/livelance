package livelance.app.support;

import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import livelance.app.model.list.ProfileList;
import livelance.app.web.controller.ApiProfileController;
import livelance.app.web.resource.ProfileResource;
import livelance.app.web.resource.list.ProfileListResource;

@Component
public class ProfileListResourceAsm  extends ResourceAssemblerSupport<ProfileList, ProfileListResource> {


    public ProfileListResourceAsm() {
        super(ApiProfileController.class, ProfileListResource.class);
    }

    @Override
    public ProfileListResource toResource(ProfileList accountList) {
        List<ProfileResource> resList = new ProfileResourceAsm().toResources(accountList.getProfiles());
        ProfileListResource finalRes = new ProfileListResource();
        finalRes.setProfiles(resList);
        return finalRes;
    }
}
