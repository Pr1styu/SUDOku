//import authHeader from './AuthHeader';
import { AuthType } from './AuthType';
import IComment from '../interfaces/comment';
import axios, { AxiosResponse } from 'axios';
import basicAuthHeader from './BasicAuthHeader';
import config from '../config/config';

//const urlConfig = { headers: { 'Content-Type': 'application/json', ...authHeader() } };

const getAllCaffFiles = (authType: AuthType): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.get(config.urls.caff.getAllCaffFiles, { auth: basicAuthHeader() });

    //TODO: JWT AUTH
  } else {
    return axios.get(config.urls.caff.getAllCaffFiles, { auth: basicAuthHeader() });
  }
};

const getCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.get(config.urls.caff.getAllCaffFiles + '/' + id, { auth: basicAuthHeader() });

    //TODO: JWT AUTH
  } else {
    return axios.get(config.urls.caff.getAllCaffFiles + '/' + id, { auth: basicAuthHeader() });
  }
};

const downloadCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.get(config.urls.caff.downloadCaffFile + id, {
      auth: basicAuthHeader(),
      responseType: 'blob',
    });

    //TODO: JWT AUTH
  } else {
    return axios.get(config.urls.caff.downloadCaffFile + id, {
      auth: basicAuthHeader(),
      responseType: 'blob',
    });
  }
};

const addComment = (
  authType: AuthType,
  comment: IComment,
  id: number
): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.post(
      config.urls.caff.getAllCaffFiles + '/' + id + '/comment',
      { text: comment.text, username: comment.userName },
      {
        auth: basicAuthHeader(),
      }
    );

    //TODO: JWT AUTH
  } else {
    return axios.post(
      config.urls.caff.getAllCaffFiles + '/' + id + '/comment',
      { text: comment.text, username: comment.userName },
      {
        auth: basicAuthHeader(),
      }
    );
  }
};

const uploadCaffFile = (
  authType: AuthType,
  fileName: string,
  file: File
): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    const formData = new FormData();
    formData.append('fileName', fileName);
    formData.append('caffFile', file);

    return axios.post(config.urls.caff.uploadCaffFile, formData, {
      auth: basicAuthHeader(),
    });

    //TODO: JWT AUTH
  } else {
    const formData = new FormData();
    formData.append('fileName', fileName);
    formData.append('caffFile', file);

    return axios.post(config.urls.caff.uploadCaffFile, formData, {
      auth: basicAuthHeader(),
    });
  }
};

const deleteCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.delete(config.urls.caff.getAllCaffFiles + '/' + id, {
      auth: basicAuthHeader(),
    });

    //TODO: JWT AUTH
  } else {
    return axios.delete(config.urls.caff.getAllCaffFiles + '/' + id, {
      auth: basicAuthHeader(),
    });
  }
};

const oauth = () => {
  return axios
    .get(
      'oauth2/authorize?response_type=code&client_id=frontend&scope=openid&redirect_uri=http://127.0.0.1:4200/authorized',
      {
        withCredentials: true,
        auth: basicAuthHeader(),
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
        },
      }
    )
    .then(function (response) {
      //handle success
      console.log('1s');
      console.log(response);
    })
    .catch(function (response) {
      //handle error
      console.log('1r');
      console.log(response);
    });
};

export default {
  getAllCaffFiles,
  getCaffFile,
  downloadCaffFile,
  addComment,
  uploadCaffFile,
  deleteCaffFile,
  oauth,
};
