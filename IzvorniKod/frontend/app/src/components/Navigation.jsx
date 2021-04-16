import React, { useEffect } from "react";
import { Nav } from "react-bootstrap";
import { propTypes } from "react-bootstrap/esm/Image";
import { useSelector, useDispatch } from "react-redux";
import { connect } from "react-redux";
import { logoutUser } from "../redux/actions";
import "./../static/nav.css";

function Navigation(props) {
  const dispatch = useDispatch();
  const loggedUser = useSelector((state) => state.user.logged);
  const userType = useSelector((state) => state.user.userType);
  //const userName = useSelector((state) => state.user.data.firstName);

  useEffect(() => {
    const burger = document.getElementById("burger");
    const nav = document.getElementById("nav-links");
    const navLinks = document.querySelectorAll(".nav-links li");
    const body = document.body;

    console.log(burger);
    console.log(nav);
    console.log(navLinks);
    burger.addEventListener("click", () => {
      //Toggle Nav
      nav.classList.toggle("nav-active");

      //Animate Links
      navLinks.forEach((link, index) => {
        if (link.style.animation) {
          link.style.animation = "";
        } else {
          link.style.animation = `navLinkFade 0.5s ease forwards ${
            index / 7 + 0.5
          }s`;
        }
      });
      //Burger Animation
      burger.classList.toggle("toggle");
      body.classList.toggle("darker");
    });
  });

  return (
    <nav className="navbar">
      <a className="navbar-brand" style={{ color: "black" }} href="/home">
        <strong>DogGO</strong>
      </a>
      <Nav activeKey="/home">
        <ul className="nav-links" id="nav-links">
          <li>
            <Nav.Link style={{ color: "black" }} id="1" href="/home">
              Naslovna
            </Nav.Link>
          </li>
          <li>
            <Nav.Link
              style={{ color: "black" }}
              id="2"
              href="/associations/all"
            >
              Udruge
            </Nav.Link>
          </li>
          <li>
            <Nav.Link style={{ color: "black" }} id="3" href="/walkers">
              Šetači
            </Nav.Link>
          </li>
          <li>
            {loggedUser ? (
              <Nav.Link
                style={{ color: "black" }}
                id="5"
                href={
                  userType === "korisnik"
                    ? "/userprofile"
                    : userType === "admin"
                    ? "/adminProfile"
                    : "/associationProfile"
                }
              >
                Moj profil
              </Nav.Link>
            ) : (
              <Nav.Link style={{ color: "black" }} id="5" href="/registration">
                Registracija
              </Nav.Link>
            )}
          </li>
          <li>
            {loggedUser ? (
              <Nav.Link
                style={{ color: "black" }}
                id="5"
                href="/home"
                onClick={() => props.dispatch()}
              >
                Odjava
              </Nav.Link>
            ) : (
              <Nav.Link style={{ color: "black" }} id="5" href="/login">
                Prijava
              </Nav.Link>
            )}
          </li>
        </ul>
      </Nav>
      <div className="burger" id="burger">
        <div className="line1"></div>
        <div className="line2"></div>
        <div className="line3"></div>
      </div>
    </nav>
  );
}

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch: () => dispatch(logoutUser()),
  };
};

export default connect(null, mapDispatchToProps)(Navigation);
