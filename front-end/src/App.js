import React, {Suspense, useEffect} from "react";
import ReactGA from 'react-ga';
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { routes } from "./routes";
import Loading from "./components/suspenseLoding";

function App() {

  useEffect(() => {
      ReactGA.initialize('G-8X3LJW08X7');
      ReactGA.pageview(window.location.pathname + window.location.search);
  }, [])

  return (
      <Suspense fallback={<Loading/>}>
        <BrowserRouter>
          <Switch>
            {routes.map(route => {
              return (
                  <Route
                      key={route.path}
                      path={route.path}
                      exact
                      render={(props) => {
                        return <route.component {...props} />
                      }}/>
              )
            })}
          </Switch>
        </BrowserRouter>
      </Suspense>
  );

}

export default App;
