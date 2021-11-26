import { Alert, Box, Button, FormControl, TextField, Typography } from '@mui/material';
import { RouteComponentProps } from 'react-router';
import { State, actionCreators } from '../state';
import { bindActionCreators } from 'redux';
import { useDispatch, useSelector } from 'react-redux';
import CircularProgress from '@mui/material/CircularProgress';
import Container from '@mui/material/Container';
import Copyright from '../components/test/Copyright';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import Paper from '@mui/material/Paper';
import React, { useEffect, useState } from 'react';

const AddCaff: React.FC<IComponent & RouteComponentProps<any>> = () => {
  const [name, setName] = useState('');
  const [file, setFile] = useState<File | ''>('');
  const [progress, setProgress] = useState(0);

  const dispatch = useDispatch();
  const { uploadCaffFile, resetUploadDone } = bindActionCreators(actionCreators, dispatch);

  useEffect(() => {
    resetUploadDone();
  }, []);

  const caffStore = useSelector((state: State) => state.CAFF);

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ ml: 2, mt: 2, pb: '5em' }}>
              <Typography variant="h3" gutterBottom component="div">
                Add new Caff file
              </Typography>
              <Grid container spacing={3} sx={{ mt: 2, mb: 2 }}>
                <Grid item xs={3}>
                  <Typography variant="h6" component="div">
                    Name
                  </Typography>
                  <FormControl fullWidth>
                    <TextField
                      name="name"
                      value={name}
                      variant="standard"
                      onChange={(e) => {
                        setName(e.target.value);
                      }}
                      sx={{ mt: '1.4em' }}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={3}>
                  <Typography variant="h6" component="div">
                    File
                  </Typography>
                  <FormControl fullWidth>
                    <TextField
                      name="file"
                      value={file && file.name}
                      variant="standard"
                      sx={{ mt: '1.4em' }}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={6} sx={{ mt: 4, display: 'inline' }}>
                  <Button
                    variant="contained"
                    component="label"
                    sx={{ width: 170, height: 52, textAlign: 'left' }}
                  >
                    Upload File
                    {progress ? (
                      <CircularProgress
                        variant="determinate"
                        value={progress}
                        sx={{ ml: 1, color: 'white' }}
                      />
                    ) : (
                      ''
                    )}
                    <input
                      type="file"
                      accept=".caff"
                      hidden
                      onChange={(e) => {
                        const file = e.target.files ? e.target.files[0] : '';
                        setFile(file);
                        const reader = new FileReader();
                        file && reader.readAsDataURL(file);
                        reader.onprogress = (e) => {
                          const percent = Math.round((e.loaded / e.total) * 100);
                          setProgress(percent);
                        };
                      }}
                    />
                  </Button>
                  <Button
                    variant="contained"
                    component="label"
                    color="success"
                    disabled={!name || !file}
                    sx={{ height: 52, textAlign: 'left', ml: 4 }}
                    onClick={() => file && uploadCaffFile(name, file)}
                  >
                    Create new file
                  </Button>
                </Grid>
              </Grid>
              {caffStore.uploadDone && (
                <Grid item xs={12} sx={{ mt: 5 }}>
                  <Alert severity="success">
                    <Typography
                      variant="body1"
                      component="div"
                      sx={{ color: 'green', fontWeight: 'bold' }}
                    >
                      New file created successfully!
                    </Typography>
                  </Alert>
                </Grid>
              )}
            </Box>
          </Paper>
        </Grid>
      </Grid>
      <Copyright sx={{ pt: 4 }} />
    </Container>
  );
};

export default AddCaff;
