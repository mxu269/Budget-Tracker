import React, { Component } from 'react';
import '../styles/navbar.css';

/**
 * Footer component
 */
class Footer extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="row mb-5 justify-content-center footer">
                <h6> <small>copyright © 2020 Jerry Xu</small></h6>
            </div>
            
        )
    }
}

export default Footer;
