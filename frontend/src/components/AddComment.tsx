import { Button, Grid, TextField, Typography } from '@mui/material';
import { actionCreators } from '../state';
import { bindActionCreators } from 'redux';
import { useDispatch } from 'react-redux';
import IComponent from '../interfaces/component';
import React, { useState } from 'react';

const AddComment: React.FC<IComponent & { id: number; userName: string }> = (props) => {
  const [editing, setEditing] = useState(false);
  const [text, setText] = useState('');

  const dispatch = useDispatch();
  const { addComment } = bindActionCreators(actionCreators, dispatch);

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
          <Grid item xs={6}>
            <Typography
              variant="h6"
              gutterBottom
              component="div"
              sx={{ fontWeight: 'bold', mb: 0, textAlign: 'left', color: '#6e6e6e', mr: 2 }}
            >
              {editing ? props.userName : 'Add new comment...'}
            </Typography>
          </Grid>
          <Grid item xs={6} sx={{ textAlign: 'right' }}>
            {!editing && (
              <Button
                variant="contained"
                sx={{ mr: '1em' }}
                onClick={() => {
                  setEditing(true);
                  setText('');
                }}
              >
                New comment
              </Button>
            )}
            {editing && (
              <div>
                <Button
                  variant="contained"
                  sx={{ mr: '1em' }}
                  color="error"
                  onClick={() => {
                    setEditing(false);
                  }}
                >
                  Cancel
                </Button>
                <Button
                  variant="contained"
                  sx={{ mr: '1em' }}
                  color="success"
                  onClick={() => {
                    addComment({ text, userName: props.userName }, props.id);
                    setEditing(false);
                  }}
                >
                  Post comment
                </Button>
              </div>
            )}
          </Grid>

          {editing && (
            <div>
              <Grid item xs={12} sx={{ border: '1px solid #e0e0e0', mt: 2, p: 2 }}>
                <TextField
                  name="name"
                  value={text}
                  multiline
                  autoFocus
                  placeholder="Write your comment..."
                  variant="standard"
                  onChange={(e) => {
                    e.target.value.length > 255
                      ? setText(e.target.value.substring(0, 255))
                      : setText(e.target.value);
                  }}
                  sx={{ width: 650 }}
                />
              </Grid>
              <Grid item xs={12} sx={{ mt: 1 }}>
                <Typography
                  variant="body2"
                  gutterBottom
                  component="div"
                  sx={{ fontWeight: 'bold', mb: 0, textAlign: 'left', color: '#6e6e6e' }}
                >
                  {text.length + '/' + '255'}
                </Typography>
              </Grid>
            </div>
          )}
        </Grid>
      </Grid>
      <Grid item xs={1} />
    </Grid>
  );
};

export default AddComment;
