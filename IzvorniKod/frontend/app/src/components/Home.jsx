import "./../static/Home.css";
import { createMedia } from "@artsy/fresnel";
import React, { Component } from "react";
import { Container, GridColumn, Header } from "semantic-ui-react";
import "react-toastify/dist/ReactToastify.css";
import Button from "@material-ui/core/Button";

function home(props) {
  console.log(props);
  return (
    <div className="fix">
      <div className="home-container d-flex flex-column">
        <div className="text-center mx-auto back">
          <Header className="display-1 h1 back" content="DogGO" />
          <Header className="display-4 h2 back" content="Idemo se šetati!" />
          <div className="home-button-pos">
            <Button
              className="home-button"
              variant="contained"
              color="default"
              href="/associations/all"
            >
              Kreni
            </Button>
          </div>
        </div>
      </div>
      <div className="footer">
        <div className="footer-wrapper">
          <div className="footerTitle">© Exception</div>
          <a href="https://gitlab.com/ejagodic/exception">
            <div className="gitpic"></div>
          </a>
        </div>
      </div>
    </div>
  );
}

export default home;

/* const { MediaContextProvider, Media } = createMedia({
  breakpoints: {
    mobile: 0,
    tablet: 768,
    computer: 1024,
  },
});

const HomepageHeading = () => (
  <Container className="home-container bg">
    <GridColumn>
      <Header className="display-1 h1" content="DogGO" />
      <Header className="display-4 h2" content="Lets take a walk!" />
    </GridColumn>
  </Container>
);

class DesktopContainer extends Component {
  state = {};

  render() {
    const { children } = this.props;

    return (
        <Media greaterThan="mobile">
          <HomepageHeading />
          {children}
        </Media>
    );
  }
}

class MobileContainer extends Component {
  state = {};

  render() {
    const { children } = this.props;

    return (
      <Media at="mobile">
        <HomepageHeading />
        {children}
      </Media>
    );
  }
}

const ResponsiveContainer = ({ children }) => (
  <MediaContextProvider>
    <DesktopContainer>{children}</DesktopContainer>
    <MobileContainer>{children}</MobileContainer>
  </MediaContextProvider>
);

const HomepageLayout = () => (
   <ResponsiveContainer>
     
   </ResponsiveContainer>
 );

export default HomepageLayout; */
