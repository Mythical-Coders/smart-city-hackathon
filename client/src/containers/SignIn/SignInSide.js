import React, { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { signIn } from "../../actions/AuthActions";
import { Redirect } from "react-router-dom";
import CssBaseline from "@material-ui/core/CssBaseline";
import Paper from "@material-ui/core/Paper";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Link from "@material-ui/core/Link";
import Grid from "@material-ui/core/Grid";
import Box from "@material-ui/core/Box";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import CircularProgress from "@material-ui/core/CircularProgress";
import { useStyles, Copyright } from "./Style";

export default function SignInSide() {
  const classes = useStyles();
  const dispatch = useDispatch();
  const state = useSelector((state) => state.auth);
  const [username, setusername] = useState("");
  const [password, setpassword] = useState("");
  const [nameErr, setnameErr] = useState("");
  const [passwordErr, setpasswordErr] = useState("");

  const handleChange = (e, name) => {
    const user = {};
    user[name] = e.target.value;
    // validations
    switch (name) {
      case "username":
        setusername(user.username);
        user.username.length < 3
          ? setnameErr("Name must be at least 3 characters!")
          : setnameErr("");
        break;
      case "password":
        setpassword(user.password);
        user.password.length < 8
          ? setpasswordErr("Password must be at least 8 characters!")
          : setpasswordErr("");
        break;
      default:
        break;
    }
  };

  const handleSignIn = async (e) => {
    e.preventDefault();
    if (username && password && !nameErr && !passwordErr) {
      dispatch(signIn({ username, password }));
    }
  };
  const authDetail = useSelector((state) => state.auth);
  if (authDetail.isAuthenticated) return <Redirect to={"/"} />;
  else
    return (
      <Grid container component="main" className={classes.root}>
        <CssBaseline />
        <Grid item xs={false} sm={4} md={7} className={classes.image} />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <div className={classes.paper}>
            <Avatar className={classes.avatar}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              تسجيل الدخول{" "}
            </Typography>
            {state.signInErr && (
              <Typography variant="h6" className={classes.titleErr}>
                خطأ في اسم المستخدم أو كلمة السر! حاول مجددا
              </Typography>
            )}
            <form className={classes.form} noValidate onSubmit={handleSignIn}>
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                id="username"
                label="اسم المستخدم"
                name="username"
                autoComplete="username"
                autoFocus
                onChange={(e) => handleChange(e, "username")}
              />
              {nameErr && (
                <small className={classes.errorText}>{nameErr}</small>
              )}
              <br></br>
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                name="password"
                label="كلمه السر"
                type="password"
                id="password"
                autoComplete="current-password"
                onChange={(e) => handleChange(e, "password")}
              />
              {passwordErr && (
                <small className={classes.errorText}>{passwordErr}</small>
              )}
              <br></br>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                className={classes.submit}
              >
                تسجيل{" "}
              </Button>
              {state.loading && <CircularProgress />}
              <Grid container>
                <Grid item xs>
                  <Link href="#" variant="body2">
                    هل نسيت كلمة المرور؟{" "}
                  </Link>
                </Grid>
              </Grid>
            </form>
          </div>
          <Box mt={8}>
            <Copyright />
          </Box>
        </Grid>
      </Grid>
    );
}
