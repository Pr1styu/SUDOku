import { AuthType } from './AuthType';
import IUserData from '../interfaces/userData';
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

const getUserData = (authType: AuthType): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.get(config.urls.user.getUserData, basicAuthUrlConfig());
  } else {
    return axios.get(config.urls.user.getUserData, tokenAuthUrlConfig());
  }
};

const updateUserData = (authType: AuthType, userData: IUserData): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return axios.put(config.urls.user.updateUserData, userData, basicAuthUrlConfig());
  } else {
    return axios.put(config.urls.user.updateUserData, userData, tokenAuthUrlConfig());
  }
};

export default {
  getUserData,
  updateUserData,
};
