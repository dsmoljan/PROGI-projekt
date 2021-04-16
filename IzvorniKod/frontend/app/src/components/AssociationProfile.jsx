import React, { useState, useSelector } from "react";
import "./../static/associationprofile.css";
import { withRouter, Link } from "react-router-dom";
import { connect } from "react-redux";
import { logoutUser } from "../redux/actions";
import "react-calendar/dist/Calendar.css";
import AddIcon from "@material-ui/icons/Add";
import Tabs from "react-bootstrap/Tabs";
import Tab from "react-bootstrap/Tab";
import avatar from "./../static/avatardoggo.png";
import defaultDog from "./../static/dog.jpg";
import AddDog from "./dog/AddDog";
import DogEditForm from "./dog/DogEditForm";
import Button from "@material-ui/core/Button";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import EditAvailability from "./dog/EditAvailabilityForm";
import { Button as ButtonA, Popconfirm } from "antd";
import ReservationForm from "./dog/ReservationForm";
import ReservationTable from "./ReservationTable";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import moment from "moment";
import "moment/locale/eu";
import { authHeader } from "./../helpers/authHeader";

var momentRange = require("moment-range");
momentRange.extendMoment(moment);

//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";
const ASSOCIATION_PATH_ID = "/association/:id";
const ASSOCIATION_PATH_PROFILE = "/associationprofile";

class AssociationProfile extends React.Component {
  GOOD_DATES = [];

  constructor(props) {
    super(props);
    this.showModal = this.showModal.bind(this);
    this.hideModal = this.hideModal.bind(this);
    this.onChange = this.onChange.bind(this);
    this.editContactOnClick = this.editContactOnClick.bind(this);
    this.editLocationOnClick = this.editLocationOnClick.bind(this);
    this.editPasswordOnClick = this.editPasswordOnClick.bind(this);
    this.onCancel = this.onCancel.bind(this);
    this.state = {
      availabilitySet: [],
      availabilitySetEdit: [],

      dogId: "",
      dogName: "",
      breed: "",
      description: "",
      picture: "",
      walkStyle: "",
      visibleAvailability: false,
      visibleReservation: false,
      visibleEdit: false,
      visible: false,
      id: "",
      oib: "",
      name: "",
      firstName: "",
      lastName: "",
      email: "",
      webAddress: "",
      description: "",
      pictureURL: "",
      phoneNumber: "",
      deleted: "",
      dogList: [],
      reservations: [],
      associationLocationPreview: {},
      association: {
        id: "",
        oib: "",
        name: "",
        firstName: "",
        lastName: "",
        username: "",
        email: "",
        webAddress: "",
        description: "",
        pictureURL: "",
        phoneNumber: "",
        deleted: "",
        dogList: [],
        associationLocationPreview: {},
      },
      editAssociation: {
        id: "",
        oib: "",
        name: "",
        firstName: "",
        lastName: "",
        username: "",
        email: "",
        webAddress: "",
        description: "",
        pictureURL: "",
        phoneNumber: "",
        deleted: "",
        dogList: [],
        associationLocationPreview: {},
      },
    };
  }
  dogList = [];
  ulaz = 0;
  ulazEdit = 0;
  prviUlaz = true;

  onChange = (event) => {
    const { name, value } = event.target;
    console.log(name + " " + value);
    if (name === "street" || name === "houseNumber" || name === "city") {
      this.setState({
        editAssociation: {
          associationLocationPreview: {
            [name]: value,
          },
        },
      });
      return true;
    }
    this.setState({
      editAssociation: {
        [name]: value,
      },
    });
  };

