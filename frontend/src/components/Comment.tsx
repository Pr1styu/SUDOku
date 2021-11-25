import { Box, Grid, Typography } from '@mui/material';
import IComment from '../interfaces/comment';
import IComponent from '../interfaces/component';
import React from 'react';

const Comment: React.FC<IComponent & IComment> = (comment: IComment) => {
  return (
    <Grid container>
      <Grid item xs={1} />
      <Grid
        item
        xs={10}
        sx={{
          mt: 1,
          mb: 1,
          border: '2px solid #e0e0e0',
          borderRadius: 2,
          p: 2,
          boxShadow: '3px 3px 3px 1px rgba(0,0,0,0.1)',
        }}
      >
        <Grid container>
          <Grid item xs={3}>
            <Typography
              variant="h6"
              gutterBottom
              component="div"
              sx={{ fontWeight: 'bold', mb: 0 }}
            >
              {comment.userName}
            </Typography>
            <Box
              component="img"
              alt="Caff"
              src="/anonymous_user.svg"
              sx={{
                width: 100,
              }}
            />
          </Grid>
          <Grid item xs={9} sx={{ border: '1px solid #e0e0e0' }}>
            <Typography
              variant="body1"
              gutterBottom
              component="div"
              sx={{ textAlign: 'left', m: 2 }}
            >
              {comment.text}
            </Typography>
          </Grid>
        </Grid>
      </Grid>
      <Grid item xs={1} />
    </Grid>
  );
};

export default Comment;
