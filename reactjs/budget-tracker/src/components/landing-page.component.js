import React, { Component } from 'react';
import { Redirect } from "react-router-dom";
import AuthService from "../services/auth.service";

/**
 * landing page component. redirect to sign up
 */
class Landing extends Component {
    constructor(props) {
        super(props);

        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.props.history.push("/signup");
        window.location.reload();
    }

    render() {
        return !AuthService.isLoggedIn() ? (
            <div className="text-center mt-5">
                <h1>Welcome to Budget Tracker!</h1>
                <button
                        type="submit mt-4"
                        className="btn btn-primary btn-block btn-smbt col-2 mx-auto"
                        onClick={this.handleClick}
                    >
                        Sign Up
                </button>
            </div>
        ) : <Redirect to="/dashboard" />
    }
}

export default Landing;