  componentDidMount() {
    {
      var extension = "";
      if (
        (this.props.match.path === ASSOCIATION_PATH_ID &&
          this.props.userType === "korisnik") ||
        (this.props.userType === "udruga" &&
          this.props.id != this.props.match.params.id &&
          this.props.match.path === ASSOCIATION_PATH_ID) ||
        this.props.userType === undefined ||
        this.props.userType === "admin"
      )
        extension = this.props.match.params.id;
      else extension = this.props.id;
      fetch(`${BACKEND_URL}/association?associationId=${extension}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json; charset=UTF-8",
          Authorization: authHeader(),
        },
      }).then((response) => {
        response.json().then((json) => {
          this.setState({
            association: {
              id: json.id,
              oib: json.oib,
              name: json.name,
              firstName: json.firstName,
              lastName: json.lastName,
              username: json.username,
              email: json.email,
              webAddress: json.webAddress,
              description: json.description,
              pictureURL: json.pictureURL,
              phoneNumber: json.phoneNumber,
              deleted: json.deleted,
              dogList: json.dogList,
              associationLocationPreview: json.associationLocationPreview,
            },
          });
          this.setState({
            editAssociation: this.state.association,
          });
          console.log(this.state);
        });
      });
      fetch(
        `${BACKEND_URL}/reservations/get/byassociation/all?associationId=${extension}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json; charset=UTF-8",
            Authorization: authHeader(),
          },
        }
      ).then((response) => {
        response.json().then((json) => {
          this.setState({
            reservations: json,
          });
          console.log(this.state);
        });
      });
    }
  }

  editContactOnClick() {
    var name = document.getElementById("name").value;
    var firstName = document.getElementById("ime").value;
    var lastName = document.getElementById("prezime").value;
    var username = document.getElementById("username").value;
    var email = document.getElementById("email").value;
    var phoneNumber = document.getElementById("phoneNumber").value;
    var description = document.getElementById("description").value;
    var pictureURL = document.getElementById("picture").value;
    var webAddress = document.getElementById("webAddress").value;
    var password = document.getElementById("edit-pass").value;

    if (name === "") {
      name = null;
    }

    if (username === "") {
      username = null;
    }
    if (!this.usernameCheck) {
      return false;
    }

    console.log(username);

    if (firstName === "") {
      firstName = null;
    }
    if (lastName === "") {
      lastName = null;
    }

    if (email === "") {
      email = null;
    }
    if (!this.emailCheck) {
      return false;
    }

    if (phoneNumber === "") {
      phoneNumber = null;
    }
    if (description === "") {
      description = null;
    }
    if (webAddress === "") {
      webAddress = null;
    }
    if (pictureURL === "") {
      pictureURL = null;
    }

    const contactData = {
      id: this.state.association.id,
      name: name,
      username: username,
      firstName: firstName,
      lastName: lastName,
      email: email,
      phoneNumber: phoneNumber,
      description: description,
      pictureURL: pictureURL,
      webAddress: webAddress,
    };

    console.log(contactData);
    if (contactData.username == "" || contactData.username == null)
      contactData.username = this.state.association.username;
    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
      body: JSON.stringify(contactData),
    };
    fetch(`${BACKEND_URL}/association/edit-profile`, options).then(
      (response) => {
        if (response.status === 400) {
          toast.error("❌ Vaša promjena podataka nije uspjela!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        } else {
          console.log("uspješna promjena profilnih podataka");
          this.setState({
            association: this.state.editAssociation,
          });
          if (this.props.userType !== "admin") {
          localStorage.setItem(
            "user",
            JSON.stringify({
              userId: this.state.association.id,
              ...{
                authdata: window.btoa(`${contactData.username}:${password}`),
              },
            })
          );
          }
          this.onCancel();
          this.componentDidMount();
          this.render();
        }
      }
    );
  }

  editLocationOnClick() {
    var street = document.getElementById("street").value;
    var houseNumber = document.getElementById("houseNumber").value;
    var city = document.getElementById("city").value;

    if (street === "") {
      street = null;
    }
    if (houseNumber === "") {
      houseNumber = null;
    }
    if (city === "") {
      city = null;
    }

    const locationData = {
      associationId: this.state.association.id,
      street: street,
      houseNumber: houseNumber,
      city: city,
    };

    console.log(locationData);

    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
      body: JSON.stringify(locationData),
    };
    fetch(`${BACKEND_URL}/association/edit-location`, options).then(
      (response) => {
        if (response.status === 400) {
          toast.error("❌ Vaša promjena lokacije nije uspjela!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        } else {
          console.log("uspješna promjena podataka lokacije");
          console.log(this.state);
          this.setState({
            association: {
              street: this.state.editAssociation.street,
              houseNumber: this.state.editAssociation.houseNumber,
              city: this.state.editAssociation.city,
            },
          });
          this.onCancel();
          this.componentDidMount();
          this.render();
        }
      }
    );
  }

  editPasswordOnClick() {
    var trenutna = document.getElementById("lozinka").value;
    var nova = document.getElementById("nova").value;
    var potvrdanove = document.getElementById("potvrdanove").value;

    console.log(trenutna + " " + nova + " " + potvrdanove + " ");

    if (!this.checkPassword(nova, potvrdanove)) {
      toast.error("❌ Polja nove lozinke i njezine potvrde nisu ista.", {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      return false;
    }

    const passwordData = {
      id: this.state.association.id,
      oldPassword: trenutna,
      newPassword: nova,
    };
    console.log(passwordData);

    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
      body: JSON.stringify(passwordData),
    };
    fetch(`${BACKEND_URL}/association/edit-password`, options).then(
      (response) => {
        if (response.status === 400) {
          toast.error("❌ Unijeli ste krivu trenutnu lozinku!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        } else {
          console.log("uspjeh");
          if (this.props.userType !== "admin") {
          localStorage.setItem(
            "user",
            JSON.stringify({
              userId: this.state.association.id,
              ...{
                authdata: window.btoa(
                  `${this.state.association.username}:${passwordData.newPassword}`
                ),
              },
            })
          );
          }
          this.onCancel();
          this.componentDidMount();
          this.render();
        }
      }
    );
  }

  editProfile() {
    document.getElementById("view").classList.toggle("d-none");
    document.getElementById("edit").classList.toggle("d-none");
    document.getElementById("view2").classList.toggle("d-none");
  }

  onCancel() {
    document.getElementById("lozinka").value = "";
    document.getElementById("nova").value = "";
    document.getElementById("potvrdanove").value = "";
    this.setState({
      editAssociation: this.state.association,
    });
    this.editProfile();
  }

  usernameCheck = () => {
    var username = document.getElementById("username").value;
    if (username === this.state.association.username) {
      console.log("isti username");
      return false;
    }
    const options = {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
    };
    fetch(
      `${BACKEND_URL}/registration/association/username-available/?username=${username}`,
      options
    ).then((response) => {
      if (response.status === 400) {
        toast.error("❌ Korisničko ime nije slobodno!", {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      }
    });
  };

  addGoodDates = () => {
    this.state.availabilitySet.map((entry) => {
      console.log(entry);
      this.GOOD_DATES.push(moment.range(entry.startDate, entry.endDate));
    });
  };

  emailCheck = () => {
    var email = document.getElementById("email").value;
    if (email === this.state.association.email) {
      console.log("isti email");
      return false;
    }
    const options = {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
    };
    fetch(
      `${BACKEND_URL}/registration/association/email-available/?email=${email}`,
      options
    ).then((response) => {
      if (response.status === 400) {
        toast.error("❌ Korisničko ime nije slobodno!", {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      }
    });
  };

  checkPassword = (nova, potvrdanove) => {
    return nova === potvrdanove;
  };

  checkPasswordLength = () => {
    var pass = document.getElementById("nova").value;
    //console.log(pass);
    if (pass !== undefined && (pass.length < 6 || pass.length > 20)) {
      toast.error("❌ Lozinka mora sadržavati između 6 i 20 znakova!", {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      return false;
    }
    return true;
  };

  deleteHandler = (id) => {
    console.log(id);
    fetch(`${BACKEND_URL}/association/delete?associationId=${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
      //body: JSON.stringify(this.state.initialValues),
    })
      .then((data) => {
        if (data.error) {
          console.log("data", data);
          alert(data.message);
        } else {
          window.location.reload();
        }
      })
      .catch((err) => {
        console.log(err);
      });
    this.props.history.push("/login");
    this.props.dispatch();
  };

  routeChange = (id) => {
    this.props.history.push(`/reservation/${id}`);
  };

  viewProfile() {
    document.getElementById("dogs-tab").classList.toggle("active");
    document.getElementById("reserve-tab").classList.toggle("active");
    document.getElementById("profile-dogs").classList.toggle("active");
    document.getElementById("profile-dogs").classList.toggle("show");
    document.getElementById("profile-reserve").classList.toggle("active");
    document.getElementById("profile-reserve").classList.toggle("show");
  }

  render() {
    return (
      <div className="container container-fix">
        <div className="profile profile-fix">
          <div className="profile-header" id="view">
            <div className="profile-header-cover"></div>
            <div className="profile-header-content">
              <div className="profile-header-img">
                {this.state.association.pictureURL !== null &&
                this.state.association.pictureURL !== "" ? (
                  <img src={this.state.association.pictureURL} />
                ) : (
                  <img src={avatar} alt="" />
                )}
              </div>
              <ul
                className="profile-header-tab nav nav-tabs nav-tabs-v2"
                role="tablist"
                id="myTab"
              >
                <li className="nav-item">
                  <a
                    className="nav-link active"
                    data-toggle="tab"
                    id="dogs-tab"
                    role="tab"
                    aria-controls="nav-dogs"
                    aria-selected="true"
                    onClick={this.viewProfile}
                  >
                    <div className="nav-field">Psi</div>
                    <div className="nav-value">
                      {this.state.association.dogList != undefined
                        ? this.state.association.dogList.length
                        : 0}
                    </div>
                  </a>
                </li>
                {(this.props.userType === "udruga" &&
                  this.props.match.path === ASSOCIATION_PATH_PROFILE) ||
                (this.props.userType === "udruga" &&
                  this.props.match.path === ASSOCIATION_PATH_ID &&
                  this.props.id == this.props.match.params.id) ||
                this.props.userType === "admin" ? (
                  <li className="nav-item">
                    <a
                      className="nav-link"
                      data-toggle="tab"
                      id="reserve-tab"
                      role="tab"
                      aria-controls="nav-reserve"
                      aria-selected="false"
                      onClick={this.viewProfile}
                    >
                      <div className="nav-field">Rezervacije</div>
                      <div className="nav-value">
                        {this.state.reservations != undefined
                          ? this.state.reservations.length
                          : 0}
                      </div>
                    </a>
                  </li>
                ) : (
                  ""
                )}
              </ul>
            </div>
          </div>

          <div className="profile-container" id="view2">
            <div className="profile-sidebar">
              <div className="desktop-sticky-top">
                <h4>{this.state.association.name}</h4>
                <div className="font-weight-600 mb-3 text-muted mt-n2">
                  {this.state.association.firstName +
                    " " +
                    this.state.association.lastName}
                </div>
                <p>{this.state.association.description}</p>
                <div className="mb-1">
                  <i className="fa fa-map-marker-alt fa-fw text-muted"></i>
                  {(this.state.association.associationLocationPreview ===
                    undefined ||
                    this.state.association.associationLocationPreview ===
                      null) &&
                  ((this.props.userType === "udruga" &&
                    this.props.match.path === ASSOCIATION_PATH_PROFILE) ||
                    (this.props.userType === "udruga" &&
                      this.props.match.path === ASSOCIATION_PATH_ID &&
                      this.props.id == this.props.match.params.id) ||
                    this.props.userType === "admin") ? (
                    <a href="#" onClick={this.editProfile}>
                      Dodajte lokaciju
                    </a>
                  ) : (
                    ""
                  )}
                  {this.state.association.associationLocationPreview !==
                    undefined &&
                  this.state.association.associationLocationPreview !== null
                    ? this.state.association.associationLocationPreview.street +
                      " " +
                      this.state.association.associationLocationPreview
                        .houseNumber +
                      ", " +
                      this.state.association.associationLocationPreview.city
                    : ""}
                </div>
                <div className="mb-3">
                  <i className="fa fa-link fa-fw text-muted"></i>
                  <a href={this.state.association.webAddress}>
                    {this.state.association.webAddress}
                  </a>
                </div>
                <hr className="mt-4 mb-4" />
                {(this.props.userType === "udruga" &&
                  this.props.match.path === ASSOCIATION_PATH_PROFILE) ||
                (this.props.userType === "udruga" &&
                  this.props.match.path === ASSOCIATION_PATH_ID &&
                  this.props.id == this.props.match.params.id) ||
                this.props.userType === "admin" ? (
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={this.editProfile}
                  >
                    UREDI PROFIL
                  </Button>
                ) : (
                  ""
                )}
              </div>
            </div>

            <div className="profile-content">
              <div className="row">
                <div className="col-xl-12">
                  <div className="tab-content p-0">
                    <div
                      className="tab-pane fade active show"
                      id="profile-dogs"
                      role="tabpanel"
                      aria-labelledby="dogs-tab"
                    >
                      {(this.props.userType === "udruga" &&
                        this.props.match.path === ASSOCIATION_PATH_PROFILE) ||
                      (this.props.userType === "udruga" &&
                        this.props.match.path === ASSOCIATION_PATH_ID &&
                        this.props.id == this.props.match.params.id) ||
                      this.props.userType === "admin" ? (
                        <Button
                          variant="contained"
                          color="primary"
                          onClick={this.showModal}
                        >
                          {this.state.visible === true ? (
                            <AddDog
                              visible={this.state.visible}
                              associationId={this.state.association.id}
                            />
                          ) : (
                            ""
                          )}
                          <AddIcon
                            color="action"
                            aria-label="add dog"
                            className="add-icon"
                          />
                          DODAJ PSA
                        </Button>
                      ) : (
                        ""
                      )}
                      {this.state.association !== undefined &&
                      this.state.association.dogList !== undefined &&
                      this.state.association.dogList.length > 0 ? (
                        this.state.association.dogList.map(
                          (dog) => (
                            console.log(dog),
                            (
                              <div
                                className="dog-list d-flex flex-row bd-highlight mb-3 align-items-center"
                                id={dog.dogId}
                                key={dog.dogId}
                                data-index={dog.dogId}
                              >
                                {dog.picture !== null && dog.picture !== "" ? (
                                  <div className="p-2 w-40 bd-highlight">
                                    <img
                                      src={dog.picture}
                                      width="100px"
                                      className="rounded-sm ml-n2"
                                    />
                                  </div>
                                ) : (
                                  <div className="p-2 w-40 bd-highlight">
                                    <img
                                      src={defaultDog}
                                      width="100px"
                                      className="rounded-sm ml-n2"
                                    />
                                  </div>
                                )}
                                <div className="p-2 w-30 bd-highlight">
                                  <div className="text-dark font-weight-600">
                                    {dog.name}
                                  </div>
                                  <div className="text-muted fs-5px">
                                    {dog.breed}
                                  </div>
                                </div>
                                <div className="p-2 flex-fill bd-highlight">
                                  <div className="text-muted fs-5px">
                                    {dog.description}
                                  </div>
                                </div>
                                <div className="p-2 w-30 bd-highlight">
                                  <div className="text-dark font-weight-600">
                                    Vrsta šetnje
                                  </div>
                                  <div className="text-muted fs-5px">
                                    {dog.walkStyle === "INDIVIDUAL"
                                      ? "INDIVIDUALNA"
                                      : "GRUPNA"}
                                  </div>
                                </div>
                                <div
                                  id="button"
                                  className="p-2 w-30 bd-highlight"
                                >
                                  {this.props.match.path ===
                                    ASSOCIATION_PATH_ID &&
                                  this.props.userType !== "udruga" &&
                                  this.props.userType !== "admin" ? (
                                    <Button
                                      variant="contained"
                                      color="primary"
                                      onClick={async () => {
                                        console.log(this.state);
                                        if (
                                          this.props.userType === "korisnik"
                                        ) {
                                          if (this.prviUlaz) {
                                            fetch(
                                              `${BACKEND_URL}/dog?dogId=${dog.dogId}`,
                                              {
                                                method: "GET",
                                                headers: {
                                                  "Content-Type":
                                                    "application/json; charset=UTF-8",
                                                  Authorization: authHeader(),
                                                },
                                              }
                                            ).then((json) => {
                                              json.json().then((response) => {
                                                this.setState({
                                                  dogId: response.dogId,
                                                  walkStyle: response.walkStyle,
                                                });
                                                console.log(response.dogId);
                                              });
                                            });
                                            fetch(
                                              `${BACKEND_URL}/dog/availability/get?dogId=${dog.dogId}`,
                                              {
                                                method: "GET",
                                                headers: {
                                                  "Content-Type":
                                                    "application/json; charset=UTF-8",
                                                  Authorization: authHeader(),
                                                },
                                              }
                                            ).then((json) => {
                                              json.json().then((response) => {
                                                console.log(response);
                                                this.setState({
                                                  //dogId: dog.dogId,
                                                  availabilitySet: response,
                                                  //walkStyle: dog.walkStyle,
                                                  visibleReservation: true,
                                                });
                                                // TODO

                                                this.addGoodDates();
                                                console.log(this.GOOD_DATES);
                                              });
                                              console.log(
                                                this.state.availabilitySet
                                              );
                                            });
                                            this.prviUlaz = false;
                                          }
                                        } else {
                                          toast.info(
                                            "🐶 Ulogirajte se kako bi napravili rezervaciju!",
                                            {
                                              position: "top-right",
                                              autoClose: 5000,
                                              hideProgressBar: true,
                                              closeOnClick: true,
                                              pauseOnHover: true,
                                              draggable: true,
                                              progress: undefined,
                                            }
                                          );
                                        }
                                      }}
                                    >
                                      <ReservationForm
                                        walkerId={this.props.id}
                                        visible={this.state.visibleReservation}
                                        //onClick={this.showModalRegistration}
                                        handleClose={this.hideModalRegistration}
                                        availabilitySet={
                                          this.state.availabilitySet
                                        }
                                        GOOD_DATES={this.GOOD_DATES}
                                        associationId={
                                          this.state.association.id
                                        }
                                        dogId={this.state.dogId}
                                        walkStyle={this.state.walkStyle}
                                      />
                                      REZERVIRAJ
                                    </Button>
                                  ) : (
                                    ""
                                  )}
                                  {((this.props.match.path ===
                                    ASSOCIATION_PATH_PROFILE ||
                                    this.props.id ==
                                      this.props.match.params.id) &&
                                    this.props.userType === "udruga") ||
                                  this.props.userType === "admin" ? (
                                    <Button
                                      variant="contained"
                                      color="primary"
                                      //onClick={this.showModalEdit}
                                      onClick={async () =>
                                        fetch(
                                          `${BACKEND_URL}/dog?dogId=${dog.dogId}`,
                                          {
                                            method: "GET",
                                            headers: {
                                              "Content-Type":
                                                "application/json; charset=UTF-8",
                                              Authorization: authHeader(),
                                            },
                                          }
                                        ).then((json) => {
                                          json.json().then((response) => {
                                            this.setState({
                                              dogId: response.dogId,
                                              dogName: response.name,
                                              breed: response.breed,
                                              description: response.description,
                                              picture: response.picture,
                                              walkStyle: response.walkStyle,
                                              visibleEdit: true,
                                            });
                                          });
                                        })
                                      }
                                    >
                                      {this.state.visibleEdit === true ? (
                                        <DogEditForm
                                          visible={this.state.visibleEdit}
                                          onClick={this.showModalEdit}
                                          handleClose={this.hideModalEdit}
                                          associationId={
                                            this.state.association.id
                                          }
                                          dogId={this.state.dogId}
                                          dogName={this.state.dogName}
                                          breed={this.state.breed}
                                          description={this.state.description}
                                          picture={this.state.picture}
                                          walkStyle={this.state.walkStyle}
                                        />
                                      ) : (
                                        ""
                                      )}
                                      UREDI
                                    </Button>
                                  ) : (
                                    ""
                                  )}{" "}
                                  {((this.props.match.path ===
                                    ASSOCIATION_PATH_PROFILE ||
                                    this.props.id ==
                                      this.props.match.params.id) &&
                                    this.props.userType === "udruga") ||
                                  this.props.userType === "admin" ? (
                                    <Button
                                      variant="contained"
                                      color="primary"
                                      onClick={async () => {
                                        console.log(dog.dogId);
                                        fetch(
                                          `${BACKEND_URL}/dog/availability/get/original/all?dogId=${dog.dogId}`,
                                          {
                                            method: "GET",
                                            headers: {
                                              "Content-Type":
                                                "application/json; charset=UTF-8",
                                              Authorization: authHeader(),
                                            },
                                          }
                                        ).then((json) => {
                                          json.json().then((response) => {
                                            this.setState({
                                              availabilitySet: response,
                                              visibleAvailability: true,
                                            });
                                          });
                                        });
                                      }}
                                    >
                                      {this.state.visibleAvailability ===
                                      true ? (
                                        <EditAvailability
                                          visible={
                                            this.state.visibleAvailability
                                          }
                                          onClick={this.showModalAvailability}
                                          handleClose={
                                            this.hideModalAvailability
                                          }
                                          availabilitySet={
                                            this.state.availabilitySet
                                          }
                                          associationId={
                                            this.state.association.id
                                          }
                                          dogId={dog.dogId}
                                          walkStyle={dog.walkStyle}
                                        />
                                      ) : (
                                        ""
                                      )}
                                      UREDI DOSTUPNOST
                                    </Button>
                                  ) : (
                                    ""
                                  )}
                                </div>
                              </div>
                            )
                          )
                        )
                      ) : (
                        <p>Nema psa.</p>
                      )}
                    </div>
                    <div
                      className="tab-pane fade"
                      id="profile-reserve"
                      role="tabpanel"
                      aria-labelledby="reserve-tab"
                    >
                      <div className="panel-content">
                        <TableContainer>
                          {" "}
                          <div style={{ margin: "5%" }}>
                            <Table aria-label="custom pagination table">
                              <TableBody>
                                {this.state.reservations !== undefined &&
                                this.state.reservations.length > 0 ? (
                                  <ReservationTable
                                    rows={this.state.reservations}
                                  ></ReservationTable>
                                ) : (
                                  <TableCell component="th" scope="row">
                                    Nema dostupnih rezervacija
                                  </TableCell>
                                )}
                              </TableBody>
                            </Table>
                          </div>
                        </TableContainer>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div className="well align-items-center d-none" id="edit">
            <Tabs defaultActiveKey="profile" id="uncontrolled-tab">
              <Tab eventKey="profile" title="Profil">
                <form className="edit-form-control">
                  <label>Naziv udruge</label>
                  <input
                    id="name"
                    type="text"
                    value={this.state.editAssociation.name}
                    name="name"
                    onChange={this.onChange}
                  />
                  <label>Ime</label>
                  <input
                    id="ime"
                    type="text"
                    value={this.state.editAssociation.firstName}
                    name="firstName"
                    onChange={this.onChange}
                  />
                  <label>Prezime</label>
                  <input
                    id="prezime"
                    type="text"
                    value={this.state.editAssociation.lastName}
                    name="lastName"
                    onChange={this.onChange}
                  />
                  <label>Korisničko ime</label>
                  <input
                    id="username"
                    type="text"
                    value={this.state.editAssociation.username}
                    name="username"
                    onChange={this.onChange}
                  />
                  <label>E-mail</label>
                  <input
                    id="email"
                    type="text"
                    value={this.state.editAssociation.email}
                    name="email"
                    onChange={this.onChange}
                  />
                  <label>Kontakt</label>
                  <input
                    id="phoneNumber"
                    type="text"
                    value={
                      this.state.editAssociation.phoneNumber === null
                        ? ""
                        : this.state.editAssociation.phoneNumber
                    }
                    name="phoneNumber"
                    onChange={this.onChange}
                  />
                  <label>Opis udruge</label>
                  <textarea
                    id="description"
                    type="text"
                    value={
                      this.state.editAssociation.description === null
                        ? ""
                        : this.state.editAssociation.description
                    }
                    name="description"
                    onChange={this.onChange}
                  />
                  <label>URL slike</label>
                  <input
                    id="picture"
                    type="text"
                    value={
                      this.state.editAssociation.pictureURL === null
                        ? ""
                        : this.state.editAssociation.pictureURL
                    }
                    name="picture"
                    onChange={this.onChange}
                  />
                  <label>Web-adresa</label>
                  <input
                    id="webAddress"
                    type="text"
                    value={
                      this.state.editAssociation.webAdress === null
                        ? ""
                        : this.state.editAssociation.webAddress
                    }
                    name="webAdress"
                    onChange={this.onChange}
                  />
                  <label className="promijeni-sifru">
                    UPIŠITE LOZINKU!
                  </label>
                  <input required id="edit-pass" type="password" />
                </form>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.onCancel}
                >
                  ODUSTANI
                </button>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.editContactOnClick}
                >
                  SPREMI PROMJENE
                </button>
                <div className="buttonA">
                  <Popconfirm
                    placement="topRight"
                    title={
                      "Jeste li sigurni da želite obrisati profil?\n Ova radnja se ne može poništiti!"
                    }
                    onConfirm={(e) =>
                      this.deleteHandler(this.state.association.id, e)
                    }
                    okText="Da"
                    cancelText="Ne"
                  >
                    <ButtonA type="danger">OBRIŠI PROFIL</ButtonA>
                  </Popconfirm>
                </div>
              </Tab>
              <Tab eventKey="location" title="Lokacija">
                <form className="edit-form-control">
                  <label>Ulica</label>
                  <input
                    id="street"
                    type="text"
                    value={
                      this.state.editAssociation.associationLocationPreview ===
                      null
                        ? ""
                        : this.state.editAssociation
                            .associationLocationPreview === undefined
                        ? ""
                        : this.state.editAssociation.associationLocationPreview
                            .street
                    }
                    name="street"
                    onChange={this.onChange}
                  />
                  <label>Kućni broj</label>
                  <input
                    id="houseNumber"
                    type="text"
                    value={
                      this.state.editAssociation.associationLocationPreview ===
                      null
                        ? ""
                        : this.state.editAssociation
                            .associationLocationPreview === undefined
                        ? ""
                        : this.state.editAssociation.associationLocationPreview
                            .houseNumber
                    }
                    name="houseNumber"
                    onChange={this.onChange}
                  />
                  <label>Grad</label>
                  <input
                    id="city"
                    type="text"
                    value={
                      this.state.editAssociation.associationLocationPreview ===
                      null
                        ? ""
                        : this.state.editAssociation
                            .associationLocationPreview === undefined
                        ? ""
                        : this.state.editAssociation.associationLocationPreview
                            .city
                    }
                    name="city"
                    onChange={this.onChange}
                  />
                </form>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.onCancel}
                >
                  ODUSTANI
                </button>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.editLocationOnClick}
                >
                  SPREMI PROMJENE
                </button>
              </Tab>
              <Tab eventKey="password" title="Lozinka">
                <form className="edit-form-control">
                  <label>Upišite trenutnu lozinku</label>
                  <input id="lozinka" type="password" />
                  <label>Upišite novu lozinku</label>
                  <input
                    id="nova"
                    type="password"
                    onBlur={this.checkPasswordLength}
                  />
                  <label>Potvrdite novu lozinku</label>
                  <input id="potvrdanove" type="password" />
                </form>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.onCancel}
                >
                  ODUSTANI
                </button>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.editPasswordOnClick}
                >
                  SPREMI PROMJENE
                </button>
              </Tab>
            </Tabs>
          </div>
        </div>
        <ToastContainer />
      </div>
    );
  }

  showModal = () => {
    this.setState({ visible: true });
  };

  hideModal = () => {
    this.setState({ visible: false });
  };
  showModalEdit = () => {
    this.setState({ visibleEdit: true });
  };

  hideModalEdit = () => {
    this.setState({ visibleEdit: false });
  };
  showModalRegistration = () => {
    this.setState({ visibleReservation: true });
  };

  hideModalRegistration = () => {
    this.setState({ visibleReservation: false });
  };
  showModalAvailability = () => {
    this.setState({ visibleAvailability: true });
  };
  hideModalAvailability = () => {
    this.setState({ visibleAvailability: false });
  };
}

const mapStateToProps = (state) => {
  return {
    id: state.user.id,
    oib: state.user.oib,
    name: state.user.name,
    firstName: state.user.firstName,
    lastName: state.user.lastName,
    username: state.user.username,
    email: state.user.email,
    webAddress: state.user.webAddress,
    description: state.user.description,
    phoneNumber: state.user.phoneNumber,
    pictureUrl: state.user.pictureUrl,
    logged: state.user.logged,
    userType: state.user.userType,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch: () => dispatch(logoutUser()),
  };
};

export default withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AssociationProfile)
);
