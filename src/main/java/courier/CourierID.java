package courier;

public class CourierID {
    private String id;

    public CourierID() {
    }

    public CourierID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{ id= \"" + id + "\" }";
    }
}
