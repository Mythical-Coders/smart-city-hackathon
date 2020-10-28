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
import PeopleIcon from "@material-ui/icons/People";
import DnsRoundedIcon from "@material-ui/icons/DnsRounded";
// import PermMediaOutlinedIcon from "@material-ui/icons/PhotoSizeSelectActual";
import PublicIcon from "@material-ui/icons/Public";
import AccountBoxIcon from '@material-ui/icons/AccountBox';
import SettingsEthernetIcon from "@material-ui/icons/SettingsEthernet";
// import SettingsInputComponentIcon from "@material-ui/icons/SettingsInputComponent";
import TimerIcon from "@material-ui/icons/Timer";
import SettingsIcon from "@material-ui/icons/Settings";
import ReportIcon from '@material-ui/icons/Report';
import ReportProblemIcon from '@material-ui/icons/ReportProblem';
import PhonelinkSetupIcon from "@material-ui/icons/PhonelinkSetup";
import Image from "@material-ui/icons/Image";

import { stylesNavigator } from "./styles/Styles";

const defaultCategories = [
  {
    id: "جداول قاعدة البيانات",
    children: [
      {
        id: "المستخدمون",
        icon: <PeopleIcon />,
        link: "/admin_dashboard/users",
      },
      {
        id: "الحجز",
        icon: <DnsRoundedIcon />,
        link: "/admin_dashboard/impounds",
      },
      {
        id: "الأماكن",
        icon: <PublicIcon />,
        link: "/admin_dashboard/places",
      },
      {
        id: "المواطنين",
        icon: <SettingsEthernetIcon />,
        link: "/admin_dashboard/citizens",
      },
      {
        id: "ملفات تعريف المستخدمين",
        icon: <AccountBoxIcon />,
        link: "/admin_dashboard/profiles",
      },
      {
        id: "التقرير",
        icon: <ReportIcon />,
        link: "/admin_dashboard/reports",
      },
      {
        id: "مكان التقرير",
        icon: <ReportProblemIcon />,
        link: "/admin_dashboard/reportPlaces",
      },
      {
        id: "الصور",
        icon: <Image />,
        link: "/admin_dashboard/images",
      },
    ],
  },
  {
    id: "جودة",
    children: [
      { id: "تحليلات", icon: <SettingsIcon />, link: "/agent_dashboard/statistics" },
      { id: "أداء", icon: <TimerIcon />, link: "/admin_dashboard" },
      {
        id: "معمل الاختبار",
        icon: <PhonelinkSetupIcon />,
        link: "/admin_dashboard",
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
          {"\xa0\xa0\xa0\xa0\xa0\xa0\xa0لوحة التحكم"}
        </ListItem>
        {categories.map(({ id, children }) => (
          <React.Fragment key={id}>
            <ListItem className={classes.categoryHeader}>
              <ListItemText
                classes={{
                  primary: classes.categoryHeaderPrimary,
                }}
                style={{textAlign:"center"}}
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
                  
                  <ListItemText
                    classes={{
                      primary: classes.itemPrimary,
                    }}
                    style={{textAlign:"right"}}
                  >
                    {childId+"\xa0\xa0"}
                  </ListItemText>
                  <ListItemIcon className={classes.itemIcon}>
                    {icon}
                  </ListItemIcon>
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
