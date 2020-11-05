import React, { useEffect, useState } from "react";
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
// import Tooltip from "@material-ui/core/Tooltip";
import { withStyles } from "@material-ui/core/styles";
import { stylesHeader } from "./styles/Styles";
import { logoutUser } from "../../actions/AuthActions";
import Cookies from "js-cookie";
import { notificationGetDataReceiver } from "../../actions/NotificationActions";
import CustomDropdown from "../../components/CustomDropdown/CustomDropdown";
import { NavLink } from "react-router-dom";
function Header(props) {
  const { classes, onDrawerToggle } = props;
  const dispatch = useDispatch();
  const authData = useSelector((state) => state.auth);
  const notyData = useSelector((state) => state.notification);
  const [dataNoSeen, setDataNoSeen] = useState([]);
  const [countNoti, setCountNoti] = useState(0);
  const handleLogout = () => {
    dispatch(logoutUser());
    Cookies.set("token", "");
  };
  useEffect(() => {
    if (!notyData.data) dispatch(notificationGetDataReceiver(authData.user.id));
    setInterval(
      () => dispatch(notificationGetDataReceiver(authData.user.id)),
      30000
    );
  }, [setInterval]); // eslint-disable-line react-hooks/exhaustive-deps
  useEffect(() => {
    if (notyData.data) {
      if (notyData.data.length !== dataNoSeen.length) {
        let dataNoSeenArray = [];
        notyData.data.forEach((item) => {
          if (item.seen === false) dataNoSeenArray = [...dataNoSeenArray, item];
        });
        setDataNoSeen(dataNoSeenArray.sort((a, b) => (a.date > b.date ? -1 : 1)));
        setCountNoti(dataNoSeenArray.length);
      }
    }
  }, [notyData]); // eslint-disable-line react-hooks/exhaustive-deps
  const dataNoSeenMap = () => {
    let list = [];
    if (dataNoSeen) {
      dataNoSeen.forEach((item) => {
        list.push(
          <NavLink
            key={item.id}
            to={"/admin_dashboard/notifications/" + item.id}
            className={classes.dropdownLink}
          >
            {item.type+" "+item.title+" "+item.date}
          </NavLink>
        );
      });
      list.push(
        <NavLink
          key={"seeAll"}
          to={"/admin_dashboard/notifications"}
          className={classes.dropdownLink}
        >
          See All
        </NavLink>
      );
      return list;
    } else {
      list.push("No new noti");
      list.push(
        <NavLink
          key={"seeAll"}
          to={"/admin_dashboard/notifications"}
          className={classes.dropdownLink}
        >
          See All
        </NavLink>
      );
    }
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
            {/* <Tooltip
              title={
                countNoti > 0
                  ? "  عدد الإشعارات " + countNoti
                  : "لا يوجد إشعارات"
              }
            > */}
              <Grid item>
                <CustomDropdown
                  hoverColor="info"
                  noLiPadding
                  buttonProps={{
                    className: classes.navLink,
                    color: countNoti > 0 ? "info" : "transparent",
                  }}
                  buttonText={countNoti > 10 ? "+10" : countNoti}
                  buttonIcon={NotificationsIcon}
                  dropdownList={dataNoSeenMap()}
                />
              </Grid>
            {/* </Tooltip> */}

            <Grid item>
              <IconButton color="inherit" className={classes.iconButtonAvatar}>
                {authData.user.username}
              </IconButton>
            </Grid>
            <Grid item xs />
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
