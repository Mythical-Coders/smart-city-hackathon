import React from "react";
import PropTypes from "prop-types";
import { ThemeProvider, withStyles } from "@material-ui/core/styles";
import CssBaseline from "@material-ui/core/CssBaseline";
// import Hidden from "@material-ui/core/Hidden";
// import Navigator from "./Navigator";
import Header from "./Header";
import { stylesPaperbase, theme, Copyright } from "./styles/Styles";
import { Switch, Redirect, Route } from "react-router-dom";
import ImpoundList from "./Content/ImpoundList";
import EditImpound from "./Content/EditImpound";



// const drawerWidth = 256;

function AgentDashboard(props) {
  const { classes } = props;
  const [mobileOpen, setMobileOpen] = React.useState(false);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  return (
    <ThemeProvider theme={theme}>
      <div className={classes.root}>
        <CssBaseline />
        {/* <nav className={classes.drawer}>
          <Hidden smUp implementation="js">
            <Navigator
              PaperProps={{ style: { width: drawerWidth } }}
              variant="temporary"
              open={mobileOpen}
              onClose={handleDrawerToggle}
            />
          </Hidden>
          <Hidden xsDown implementation="css">
            <Navigator PaperProps={{ style: { width: drawerWidth } }} />
          </Hidden>
        </nav> */}
        <div className={classes.app}>
          <Header onDrawerToggle={handleDrawerToggle} />
          <main className={classes.main}>
            <Switch>
              <Route path={"/agent_dashboard/impounds"}  component={ImpoundList} />
              <Route path={"/agent_dashboard/impound/:id"}  component={EditImpound} />

              <Redirect to={"/agent_dashboard/impounds"} />
            </Switch>
          </main>
          <footer className={classes.footer}>
            <Copyright />
          </footer>
        </div>
      </div>
    </ThemeProvider>
  );
}

AgentDashboard.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesPaperbase)(AgentDashboard);
