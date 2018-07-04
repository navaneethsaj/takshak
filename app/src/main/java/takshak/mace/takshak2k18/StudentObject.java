package takshak.mace.takshak2k18;

public class StudentObject {
    String name;
    String identifier;
    String mobileno;

    public StudentObject(String name, String identifier, String mobileno) {
        this.name = name;
        this.identifier = identifier;
        this.mobileno = mobileno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }
}
