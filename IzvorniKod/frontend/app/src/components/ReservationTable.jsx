import React from "react";
import PropTypes from "prop-types";
import { makeStyles } from "@material-ui/core/styles";
import Box from "@material-ui/core/Box";
import Collapse from "@material-ui/core/Collapse";
import IconButton from "@material-ui/core/IconButton";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Typography from "@material-ui/core/Typography";
import Paper from "@material-ui/core/Paper";
import KeyboardArrowDownIcon from "@material-ui/icons/KeyboardArrowDown";
import KeyboardArrowUpIcon from "@material-ui/icons/KeyboardArrowUp";
import defaultDog from "./../static/dog.jpg";
import Button from "@material-ui/core/Button";
import { authHeader } from "./../helpers/authHeader";
//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";

const useRowStyles = makeStyles({
  root: {
    "& > *": {
      borderBottom: "unset",
    },
  },
});

function Row(props) {
  const { row } = props;
  const [open, setOpen] = React.useState(false);
  const classes = useRowStyles();

  return (
    <React.Fragment>
      <TableRow className={classes.root}>
        <TableCell>
          <IconButton
            aria-label="expand row"
            size="small"
            onClick={() => setOpen(!open)}
          >
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        <TableCell component="th" scope="row">
          {row.reservationId}
        </TableCell>
        <TableCell align="right">{row.date}</TableCell>
        <TableCell align="right">{row.walker.username}</TableCell>
        <TableCell align="right">{row.walkStyle}</TableCell>
        <TableCell align="right">{row.dogs[0].associationName}</TableCell>
        {row.cancelled == false ? (
          <TableCell>
            <Button
              variant="contained"
              color="secondary"
              onClick={async () =>
                fetch(
                  `${BACKEND_URL}/reservations/cancel?reservationID=${row.reservationId}`,
                  {
                    method: "GET",
                    headers: {
                      "Content-Type": "application/json; charset=UTF-8",
                      Authorization: authHeader(),
                    },
                  },
                  {
                    method: "DELETE",
                    headers: {
                      "Content-Type": "application/json; charset=UTF-8",
                    },
                  }
                ).then((json) => {
                  json.json().then((response) => {
                    console.log(response);
                    window.location.reload(false);
                  });
                })
              }
            >
              OTKAŽI
            </Button>
          </TableCell>
        ) : (
          ""
        )}
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box margin={1}>
              <Typography variant="h6" gutterBottom component="div">
                Detalji
              </Typography>
              <Table size="small" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    <TableCell>Vrijeme početka</TableCell>
                    <TableCell>Vrijeme kraja</TableCell>
                    <TableCell>Peseki</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  <TableRow>
                    <TableCell>{row.startTime}</TableCell>
                    <TableCell>{row.returnTime}</TableCell>
                    <TableCell>
                      {row.dogs.map((dog) => (
                        <TableRow key={dog.dogId}>
                          <TableCell>{dog.dogId}</TableCell>
                          <TableCell component="th" scope="row">
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
                          </TableCell>
                          <TableCell align="left">{dog.name}</TableCell>
                        </TableRow>
                      ))}
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </React.Fragment>
  );
}

export default function CollapsibleTable(props) {
  const { rows } = props;
  return (
    <TableContainer component={Paper}>
      <Table aria-label="collapsible table">
        <TableBody>
          {rows.map((row) => (
            <Row key={row.reservationId} row={row} />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
