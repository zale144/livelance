package livelance.app.model.list;

import java.util.ArrayList;
import java.util.List;

import livelance.app.model.Location;

public class LocationList {
    private List<Location> locations = new ArrayList<Location>();

    public LocationList(List<Location> list) {
        this.locations = list;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocation(List<Location> locations) {
        this.locations = locations;
    }
}
