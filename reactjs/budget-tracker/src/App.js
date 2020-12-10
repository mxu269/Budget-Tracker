import React, { Component } from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Login from './components/login.component';
import Dashboard from './components/dashboard.component';
import Filter from './components/filter.component'
import Signup from './components/signup.component';
import Navbar from './components/navbar.component';
import NewTransaction from './components/new-transaction.component';
import PrivateRoute from './js/PrivateRoute';
import Transaction from './components/transaction.component';
import PageNotFound from './components/page-not-found.component';
import TransactionTable from './components/transaction-table.component';
import Search from './components/search.component';
import Landing from './components/landing-page.component';
import Footer from './components/footer.component';
import About from './components/about.component';

/**
 * defines routes in the web app, also with navbar and footer
 */
class App extends Component {

  render() {
    return (
      <div className="App">
        <Router>
            <Navbar/>
            <Switch>
              <Route exact path="/" component={Landing} />
              <Route exact path="/signin" component={Login} />
              <Route exact path="/signup" component={Signup} />
              <PrivateRoute exact path="/dashboard" component={Dashboard} />
              <Route exact path="/filter" component={Filter} />
              <PrivateRoute exact path="/new" component={NewTransaction} />
              <PrivateRoute exact path="/transaction/:id" component={Transaction} />
              <Route exact path="/page-not-found" component={PageNotFound} />
              <Route exact path="/cell" component={TransactionTable} />
              <Route exact path="/search" component={Search} />
              <Route exact path="/about" component={About} />
              <Route path="/" component={PageNotFound} />
            </Switch>
            <Footer />
        </Router>
      </div>
    );
  }
}

export default App;
