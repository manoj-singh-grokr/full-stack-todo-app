import React, { useReducer, createContext } from "react";
import { userReducer } from "./reducers/userReducer";

export const UserContext = createContext();

export const initialState = {
  firstName: '',
  isLoggedIn: false,
  todos: [],
};

export const UserContextProvider = (props) => {
  const [state, dispatch] = useReducer(userReducer, initialState);
  return (
    <UserContext.Provider value={{state, dispatch}}>
      {props.children}
    </UserContext.Provider>
  );
};
