import React from "react";
import PropTypes from "prop-types";
import { ThemeProvider, withStyles } from "@material-ui/core/styles";
import CssBaseline from "@material-ui/core/CssBaseline";
import Hidden from "@material-ui/core/Hidden";
import Navigator from "./Navigator";
import UserList from "./Content/UserList";
import Header from "./Header";
import { stylesPaperbase, theme, Copyright } from "./styles/Styles";
import { Switch, Redirect, Route } from "react-router-dom";
import ImpoundList from "./Content/ImpoundList";
import PlaceList from "./Content/PlaceList";
import CitizenList from "./Content/CitizenList";
import ProfileList from "./Content/ProfileList";
import ReportList from "./Content/ReportList";
import ReportPlaceList from "./Content/ReportPlaceList";
import ImageList from "./Content/ImageList";
import ImageDetail from "./Content/ImageDetail";
import Statictics from "../Statistics/Statictics";

const drawerWidth = 256;

function AdminDashboard(props) {
  const { classes } = props;
  const [mobileOpen, setMobileOpen] = React.useState(false);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  return (
    <ThemeProvider theme={theme}>
      <div className={classes.root}>
        <CssBaseline />

        <div className={classes.app}>
          <Header onDrawerToggle={handleDrawerToggle} />
          <main className={classes.main}>
            <Switch>
              <Route
                path={"/admin_dashboard/users"}
                exact
                component={UserList}
              />
              <Route
                path={"/admin_dashboard/impounds"}
                exact
                component={ImpoundList}
              />
              <Route
                path={"/admin_dashboard/places"}
                exact
                component={PlaceList}
              />
              <Route
                path={"/admin_dashboard/citizens"}
                exact
                component={CitizenList}
              />
              <Route
                path={"/admin_dashboard/profiles"}
                exact
                component={ProfileList}
              />
              <Route
                path={"/admin_dashboard/reports"}
                exact
                component={ReportList}
              />
              <Route
                path={"/admin_dashboard/reportPlaces"}
                exact
                component={ReportPlaceList}
              />
              <Route
                path={"/admin_dashboard/images"}
                exact
                component={ImageList}
              />
              <Route
                path={"/agent_dashboard/images/:id"}
                component={ImageDetail}
              />
              <Route
                path={"/agent_dashboard/statistics"}
                component={Statictics}
              />

              <Redirect to={"/admin_dashboard/users"} />
            </Switch>
          </main>
          <footer className={classes.footer}>
            <Copyright />
          </footer>
        </div>
        <nav className={classes.drawer}>
          <Hidden smUp implementation="js">
            <Navigator
              PaperProps={{ style: { width: drawerWidth } }}
              variant="temporary"
              open={mobileOpen}
              onClose={handleDrawerToggle}
              anchor="right"
            />
          </Hidden>
          <Hidden xsDown implementation="css">
            <Navigator
              PaperProps={{ style: { width: drawerWidth } }}
              anchor="right"
            />
          </Hidden>
        </nav>
      </div>
    </ThemeProvider>
  );
}

AdminDashboard.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesPaperbase)(AdminDashboard);
