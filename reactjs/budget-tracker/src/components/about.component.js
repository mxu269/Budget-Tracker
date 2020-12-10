import React, { Component } from 'react';
import { Redirect } from "react-router-dom";

/**
 * basic component for copy. This is a basic react component
 */
class About extends Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        window.location = "https://flipgrid.com/d6f8a516"
    }

    render() {
        return (
            <div calssName="container">
                <h2 className="mt-4 mb-4 text-center">Redirecting...</h2>
            </div>
        )
    }
}

export default About;
