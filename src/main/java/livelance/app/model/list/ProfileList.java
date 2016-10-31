package livelance.app.model.list;

import java.util.ArrayList;
import java.util.List;

import livelance.app.model.Profile;

public class ProfileList {
    private List<Profile> profiles = new ArrayList<Profile>();

    public ProfileList(List<Profile> list) {
        this.profiles = list;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfile(List<Profile> profiles) {
        this.profiles = profiles;
    }
}
