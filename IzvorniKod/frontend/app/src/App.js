import './App.css';
import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Provider } from "react-redux";
import { PersistGate } from 'redux-persist/integration/react';

import Home from './components/Home';
import Udruge from './components/Udruge';
import Registration from './components/registrationForm';
import Login from './components/login';
import Navigation from './components/Navigation';
import Explore from './components/Explore';
import Walkers from './components/NewWalkers';
import UserProfile from './components/UserProfile';
import AdminProfile from './components/AdminProfile';
import EditAvailability from './components/dog/EditAvailabilityForm';
import AssociationProfile from './components/AssociationProfile';
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Dog from './components/Dog';
import { store, persistor } from './redux/store';
import AddDog from './components/dog/AddDog';
import DogEditForm from './components/dog/DogEditForm';

function App() {

  return (
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <Router>
          <Navigation />
          <Switch>
            <Route exact path="/" component={Home} />
            <Route path='/home' component={Home} exact />
            <Route path='/associations/all' component={Udruge} />
            <Route path='/association/:id' component={AssociationProfile} />
            <Route path='/walkers' component={Walkers} />
            <Route path='/explore' component={Explore} />
            <Route path='/registration' component={Registration} />
            <Route path='/login' component={Login} />
            <Route path='/userprofile' component={UserProfile} />
            <Route path='/associationprofile' component={AssociationProfile} />
            <Route path='/adminprofile' component={AdminProfile} />
            <Route path='/editDog' component={DogEditForm} />
            <Route path='/dog' component={Dog} />
            <Route path='/edit' component={EditAvailability} />
          </Switch>
        </Router>
      </PersistGate>
    </Provider>
  );
}
export default App;