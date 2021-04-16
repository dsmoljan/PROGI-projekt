package hr.fer.progi.dogGO.rest.dto;

/**
 * Lokacija udruge bez udruge
 * @author Fani
 *
 */
public class AssociationLocationPreview {
	 
	String city;
	String street;
	String houseNumber;

	public AssociationLocationPreview(String city, String street, String houseNumber) {
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
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
