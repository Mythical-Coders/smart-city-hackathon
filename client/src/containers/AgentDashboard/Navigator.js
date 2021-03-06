import React, { useState } from "react";
import { NavLink } from "react-router-dom";
import PropTypes from "prop-types";
import clsx from "clsx";
import { withStyles } from "@material-ui/core/styles";
import Divider from "@material-ui/core/Divider";
import Drawer from "@material-ui/core/Drawer";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
// import PeopleIcon from "@material-ui/icons/People";
import DnsRoundedIcon from "@material-ui/icons/DnsRounded";
// import PermMediaOutlinedIcon from "@material-ui/icons/PhotoSizeSelectActual";
// import PublicIcon from "@material-ui/icons/Public";
// import SettingsEthernetIcon from "@material-ui/icons/SettingsEthernet";
// import SettingsInputComponentIcon from "@material-ui/icons/SettingsInputComponent";
import TimerIcon from "@material-ui/icons/Timer";
import SettingsIcon from "@material-ui/icons/Settings";
import PhonelinkSetupIcon from "@material-ui/icons/PhonelinkSetup";
import { stylesNavigator } from "./styles/Styles";

const defaultCategories = [
  {
    id: "جداول قاعدة البيانات",
    children: [
      {
        id: "الحجز",
        icon: <DnsRoundedIcon />,
        link: "/agent_dashboard/impounds",
      },
    ],
  },
  {
    id: "جودة",
    children: [
      { id: "تحليلات", icon: <SettingsIcon />, link: "/agent_dashboard" },
      { id: "أداء", icon: <TimerIcon />, link: "/agent_dashboard" },
      {
        id: "معمل الاختبار",
        icon: <PhonelinkSetupIcon />,
        link: "/agent_dashboard",
      },
    ],
  },
];
function Navigator(props) {
  const [categories, setCategories] = useState(defaultCategories);
  const { classes, ...other } = props;
  const handleNavigation = (id, childId) => {
    let newCategories = defaultCategories;
    setCategories(
      newCategories.map((item) => {
        if (item.id === id) {
          return {
            ...item,
            children: item.children.map((child) => {
              if (child.id === childId) return { ...child, active: true };
              else return { ...child };
            }),
          };
        } else return { ...item };
      })
    );
  };
  return (
    <Drawer variant="permanent" {...other}>
      <List disablePadding>
        <ListItem
          className={clsx(classes.firebase, classes.item, classes.itemCategory)}
        >
          لوحة التحكم
        </ListItem>
        {categories.map(({ id, children }) => (
          <React.Fragment key={id}>
            <ListItem className={classes.categoryHeader}>
              <ListItemText
                classes={{
                  primary: classes.categoryHeaderPrimary,
                }}
              >
                {id}
              </ListItemText>
            </ListItem>
            {children.map(({ id: childId, icon, active, link }) => (
              <NavLink
                to={link}
                key={childId}
                className={clsx(classes.link, active && classes.itemActiveItem)}
                onClick={() => handleNavigation(id, childId)}
              >
                <ListItem
                  className={clsx(
                    classes.item,
                    active && classes.itemActiveItem
                  )}
                >
                  <ListItemIcon className={classes.itemIcon}>
                    {icon}
                  </ListItemIcon>
                  <ListItemText
                    classes={{
                      primary: classes.itemPrimary,
                    }}
                  >
                    {childId}
                  </ListItemText>
                </ListItem>
              </NavLink>
            ))}

            <Divider className={classes.divider} />
          </React.Fragment>
        ))}
      </List>
    </Drawer>
  );
}

Navigator.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesNavigator)(Navigator);
