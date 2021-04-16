package hr.fer.progi.dogGO.rest.dto;

import hr.fer.progi.dogGO.domain.AssociationLocation;

import java.util.List;


/**
 * Dto klasa za prijenos detalja o udruzi kod prikaza profila udruge.
 * Dogset treba sadrzavati samo NEOBRISANE pse.
 */
public class AssociationDetails {

    private Long id;

    private AssociationLocationPreview associationLocationPreview;

    private List<DogDetails> dogDetails;

    private String oib;

    private String name;

    private String firstName;

    private String lastName;

    private String email;
    
    private String username;

    private String webAddress;

    private String description;

    private String pictureURL;

    private String phoneNumber;

    private boolean deleted;

    public AssociationDetails(Long id, AssociationLocation associationLocation, List<DogDetails> dogDetails, String oib, String name, String firstName, String lastName, String username, String email, String webAddress, String description, String pictureURL, String phoneNumber, boolean deleted) {
        this.id = id;
        if(associationLocation != null) {
            this.associationLocationPreview = new AssociationLocationPreview(associationLocation.getCity(),
                    associationLocation.getStreet(), associationLocation.getHouseNumber());
        } else {
            this.associationLocationPreview = null;
        }
        this.username = username;
        this.dogDetails = dogDetails;
        this.oib = oib;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.webAddress = webAddress;
        this.description = description;
        this.pictureURL = pictureURL;
        this.phoneNumber = phoneNumber;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AssociationLocationPreview getAssociationLocationPreview() {
        return associationLocationPreview;
    }

    public void setAssociationLocationPreview(AssociationLocation associationLocation) {
        this.associationLocationPreview = new AssociationLocationPreview(associationLocation.getCity(),
        		associationLocation.getStreet(), associationLocation.getHouseNumber());
    }

    public List<DogDetails> getDogList() {
        return dogDetails;
    }

    public void setDogList(List<DogDetails> dogDetails) {
        this.dogDetails = dogDetails;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
