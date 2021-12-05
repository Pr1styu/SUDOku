import { AuthType } from './AuthType';
import { AxiosResponse } from 'axios';
import IComment from '../interfaces/comment';
import authHeader from './AuthHeader';
import basicAuthHeader from './BasicAuthHeader';
import config from '../config/config';
import useAxios from './useAxios';

const httpClient = useAxios();

const basicAuthUrlConfig = () => {
  return { headers: { 'Content-Type': 'application/json', ...basicAuthHeader() } };
};

const tokenAuthUrlConfig = () => {
  return { headers: { 'Content-Type': 'application/json', ...authHeader() } };
};

const getAllCaffFiles = (authType: AuthType): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return httpClient.get(config.urls.caff.getAllCaffFiles, basicAuthUrlConfig());
  } else {
    return httpClient.get(config.urls.caff.getAllCaffFiles, tokenAuthUrlConfig());
  }
};

const getCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return httpClient.get(config.urls.caff.getAllCaffFiles + '/' + id, basicAuthUrlConfig());
  } else {
    return httpClient.get(config.urls.caff.getAllCaffFiles + '/' + id, tokenAuthUrlConfig());
  }
};

const downloadCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return httpClient.get(config.urls.caff.downloadCaffFile(id), {
      headers: { ...basicAuthHeader() },
      responseType: 'blob',
    });
  } else {
    return httpClient.get(config.urls.caff.downloadCaffFile(id), {
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
    return httpClient.post(
      config.urls.caff.getAllCaffFiles + '/' + id + '/comment',
      { text: comment.text, username: comment.userName },
      basicAuthUrlConfig()
    );
  } else {
    return httpClient.post(
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

    return httpClient.post(config.urls.caff.uploadCaffFile, formData, basicAuthUrlConfig());
  } else {
    const formData = new FormData();
    formData.append('fileName', fileName);
    formData.append('caffFile', file);

    return httpClient.post(config.urls.caff.uploadCaffFile, formData, tokenAuthUrlConfig());
  }
};

const deleteCaffFile = (authType: AuthType, id: number): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return httpClient.delete(config.urls.caff.getAllCaffFiles + '/' + id, basicAuthUrlConfig());
  } else {
    return httpClient.delete(config.urls.caff.getAllCaffFiles + '/' + id, tokenAuthUrlConfig());
  }
};

export default {
  getAllCaffFiles,
  getCaffFile,
  downloadCaffFile,
  addComment,
  uploadCaffFile,
  deleteCaffFile,
};
