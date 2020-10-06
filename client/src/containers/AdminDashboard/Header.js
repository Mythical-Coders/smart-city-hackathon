import React from "react";
import { useDispatch, useSelector } from "react-redux";
import PropTypes from "prop-types";
import AppBar from "@material-ui/core/AppBar";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import Grid from "@material-ui/core/Grid";
import Hidden from "@material-ui/core/Hidden";
import IconButton from "@material-ui/core/IconButton";
import Link from "@material-ui/core/Link";
import MenuIcon from "@material-ui/icons/Menu";
import NotificationsIcon from "@material-ui/icons/Notifications";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import { withStyles } from "@material-ui/core/styles";
import { stylesHeader } from "./styles/Styles";
import { logoutUser } from "../../actions/AuthActions";
import Cookies from "js-cookie";

function Header(props) {
  const { classes, onDrawerToggle } = props;
  const dispatch = useDispatch();
  const authData = useSelector((state) => state.auth);
  const handleLogout = () => {
    dispatch(logoutUser());
    Cookies.set("token", "");
  };
  return (
    <React.Fragment>
      <AppBar
        color="primary"
        style={{ backgroundColor: "#fcb000" }}
        position="sticky"
        elevation={0}
      >
        <Toolbar>
          <Grid container spacing={1} alignItems="center">
            <Hidden smUp>
              <Grid item>
                <IconButton
                  color="inherit"
                  aria-label="open drawer"
                  onClick={onDrawerToggle}
                  className={classes.menuButton}
                >
                  <MenuIcon />
                </IconButton>
              </Grid>
            </Hidden>
            <Grid item xs />

            <Grid item>
              <IconButton color="inherit" className={classes.iconButtonAvatar}>
                {authData.user.username}
              </IconButton>
            </Grid>

            <Grid item>
              <Tooltip title="Alerts • No alerts">
                <IconButton color="inherit">
                  <NotificationsIcon />
                </IconButton>
              </Tooltip>
            </Grid>
            <Grid item>
              <Link
                onClick={handleLogout}
                className={classes.link}
                href="#"
                variant="body2"
              >
                <ExitToAppIcon /> خروج
              </Link>
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>
    </React.Fragment>
  );
}

Header.propTypes = {
  classes: PropTypes.object.isRequired,
  onDrawerToggle: PropTypes.func.isRequired,
};

export default withStyles(stylesHeader)(Header);
