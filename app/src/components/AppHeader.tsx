import { IconButton, Stack } from 'rsuite';
import { Icon } from '@rsuite/icons';
import MenuIcon from '@rsuite/icons/Menu';
import DocPassIcon from '@rsuite/icons/DocPass';
import React from 'react';

interface AppHeaderProps {
  expanded: boolean,
  setExpanded: React.Dispatch<React.SetStateAction<boolean>>
}

export default function AppHeader(props: AppHeaderProps) {
  return(
      <div id='app-header' style={{ height: '60px' }}>
        <Stack style={{ height: '60px' }}>
          <IconButton
              appearance='subtle'
              icon={<MenuIcon/>}
              onClick={() => props.setExpanded(!props.expanded)}
              style={{ width: '60px' }}/>
          <Icon as={DocPassIcon} style={{ marginRight: '10px' }} />
          <h5>Thesis Information Management System</h5>
        </Stack>
      </div>
  );
}