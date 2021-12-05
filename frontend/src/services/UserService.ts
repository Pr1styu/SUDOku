import { AuthType } from './AuthType';
import { AxiosResponse } from 'axios';
import IUserData from '../interfaces/userData';
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

const getUserData = (authType: AuthType): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return httpClient.get(config.urls.user.getUserData, basicAuthUrlConfig());
  } else {
    return httpClient.get(config.urls.user.getUserData, tokenAuthUrlConfig());
  }
};

const updateUserData = (authType: AuthType, userData: IUserData): Promise<AxiosResponse<any>> => {
  if (authType === 'BASIC') {
    return httpClient.put(config.urls.user.updateUserData, userData, basicAuthUrlConfig());
  } else {
    return httpClient.put(config.urls.user.updateUserData, userData, tokenAuthUrlConfig());
  }
};

export default {
  getUserData,
  updateUserData,
};
