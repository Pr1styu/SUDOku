import IEditUserData from '../interfaces/userDataEdit';
import authHeader from './AuthHeader';
import axios, { AxiosResponse } from 'axios';
import config from '../config/config';

const urlConfig = { headers: { 'Content-Type': 'application/json', ...authHeader() } };

const getUserData = (): Promise<AxiosResponse<any>> => {
  return axios.get(config.urls.user.getUserData, urlConfig);
};

const updateUserData = (userData: IEditUserData): Promise<AxiosResponse<any>> => {
  return axios.put(config.urls.user.updateUserData, userData, urlConfig);
};

export default {
  getUserData,
  updateUserData,
};
