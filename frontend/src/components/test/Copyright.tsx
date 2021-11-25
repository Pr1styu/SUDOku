import * as React from 'react';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';

const Copyright: React.FC<any> = (props: any) => {
  return (
    <Typography variant="body2" color="text.secondary" align="center" {...props}>
      {'Copyright © '}
      <Link color="inherit" href="https://localhost:4200/">
        CAFF Viewer
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
};

export default Copyright;
