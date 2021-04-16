import "./../static/search.css";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
import ToolkitProvider, {
  Search,
  CSVExport,
} from "react-bootstrap-table2-toolkit";
import filterFactory, {
  textFilter,
  numberFilter,
} from "react-bootstrap-table2-filter";
import React, { useEffect } from "react";
const { SearchBar, ClearSearchButton } = Search;
const { ExportCSVButton } = CSVExport;
//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";

const columns = [
  {
    dataField: "id",
    text: "id",
    hidden: true,
  },
  {
    dataField: "name",
    text: "Ime Šetača",
  },
  {
    dataField: "br_setnji",
    text: "Broj Šetnji",
    sort: true,
    filter: numberFilter(),
  },
  {
    dataField: "br_pasa",
    text: "Broj Pasa",
    sort: true,
    filter: numberFilter(),
  },
  {
    dataField: "duljina_setnji",
    text: "Duljina Šetnji",
    sort: true,
    filter: numberFilter(),
  },
];

const defaultSorted = [
  {
    dataField: "name",
    order: "desc",
  },
];

const MyExportCSV = (props) => {
  const handleClick = () => {
    props.onExport();
  };
  return (
    <div>
      <button className="btn btn-success" onClick={handleClick}>
        Export to CSV
      </button>
    </div>
  );
};

function SearchApp() {
  const [data, setData] = React.useState({
    walkers: [],
  });

  useEffect(() => {
    fetch(`${BACKEND_URL}/walker/all`).then((response) => {
      const newArray = { walkers: [] };
      response.json().then((json) => {
        json.forEach((el) => {
          const newData = {
            id: "",
            name: "",
            br_setnji: "",
            br_pasa: "",
            duljina_setnji: "",
          };
          newData.name = el.firstName + " " + el.lastName;
          newData.id = el.id;
          // newData.br_pasa = el.br_pasa;
          // newData.br_setnji = el.br_setnji;
          // newData.duljina_setnji = el.duljina_setnji;
          newArray.walkers.push(newData);
        });
        setData(newArray);
      });
    });
  });

  return (
    <div>
      <div className="text-center mx-auto">
        <span className="istaknuti-setaci"> Istaknuti Šetači </span>
      </div>
      <div>
        <ToolkitProvider
          search
          keyField="id"
          data={data.walkers}
          columns={columns}
          exportCSV={{ onlyExportFiltered: true, exportAll: false }}
        >
          {(props) => (
            <div>
              <BootstrapTable
                {...props.baseProps}
                version="4"
                defaultSorted={defaultSorted}
                filter={filterFactory()}
                pagination={paginationFactory()}
                striped
                hover
                condensed
              />
              <MyExportCSV {...props.csvProps}>Export Data</MyExportCSV>
            </div>
          )}
        </ToolkitProvider>
      </div>
    </div>
  );
}
export default SearchApp;
