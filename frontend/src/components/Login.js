import React, { useContext, useState } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import { UserContext } from "../context/UserContext";
import { LOGIN } from "../context/constants/ActionConstants";
import * as Yup from "yup";
import { Typography } from "@mui/material";

const validationSchema = Yup.object({
  email: Yup.string().email("Invalid email format").required(),
  password: Yup.string().required(),
});

const initialValues = {
  email: "",
  password: "",
};

const Login = () => {

  const [error, setError] = useState("");
  const navigate = useNavigate();
  const {dispatch} = useContext(UserContext);

  return (
    <div>
      <h1 className="heading">Login</h1>

      <Formik
        initialValues={initialValues}
        validationSchema={validationSchema}
        onSubmit={async (values, { setSubmitting }) => {
          const { email, password } = values;
          const formData = new FormData();
          formData.append("username", email);
          formData.append("password", password);
          try {
            const { status } = await axios.post("/api/login", formData, {
              headers: { content: "multipart/form-data" },
            });
            if (status === 200) {
              dispatch({ type: LOGIN });
              navigate("/");
            }
          } catch (err) {
            console.log(err);
            setError("Invalid Credentials");
            setTimeout(() => {
              setError("");
            }, 6000);
          }

          setSubmitting(false);
        }}
      >
        {({ isSubmitting }) => (
          <Form className="form">
            <Field
              as={TextField}
              type="email"
              name="email"
              label="Email"
              variant="standard"
              sx={{ label: { color: "#fff" }, width: 300 }}
            />
            <ErrorMessage name="email" component="div" className="err-msg" />
            <Field
              as={TextField}
              type="password"
              name="password"
              label="Password"
              variant="standard"
              sx={{ label: { color: "#fff" }, width: 300 }}
            />
            <ErrorMessage name="password" component="div" className="err-msg" />

            {error && <p className="err-msg">{error}</p>}

            <Button
              role="loginButton"
              type="submit"
              disabled={isSubmitting}
              variant="contained"
              sx={{ marginTop: "1rem", marginBottom: "1rem" }}
            >
              Submit
            </Button>
          </Form>
        )}
      </Formik>
      <Typography align="center" sx={{ color: "#fff" }}>
        Don't have an account?{" "}
        <Link to="/register" className="link">
          Register here
        </Link>
      </Typography>
    </div>
  );
};

export default Login;
