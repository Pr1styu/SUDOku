import { Box, Button, Grid, Typography } from '@mui/material';
import { State, actionCreators } from '../state';
import { bindActionCreators } from 'redux';
import { useDispatch, useSelector } from 'react-redux';
import AddComment from './AddComment';
import Comment from './Comment';
import ICaff from '../interfaces/caff';
import IComponent from '../interfaces/component';
import React, { useState } from 'react';

const Caff: React.FC<IComponent & ICaff> = (caff: ICaff) => {
  const [objectUrl, setObjectUrl] = useState('#');

  const caffStore = useSelector((state: State) => state.CAFF);
  const user = useSelector((state: State) => state.AUTH.user);

  const dispatch = useDispatch();
  const { deleteCaffFile } = bindActionCreators(actionCreators, dispatch);

  const download = (fileUrl: string, fileName: string) => {
    const a = document.createElement('a');
    a.href = fileUrl;
    a.setAttribute('download', fileName);
    a.click();
  };

  return (
    <Grid container>
      <Grid item xs={1} />
      <Grid
        item
        xs={10}
        sx={{
          mt: 2,
          mb: 2,
          border: '2px solid #e0e0e0',
          borderRadius: 2,
          p: 2,
          boxShadow: '3px 3px 3px 1px rgba(0,0,0,0.1)',
        }}
      >
        <Grid container>
          <Grid item xs={12} sx={{ textAlign: 'center' }}>
            <Typography
              variant="h3"
              gutterBottom
              component="div"
              sx={{ fontWeight: 'bold', mb: 2 }}
            >
              {caff.fileName}
            </Typography>
          </Grid>
          <Grid item xs={12} sx={{ textAlign: 'center' }}>
            <Box
              component="img"
              alt="Caff"
              src={'data:image/png;base64, ' + caff.preview}
              sx={{
                width: 700,
                boxShadow: '10px 10px 10px 1px rgba(0,0,0,0.2)',
                mb: 2,
              }}
            />
          </Grid>
          <Grid item xs={12} sx={{ textAlign: 'center' }}>
            <AddComment name="Add comment" id={caff.id} userName={user?.username ?? 'Anonymous'} />
          </Grid>
          <Grid item xs={12} sx={{ textAlign: 'center' }}>
            {caff.comments?.map((comment, index) => (
              <Comment
                key={index}
                name={'comment' + index}
                text={comment.text}
                userName={comment.userName ?? 'Anonymous'}
              />
            ))}
          </Grid>
        </Grid>
        <Grid item xs={12} sx={{ mt: 3, textAlign: 'right' }}>
          <Button
            variant="contained"
            sx={{ mr: '1em' }}
            color="error"
            onClick={() => {
              deleteCaffFile(caff.id);
            }}
          >
            Delete CAFF
          </Button>
          <Button
            variant="contained"
            sx={{ mr: '1em' }}
            disabled={!caffStore.downloadDone.includes(caff.id)}
            onClick={() => {
              setObjectUrl(window.URL.createObjectURL(caffStore.downloadFile));
              download(objectUrl, 'file.caff');
            }}
          >
            Download CAFF
          </Button>
          <Button
            variant="contained"
            sx={{ mr: '1em' }}
            color="success"
            onClick={() => {
              download('data:image/png;base64, ' + caff.preview, 'image.png');
            }}
          >
            Download Image
          </Button>
        </Grid>
        <Grid item xs={12} sx={{ mt: 3 }}>
          <Typography
            variant="body1"
            gutterBottom
            component="div"
            sx={{ fontWeight: 'bold', display: 'inline-block', color: '#6e6e6e', mr: 2 }}
          >
            Tags:
          </Typography>
          {caff.metaData?.map((metaData, index) => (
            <Typography
              key={index}
              variant="body2"
              gutterBottom
              component="div"
              sx={{ display: 'inline-block', color: '#6e6e6e', mr: 2 }}
            >
              {metaData}
            </Typography>
          ))}
        </Grid>
      </Grid>
      <Grid item xs={1} />
    </Grid>
  );
};

export default Caff;
