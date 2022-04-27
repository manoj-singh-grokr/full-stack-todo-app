import React, { useContext } from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { NavLink } from "react-router-dom";
import { UserContext } from "../context/UserContext";
import { LOGOUT } from "../context/constants/ActionConstants";
import axios from "axios";

const Navbar = () => {
  const {state, dispatch} = useContext(UserContext);

  const handleLogout = async () => {
    await axios.post("/api/logout", { withCredentials: true });
    localStorage.setItem("isLoggedIn", false);
    dispatch({ type: LOGOUT });
  };
  const { isLoggedIn } = state;
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar
        position="static"
        style={{ background: "transparent", boxShadow: "none" }}
      >
        <Toolbar>
          <Typography
            variant="h6"
            component="div"
            sx={{
              flexGrow: 1,
              fontFamily: "Shadows Into Light",
              fontWeight: "400",
            }}
          >
            <NavLink to="/" className="logo-link">
              TODO
            </NavLink>
          </Typography>

          {!isLoggedIn ? (
            <>
              <Button color="inherit">
                <NavLink to="/login" className="nav-link">
                  Login
                </NavLink>
              </Button>
              <Button color="inherit">
                <NavLink to="/register" className="nav-link">
                  SIGN UP
                </NavLink>
              </Button>
            </>
          ) : (
            <>
              <Button color="inherit" className="nav-link">
                {state.firstName}
              </Button>
              <Button
                color="inherit"
                onClick={() => handleLogout()}
                className="nav-link"
              >
                LOGOUT
              </Button>
            </>
          )}
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default Navbar;
