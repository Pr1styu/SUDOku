//import authHeader from './AuthHeader';
import { AuthType } from './AuthType';
import IComment from '../interfaces/comment';
import axios, { AxiosResponse } from 'axios';
import config from '../config/config';

//const urlConfig = { headers: { 'Content-Type': 'application/json', ...authHeader() } };

const getAllCaffFiles = (authType: AuthType): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : null;

    const auth = {
      username: user.username,
      password: user.password,
    };

    return axios.get(config.urls.caff.getAllCaffFiles, { auth });

    //TODO: JWT AUTH
  } else {
    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : null;

    const auth = {
      username: user.username,
      password: user.password,
    };

    return axios.get(config.urls.caff.getAllCaffFiles, { auth });
  }
};

const getCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : null;

    const auth = {
      username: user.username,
      password: user.password,
    };

    return axios.get(config.urls.caff.getAllCaffFiles + '/' + id, { auth });

    //TODO: JWT AUTH
  } else {
    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : null;

    const auth = {
      username: user.username,
      password: user.password,
    };

    return axios.get(config.urls.caff.getAllCaffFiles + '/' + id, { auth });
  }
};

const downloadCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : null;

    const auth = {
      username: user.username,
      password: user.password,
    };

    return axios.get(config.urls.caff.downloadCaffFile + id, { auth, responseType: 'blob' });

    //TODO: JWT AUTH
  } else {
    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : null;

    const auth = {
      username: user.username,
      password: user.password,
    };

    return axios.get(config.urls.caff.downloadCaffFile + id, { auth, responseType: 'blob' });
  }
};

const addComment = (
  authType: AuthType,
  comment: IComment,
  id: number
): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : null;

    const auth = {
      username: user.username,
      password: user.password,
    };

    return axios.post(
      config.urls.caff.getAllCaffFiles + '/' + id + '/comment',
      { text: comment.text, username: comment.userName },
      {
        auth,
      }
    );

    //TODO: JWT AUTH
  } else {
    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : null;

    const auth = {
      username: user.username,
      password: user.password,
    };

    return axios.post(
      config.urls.caff.getAllCaffFiles + '/' + id + '/comment',
      { text: comment.text, username: comment.userName },
      {
        auth,
      }
    );
  }
};

export default {
  getAllCaffFiles,
  getCaffFile,
  downloadCaffFile,
  addComment,
};
