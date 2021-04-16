import React from "react";
import "./../static/walkers.css";
import { Link, Redirect } from "react-router-dom";
import { Table, Input, Button, Space } from "antd";
import Highlighter from "react-highlight-words";
import { SearchOutlined } from "@ant-design/icons";
//import Autocomplete, { createFilterOptions } from '@material-ui/lab/Autocomplete';
import { authHeader } from "./../helpers/authHeader";

const { render } = require("react-dom");
//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";

class NewWalkers extends React.Component {
  state = {
    loaded: false,
    setaci: [],
    currentIndex: null,
    searchText: "",
    searchedColumn: "",
    redirect: false,
  };

  getColumnSearchProps = (dataIndex) => ({
    filterDropdown: ({
      setSelectedKeys,
      selectedKeys,
      confirm,
      clearFilters,
    }) => (
      <div style={{ padding: 8 }}>
        <Input
          ref={(node) => {
            this.searchInput = node;
          }}
          placeholder={`Search ${dataIndex}`}
          value={selectedKeys[0]}
          onChange={(e) =>
            setSelectedKeys(e.target.value ? [e.target.value] : [])
          }
          onPressEnter={() =>
            this.handleSearch(selectedKeys, confirm, dataIndex)
          }
          style={{ width: 188, marginBottom: 8, display: "block" }}
        />
        <Space>
          <Button
            type="primary"
            onClick={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
            icon={<SearchOutlined />}
            size="small"
            style={{ width: 90 }}
          >
            Search
          </Button>
          <Button
            onClick={() => this.handleReset(clearFilters)}
            size="small"
            style={{ width: 90 }}
          >
            Reset
          </Button>
        </Space>
      </div>
    ),
    filterIcon: (filtered) => (
      <SearchOutlined style={{ color: filtered ? "#1890ff" : undefined }} />
    ),
    onFilter: (value, record) =>
      record[dataIndex]
        ? record[dataIndex]
            .toString()
            .toLowerCase()
            .includes(value.toLowerCase())
        : "",
    onFilterDropdownVisibleChange: (visible) => {
      if (visible) {
        setTimeout(() => this.searchInput.select(), 100);
      }
    },
    render: (text) =>
      this.state.searchedColumn === dataIndex ? (
        <Highlighter
          highlightStyle={{ backgroundColor: "#ffc069", padding: 0 }}
          searchWords={[this.state.searchText]}
          autoEscape
          textToHighlight={text ? text.toString() : ""}
        />
      ) : (
        text
      ),
  });

  handleSearch = (selectedKeys, confirm, dataIndex) => {
    confirm();
    this.setState({
      searchText: selectedKeys[0],
      searchedColumn: dataIndex,
    });
  };

  handleReset = (clearFilters) => {
    clearFilters();
    this.setState({ searchText: "" });
  };
  componentDidMount() {
    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
    };
    fetch(`${BACKEND_URL}/walker/ranking`, options).then((response) => {
      const newArray = { walkers: [] };
      response.json().then((json) => {
        console.log(json);
        Array.prototype.forEach.call(json, (el) => {
          console.log(el);
          const newData = {
            id: "",
            name: "",
            br_setnji: "",
            br_pasa: "",
            duljina_setnji: "",
          };
          newData.name = el.fullName;
          newData.id = el.walkerId;
          newData.br_pasa = el.numOfDogs;
          newData.br_setnji = el.numOfWalks;
          newData.duljina_setnji = el.totalDuration;
          newArray.walkers.push(newData);
        });
        this.setState({
          setaci: newArray.walkers,
        });
        console.log(this.state);
      });
    });
  }

  render() {
    console.log(this.state.udruge);

    const columns = [
      {
        key: "name",
        numeric: false,
        disablePadding: true,
        title: "",
        dataIndex: "name",
        ...this.getColumnSearchProps("name"),
      },
      {
        id: "br_setnji",
        numeric: true,
        disablePadding: false,
        title: "Broj šetnji",
        dataIndex: "br_setnji",
        key: "br_setnji",
        defaultSortOrder: "descend",
        sorter: (a, b) => a.br_setnji - b.br_setnji,
      },
      ,
      {
        id: "br_pasa",
        numeric: true,
        disablePadding: false,
        title: "Broj pasa",
        dataIndex: "br_pasa",
        key: "br_pasa",
        defaultSortOrder: "descend",
        sorter: (a, b) => a.br_pasa - b.br_pasa,
      },
      {
        id: "duljina_setnji",
        numeric: true,
        disablePadding: false,
        title: "Duljina šetnji",
        dataIndex: "duljina_setnji",
        key: "duljina_setnji",
        defaultSortOrder: "descend",
        sorter: (a, b) => a.duljina_setnji - b.duljina_setnji,
      },
      //   {
      //     title: "Detalji udruge",
      //     key: "operation",
      //     fixed: "right",
      //     width: 100,
      //     render: (data) => (
      //       <Button
      //         onClick={async () =>
      //           fetch(`${BACKEND_URL}/association?associationId=` + data.id).then(
      //             (response) => {
      //               if (response.status !== 400) {
      //                 response.json().then((json) => {
      //                   console.log(json);
      //                   this.props.history.push("/association/" + data.id);
      //                 });
      //               }
      //             }
      //           )
      //         }
      //       >
      //         Pogledaj više
      //       </Button>
      //     ),
      //   },
    ];
    console.log(this.state.korisnici);
    /**
    const filterOptions = createFilterOptions({
      matchFrom: 'start',
      stringify: option => option.title,
    });
    */
    // <Autocomplete filterOptions={filterOptions} />
    console.log(this.props.name);

    return (
      <div>
        <div className="walkers-header">
          <div className="walkersTitle">
            <span>... najviše se šetaju</span>
          </div>
          <div className="paws-img"></div>
        </div>
        <Table
          className="walkers-table"
          columns={columns}
          dataSource={this.state.setaci}
          style={{ margin: "2%" }}
        />
      </div>
    );
  }
}

//<Udruga currentIndex={this.state.currentIndex} />
export default NewWalkers;
