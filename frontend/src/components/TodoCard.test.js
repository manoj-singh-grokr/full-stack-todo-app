import { render, screen } from '@testing-library/react'
import TodoCard from './TodoCard';

function renderComponent(props) {
    return render(
          <TodoCard id={props.id} content={props.content} completed={props.completed} dispatch={props.dispatch} />
    );
  }

  test("Todo is displayed", () => {
    const props = {id: 12324, content: "whatever you say", completed: false, dispatch: jest.fn() };
    renderComponent(props);
    expect(screen.getByText("whatever you say")).toBeInTheDocument();
  });