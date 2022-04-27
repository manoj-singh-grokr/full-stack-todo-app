import Navbar from "./Navbar";
import renderComponentWithContext from "../testUtils";
import {screen} from "@testing-library/react";

test("Navbar shows username and logout when logged in", () => {
    const contextValue = { state: {firstName: 'Test', isLoggedIn: true, todos: [] }, dispatch: jest.fn() };
    renderComponentWithContext(contextValue, <Navbar />);
    expect(screen.getByText("Test")).toBeInTheDocument();
    expect(screen.getByText(/Logout/i)).toBeInTheDocument();
    screen.debug();
});

test("Navbar shows login and signup when not logged in", () => {
    const contextValue = { state: {firstName: '', isLoggedIn: false, todos: [] }, dispatch: jest.fn() };
    renderComponentWithContext(contextValue, <Navbar />);
    expect(screen.getByText(/Login/i)).toBeInTheDocument();
    expect(screen.getByText(/SIGN UP/i)).toBeInTheDocument();
});