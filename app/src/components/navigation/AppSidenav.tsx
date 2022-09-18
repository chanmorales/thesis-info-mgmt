import React from 'react';
import {Nav, Sidebar, Sidenav} from 'rsuite';
import AdminIcon from '@rsuite/icons/Admin';
import PageIcon from '@rsuite/icons/Page';
import ToolsIcon from '@rsuite/icons/Tools';
import ModelIcon from '@rsuite/icons/Model';
import CharacterAuthorizeIcon from '@rsuite/icons/CharacterAuthorize';

interface AppSidenavProps {
  expanded: boolean,
  setExpanded: React.Dispatch<React.SetStateAction<boolean>>
}

export default function AppSidenav(props: AppSidenavProps) {

  const [activeNav, setActiveNav] = React.useState('nav-menu-theses')

  return(
      <Sidebar
          style={{ display: 'flex', flexDirection: 'column' }}
          width={ props.expanded ? 260 : 56 }
          collapsible>
        <Sidenav expanded={props.expanded} defaultOpenKeys={['nav-menu-mgmt']}>
          <Sidenav.Body>
            <Nav activeKey={activeNav} onSelect={setActiveNav}>
            <Nav.Item eventKey='nav-menu-theses' icon={<PageIcon/>}>
              Theses
            </Nav.Item>
            <Nav.Menu placement='rightStart'
                      eventKey='nav-menu-mgmt'
                      title='Management'
                      icon={<ToolsIcon/>}>
              <Nav.Item eventKey='nav-menu-mgmt-authors' icon={<AdminIcon/>}>
                Authors
              </Nav.Item>
              <Nav.Item eventKey='nav-menu-mgmt-degrees' icon={<ModelIcon/>}>
                Degrees
              </Nav.Item>
              <Nav.Item eventKey='nav-menu-mgmt-roles' icon={<CharacterAuthorizeIcon/>}>
                Roles
              </Nav.Item>
            </Nav.Menu>
          </Nav>
          </Sidenav.Body>
          <Sidenav.Toggle onToggle={expanded => props.setExpanded(expanded)}/>
        </Sidenav>
      </Sidebar>
  );
}