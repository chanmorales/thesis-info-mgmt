import React from 'react';
import { Container } from 'rsuite';
import AppHeader from './AppHeader';
import AppContent from './AppContent';
import AppFooter from './AppFooter';
import AppSidenav from './navigation/AppSidenav';

export default function AppMain() {

  const [expanded, setExpanded] = React.useState(true);

  return(
      <div id='app-main'>
        <Container>
          <AppHeader expanded={expanded} setExpanded={setExpanded}/>
          <Container style={{ height: `calc(100vh - 130px)` }}>
            <AppSidenav expanded={expanded} setExpanded={setExpanded}/>
            <AppContent/>
          </Container>
          <AppFooter/>
        </Container>
      </div>
  );
}