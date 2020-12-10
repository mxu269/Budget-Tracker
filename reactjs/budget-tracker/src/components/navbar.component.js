import React, { Component } from 'react';
import logo from '../assets/logo.png';
import '../styles/navbar.css';
import {SignOutIcon, XCircleIcon, PlusCircleIcon, PersonIcon, SignInIcon} from '@primer/octicons-react';

import AuthService from "../services/auth.service";

/**
 * Nav bar component. Workds great on mobile too!
 */
class Navbar extends Component {
    constructor(props) {
        super(props);

        this.state = {
            name: 'My Account',
        }

        this.handleSignout = this.handleSignout.bind(this);
    }

    componentDidMount() {
        this.setState({ 
            name: AuthService.getName()
        })
        console.log(AuthService.isLoggedIn())
    }

    handleSignout() {
        console.log("hello");
        AuthService.signout();
        window.location.href= process.env.PUBLIC_URL + '/';
    }

    render() {
        return (
            <nav className="navbar navbar-expand-md navbar-light bg-light fixed-top">
                <a className="navbar-brand" href={process.env.PUBLIC_URL + '/'}>
                <img src={logo} width="30" height="30" className="mr-1 d-inline-block align-top" alt=""/>
                    Budget Tracker
                </a>

                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse justify-content-end" id="navbarNav">
                    <ul className="navbar-nav"> 

                        {
                            AuthService.isLoggedIn() &&
                            <li className="nav-item">
                                <a className="nav-link" href={process.env.PUBLIC_URL + '/dashboard'}>Dashboard</a>
                            </li>
                        }    
                        {
                            AuthService.isLoggedIn() &&
                        <li className="nav-item">
                            <a className="nav-link" href={process.env.PUBLIC_URL + '/search'}>Search</a>
                        </li>
                        }
                        <li className="nav-item">
                            <a className="nav-link" href={process.env.PUBLIC_URL + '/about'}>About</a>
                        </li>
                    </ul>

                    <ul className="navbar-nav ml-auto mr-1">

                        {
                            AuthService.isLoggedIn() &&
                            <li className="nav-item">
                                <a className="nav-link" href={process.env.PUBLIC_URL + '/new'}>
                                    <PlusCircleIcon className="mr-2" size={16} />
                                    Add Transaction
                                </a>
                            </li>
                        }
                        
                        {
                            AuthService.isLoggedIn() ? 
                            <li className="nav-item dropdown">
                                <span className="nav-link dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <PersonIcon className="mr-2" size={16} />
                                    {this.state.name}
                                </span>
                                <div className="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                                <span className="dropdown-item" onClick={this.handleSignout}>
                                    <SignOutIcon className="mr-2" size={16} />
                                    Sign Out
                                </span>
                                {/* <span className="dropdown-item text-danger">
                                    <XCircleIcon className="mr-2" size={16} />
                                    Delete Account
                                </span> */}
                                </div>
                            </li> :
                            <li className="nav-item">
                                <a className="nav-link" href={process.env.PUBLIC_URL + '/signin'}>
                                    <SignInIcon className="mr-2" size={16} />
                                    Sign In
                                </a>
                            </li>
                        }
                        
                    </ul>
                </div>

            </nav>
        )
    }
}

export default Navbar;
