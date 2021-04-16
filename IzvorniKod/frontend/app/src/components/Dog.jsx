import React, { Component } from 'react';
import doggo from './../static/avatardoggo.png';

class Dog extends React.Component {

    constructor(props){
        super(props);

        this.state = {
            dog : {
                id: 1,
                name: "Fuzzy",
                breed: "pudlica",
                pictureUrl: doggo,
                walkStyle: "individualna",
                description: "hjksahf jsadfhksh hsdafskfh jkasfhoirehadj joeadh fdasfeoahdf dsafoehfkds feoask"
            }
        };
    }

    render(){
        return (
            <div className="dog-list d-flex flex-row bd-highlight mb-3 align-items-center" id={this.state.dog.id}>
                <div className="p-2 w-50 bd-highlight">
                    <img src={this.state.dog.pictureUrl} alt="" width="100px" className="rounded-sm ml-n2" />
                </div>
                <div className="p-2 w-30 bd-highlight">
                    <div className="text-dark font-weight-600">{this.state.dog.name}</div>
                    <div className="text-muted fs-5px">{this.state.dog.breed}</div>
                </div>
                <div className="p-2 flex-fill bd-highlight">
                    <div className="text-muted fs-5px">{this.state.dog.description}</div>
                </div>
                <div className="p-2 w-30 bd-highlight">
                    <div className="text-dark font-weight-600">Vrsta šetnje</div>
                    <div className="text-muted fs-5px">{this.state.dog.walkStyle}</div>
                </div>
                <div id="button" className="p-2 w-30 bd-highlight">
                    <button className="btn btn-outline-primary">UREDI</button>
                    <button className="btn btn-outline-primary">REZERVIRAJ</button>
                </div>
            </div>
        )
    }
}

export default Dog;