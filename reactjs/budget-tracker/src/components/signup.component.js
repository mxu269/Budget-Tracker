import React, { Component } from 'react';
import '../styles/signin.css'
import logo from '../assets/logo.png';

import AuthService from "../services/auth.service";

/**
 * Signup component
 */
class Signup extends Component {
    constructor(props) {
        super(props);

        this.state = {
            name: '',
            username: '',
            password: '',
            message: '',
            loading: false,
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        let newState = Object.assign({}, this.state);
        newState[name] = value;

        this.setState(newState);
    }

    handleSubmit(event) {
        event.preventDefault();

        let newState = Object.assign({}, this.state);
        newState.message = '';
        newState.loading = true;

        this.setState(newState);

        AuthService.signup(this.state.name, this.state.username, this.state.password)
        .then((result) => {
                console.log(result);
                this.props.history.push("/dashboard");
                window.location.reload();
            }
        )
        .catch(error => {
            if (error.response) {
                this.setState({
                    message: error.response.data,
                });
            } else {
                this.setState({
                    message: 'server connection failed',
                });
            }
                
            }
        )
        .finally(() => {
                this.setState({
                    loading: false,
                });
            }
        );


    }

    render() {
        return (
        <div className="text-center center">
        <img className="mb-4" src={logo} alt="" width="72" height="72"/>
            <h1 className="h3 mb-3 font-weight-normal">Please Sign up</h1>
            <form className="form-signin" onSubmit={this.handleSubmit}>

                <input 
                    type="text"
                    name="name" 
                    className="form-control signin" 
                    placeholder="Name"
                    onChange={this.handleChange} 
                    required 
                    autoFocus />

                <br/>

                <input 
                    type="username"
                    name="username" 
                    className="form-control signin" 
                    placeholder="Username"
                    onChange={this.handleChange} 
                    required />
                <input 
                    type="password" 
                    name="password" 
                    className="form-control signin" 
                    placeholder="Password" 
                    required
                    onChange={this.handleChange} />

                {
                    this.state.message && (
                        <div className="alert alert-danger" role="alert">
                            <p>{this.state.message}</p>
                        </div>
                        )
                }    
                
                <a href="/signin">
                    Already have an account?
                </a>

                <button
                    type="submit"
                    className="btn btn-primary btn-block btn-smbt"
                    disabled={this.state.loading}
                >
                    Sign up
                </button>

                
            </form>
        </div>
        )
    }
}

export default Signup;
