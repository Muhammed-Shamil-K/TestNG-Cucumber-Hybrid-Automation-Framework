package hybridAutomation.enums;

public enum PlaceAPI {

    AddPlace("/maps/api/place/add/json"),
    GetPlace("/maps/api/place/get/json"),
    DeletePlace("/maps/api/place/delete/json");

    private String resource;
    PlaceAPI(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
