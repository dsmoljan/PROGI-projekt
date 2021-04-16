package hr.fer.progi.dogGO.rest.dto;

public class AssociationEdit {
	
		private Long id;
	    private String name;
	    private String firstName;
	    private String lastName;
	    private String username;
	    private String email;
	    private String webAddress;
	    private String description;
	    private String pictureURL;
	    private String phoneNumber;
	    
		public AssociationEdit(Long id, String name, String firstName, String lastName, String username, String email, String webAddress,
				String description, String pictureURL, String phoneNumber) {
			this.id = id;
			this.name = name;
			this.firstName = firstName;
			this.lastName = lastName;
			this.username = username;
			this.email = email;
			this.webAddress = webAddress;
			this.description = description;
			this.pictureURL = pictureURL;
			this.phoneNumber = phoneNumber;
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
	    
}
