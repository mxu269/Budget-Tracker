import AuthService from "../services/auth.service";
import { Route, Redirect } from 'react-router-dom';

/**
 * private route func
 * @param {p} param0 
 */
function PrivateRoute({component: Component, ...rest}) {
    return (
        <Route {...rest} render={ props =>
            AuthService.isLoggedIn() ? <Component {...props} /> : <Redirect to="/signin"/>
          }
        />
      );
  }

  export default PrivateRoute;