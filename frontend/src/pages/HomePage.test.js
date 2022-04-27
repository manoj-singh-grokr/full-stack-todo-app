import { screen } from "@testing-library/react";
import renderComponentWithContext from "../testUtils";
import HomePage from "./HomePage";

test("When the user is logged in", () => {
  const contextValue = {
    state: { firstName: "Test", isLoggedIn: true, todos: [] },
    dispatch: jest.fn(),
  };
  renderComponentWithContext(contextValue, <HomePage />);

  expect(screen.getByText("WHAT DO YOU WANT TO DO?")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Write a todo 😋")).toBeInTheDocument();
});

test("When the user is not logged in", () => {
  const contextValue = {
    state: { firstName: "", isLoggedIn: false, todos: [] },
    dispatch: jest.fn(),
  };
  renderComponentWithContext(contextValue, <HomePage />);
  expect(screen.getByText("WHAT TO DO? WHAT TO DO? HERE'S A FEW THINGS YOU COULD DO.")).toBeInTheDocument();
  expect(screen.getByRole("loginPageLink")).toBeInTheDocument();
});
