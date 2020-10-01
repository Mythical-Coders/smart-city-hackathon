import React from "react";
// import { useSelector } from "react-redux";
// import { Switch, Redirect, Route } from "react-router-dom";
// import CssBaseline from "@material-ui/core/CssBaseline";
import AdminDashboard from "./containers/AdminDashboard/AdminDashboard";


function App() {
  // const authDetail = useSelector((state) => state.auth);
  // if (!authDetail.user)
  //   return (
  //     <div className="App">
  //       <CssBaseline />
  //       <Switch>
  //          {/* Landing page*/}
  //         <Route path="/" exact component={LandingPage} />
  //         <Route path="/signin" exact component={SignIn} />
  //       </Switch>
  //     </div>
  //   );
  // else if (authDetail.user.roles[0] === "ROLE_ADMIN")
    return (
      <div className="App">
        <AdminDashboard />
      </div>
    );
  // else
  //   return (
  //     <div className="App">
  //       <CssBaseline />
  //       <nav>
  //         <NavBar />
  //       </nav>
  //       <Switch>
  //         {/* Landing */}
  //         <Route path="/" exact component={LandingPage} />
  //         {/* dashboard page*/}
  //         <Route path="/dashboard" exact component={DashboardPage} />
  //         {/* authentication pages*/}
  //         <Route path="/signup" exact component={SignUp} />
  //         <Route path="/signin" exact component={SignIn} />
  //         {/* condidat profile pages */}
  //         <Route path={"/profile"} exact component={ProfilePage} />
  //         <Route path={"/account"} exact component={AccountPage} />
  //         {/* formations pages */}
  //         <Route path={"/formations"} exact component={FormationPage} />
  //         {/* default */}
  //         <Route path={"/default"} exact component={DefaultPage} />

  //         <Route path="/test" exact component={TestNiveauAdmin} />
  //         <Route path="/uploadRessource" exact component={RessourcePage} />

  //         <WiemRoutes />
  //         <RanimRoutes />
  //         {/* redirect */}
  //         <Redirect to={"/"} />
  //       </Switch>
  //     </div>
  //   );
}

export default App;
