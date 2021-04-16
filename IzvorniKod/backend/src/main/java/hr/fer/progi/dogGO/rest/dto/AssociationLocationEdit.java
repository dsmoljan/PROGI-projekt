package hr.fer.progi.dogGO.rest.dto;


public class AssociationLocationEdit {
	
	private Long associationId;
    private String city;
    private String street;
    private String houseNumber;
    
	public AssociationLocationEdit(Long associationId, String city, String street, String houseNumber) {
		this.associationId = associationId;
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
	}

	public Long getAssociationId() {
		return associationId;
	}

	public void setAssociationId(Long associationId) {
		this.associationId = associationId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
}
