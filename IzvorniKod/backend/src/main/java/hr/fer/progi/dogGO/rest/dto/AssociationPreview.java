package hr.fer.progi.dogGO.rest.dto;


/**
 * DTO klasa za prikaz najosnovnijih podataka udruge kod pregleda svih udruga.
 */
public class AssociationPreview {
    Long id;
    String name;
    String city;

    public AssociationPreview(Long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
