import { render } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom';
import { UserContext } from './context/UserContext';

export default function renderComponentWithContext({state, dispatch}, component) {
    return render(
      <BrowserRouter>
        <UserContext.Provider value={{state, dispatch}}>
          {component}
        </UserContext.Provider>
      </BrowserRouter>
    );
}