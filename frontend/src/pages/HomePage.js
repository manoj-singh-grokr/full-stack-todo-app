import React, { useContext, useEffect } from "react";
import TodoCard from "../components/TodoCard";
import Container from "@mui/material/Container";
import { UserContext } from "../context/UserContext";
import Input from "@mui/material/Input";
import {
  ADD_TODO,
  GET_TODOS,
  IS_LOGGED,
} from "../context/constants/ActionConstants";
import axios from "axios";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import ImageListContainer from "../components/ImageListContainer";
import { Link } from "react-router-dom";

const HomePage = () => {
  const { state, dispatch } = useContext(UserContext);

  useEffect(() => {
    const fetchTodos = async () => {
      const { data } = await axios.get("/api/todos", { withCredentials: true });
      dispatch({ type: GET_TODOS, payload: data });
    };

    const checkIfLoggedIn = async () => {
      const { status, data } = await axios.get("/api/checklogged", {
        withCredentials: true,
      });
      if (status === 200) {
        dispatch({ type: IS_LOGGED, payload: data });
        fetchTodos();
      }
    };
    checkIfLoggedIn();
  }, []);

  const handleAdd = async (e) => {
    e.preventDefault();
    const todo = { content: e.target.todo_content.value };
    const { data } = await axios.post("/api/todos/add", todo, {
      headers: { "Content-Type": "application/json" },
      withCredentials: true,
    });
    dispatch({ type: ADD_TODO, payload: data });
    e.target.reset();
  };

  return (
    <div>
      {state.isLoggedIn ? (
        <div>
          <h1 className="heading">WHAT DO YOU WANT TO DO?</h1>
          <Container maxWidth="sm">
            <form onSubmit={(e) => handleAdd(e)} className="card-content">
              <Input
                sx={{
                  color: "#fff",
                  marginBottom: "1rem",
                  width: "100%",
                  marginRight: "1rem",
                }}
                placeholder="Write a todo 😋"
                name="todo_content"
              />
              <Button
                type="submit"
                color="success"
                variant="contained"
                className="add-btn"
              >
                <AddIcon />
              </Button>
            </form>

            {state.todos &&
              state.todos.map((todo) => (
                <TodoCard
                  key={todo.id}
                  id={todo.id}
                  content={todo.content}
                  completed={todo.completed}
                  dispatch={dispatch}
                />
              ))}
          </Container>
          <footer className="main-footer">©Todo</footer>
        </div>
      ) : (
        <div>
          <h1 className="heading">
            WHAT TO DO? WHAT TO DO? HERE'S A FEW THINGS YOU COULD DO.
          </h1>
          <Container>
            <ImageListContainer />
            <h1 className="heading">
              create your own todo list by{" "}
              <Link role="loginPageLink" to="/login" className="link">
                Logging In
              </Link>
            </h1>
          </Container>
          <footer className="homepage-footer">©Todo</footer>
        </div>
      )}
    </div>
  );
};

export default HomePage;
