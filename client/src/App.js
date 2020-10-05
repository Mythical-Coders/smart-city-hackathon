import React from "react";
import { useSelector } from "react-redux";
import { Switch,  Route, Redirect } from "react-router-dom";
import CssBaseline from "@material-ui/core/CssBaseline";
import AdminDashboard from "./containers/AdminDashboard/AdminDashboard";
import SignInSide from "./containers/SignIn/SignInSide";
import AgentDashboard from "./containers/AgentDashboard/AgentDashboard";

function App() {
  const authDetail = useSelector((state) => state.auth);
  if (!authDetail.user)
    return (
      <div className="App">
        <CssBaseline />
        <Switch>
          {/* Landing page*/}
          <Route path="/signin" exact component={SignInSide} />
          <Redirect to="/signin" />
        </Switch>
      </div>
    );
  else if (authDetail.user.roles[0] === "ROLE_ADMIN")
    return (
      <div className="App">
        <AdminDashboard />
      </div>
    );
  else if (authDetail.user.roles[0] === "ROLE_RECEPTIVE")
    return (
      <div className="App">
        <AgentDashboard />
      </div>
    );
 
}

export default App;
