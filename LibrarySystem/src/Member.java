public class Member {
    private int memberId;
    private String name;
    private String contactNumber;
    private String addressLine1;
    private String addressLine2;

    // Constructor
    public Member(int memberId, String name, String contactNumber, String addressLine1, String addressLine2) {
        this.memberId = memberId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
}
