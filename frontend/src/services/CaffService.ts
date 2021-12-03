import { AuthType } from './AuthType';
import IComment from '../interfaces/comment';
import authHeader from './AuthHeader';
import axios, { AxiosResponse } from 'axios';
import basicAuthHeader from './BasicAuthHeader';
import config from '../config/config';

const basicAuthUrlConfig = () => {
  return { headers: { 'Content-Type': 'application/json', ...basicAuthHeader() } };
};

const tokenAuthUrlConfig = () => {
  return { headers: { 'Content-Type': 'application/json', ...authHeader() } };
};

const getAllCaffFiles = (authType: AuthType): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.get(config.urls.caff.getAllCaffFiles, basicAuthUrlConfig());
  } else {
    return axios.get(config.urls.caff.getAllCaffFiles, tokenAuthUrlConfig());
  }
};

const getCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.get(config.urls.caff.getAllCaffFiles + '/' + id, basicAuthUrlConfig());
  } else {
    return axios.get(config.urls.caff.getAllCaffFiles + '/' + id, tokenAuthUrlConfig());
  }
};

const downloadCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.get(config.urls.caff.downloadCaffFile + id, {
      headers: { ...basicAuthHeader() },
      responseType: 'blob',
    });
  } else {
    return axios.get(config.urls.caff.downloadCaffFile + id, {
      headers: { ...authHeader() },
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
      basicAuthUrlConfig()
    );
  } else {
    return axios.post(
      config.urls.caff.getAllCaffFiles + '/' + id + '/comment',
      { text: comment.text, username: comment.userName },
      tokenAuthUrlConfig()
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

    return axios.post(config.urls.caff.uploadCaffFile, formData, basicAuthUrlConfig());
  } else {
    const formData = new FormData();
    formData.append('fileName', fileName);
    formData.append('caffFile', file);

    return axios.post(config.urls.caff.uploadCaffFile, formData, tokenAuthUrlConfig());
  }
};

const deleteCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.delete(config.urls.caff.getAllCaffFiles + '/' + id, basicAuthUrlConfig());
  } else {
    return axios.delete(config.urls.caff.getAllCaffFiles + '/' + id, tokenAuthUrlConfig());
  }
};

const getOauthToken = (code: string): Promise<void> => {
  return axios
    .post(
      config.urls.oauth2.token(code),
      {},
      {
        auth: {
          username: config.urls.oauth2.params.clientId,
          password: config.urls.oauth2.params.clientPassword,
        },
      }
    )
    .then((response) => {
      localStorage.setItem('user_token', JSON.stringify(response.data));
    });
};

export default {
  getAllCaffFiles,
  getCaffFile,
  downloadCaffFile,
  addComment,
  uploadCaffFile,
  deleteCaffFile,
  getOauthToken,
};
