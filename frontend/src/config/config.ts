import { AuthType } from '../services/AuthType';

// Frontend base URLs
const FRONTEND_PROTOCOL = 'http';
const FRONTEND_HOST = 'localhost';
const FRONTEND_HOST_IP = '127.0.0.1';
const FRONTEND_PORT = 4200;
const FRONTEND_BASE_URL = FRONTEND_PROTOCOL + '://' + FRONTEND_HOST + ':' + FRONTEND_PORT + '/';
const FRONTEND_BASE_URL_IP =
  FRONTEND_PROTOCOL + '://' + FRONTEND_HOST_IP + ':' + FRONTEND_PORT + '/';

// Proxy
const USE_PROXY = true;
const INTERCEPTOR_DEBUG = true;

// Backend base URLs
const BACKEND_SERVICE_PROTOCOL = 'http';
const BACKEND_SERVICE_HOST = 'localhost';
const BACKEND_SERVICE_PORT = '8080';
const BACKEND_SERVICE_BASE_PATH = 'caff';
const BACKEND_SERVICE_BASE_URL = USE_PROXY
  ? BACKEND_SERVICE_BASE_PATH
  : BACKEND_SERVICE_PROTOCOL +
    '://' +
    BACKEND_SERVICE_HOST +
    ':' +
    BACKEND_SERVICE_PORT +
    '/' +
    BACKEND_SERVICE_BASE_PATH;

const BACKEND_AUTH_SERVER_PROTOCOL = 'http';
const BACKEND_AUTH_SERVER_HOST = 'localhost';
const BACKEND_AUTH_SERVER_PORT = '9000';
const BACKEND_AUTH_SERVER_BASE_PATH = '/oauth2/';
const BACKEND_AUTH_SERVER_BASE_PATH_USER = '/user';
const BACKEND_AUTH_SERVER_BASE_URL_NO_PROXY =
  BACKEND_AUTH_SERVER_PROTOCOL +
  '://' +
  BACKEND_AUTH_SERVER_HOST +
  ':' +
  BACKEND_AUTH_SERVER_PORT +
  BACKEND_AUTH_SERVER_BASE_PATH;
const BACKEND_AUTH_SERVER_BASE_URL = USE_PROXY
  ? BACKEND_AUTH_SERVER_BASE_PATH
  : BACKEND_AUTH_SERVER_PROTOCOL +
    '://' +
    BACKEND_AUTH_SERVER_HOST +
    ':' +
    BACKEND_AUTH_SERVER_PORT +
    BACKEND_AUTH_SERVER_BASE_PATH;

// Authentication
const SIGN_UP_URL = BACKEND_AUTH_SERVER_BASE_PATH_USER + '/register';

const AUTH_TYPE: AuthType = 'OAUTH';

// OAuth2
const RESPONSE_TYPE = 'code';
const GRANT_TYPE = 'authorization_code';
const GRANT_TYPE_REFRESH_TOKEN = 'refresh_token';
const CLIENT_ID = 'frontend';
const SCOPE = 'openid';
const REDIRECT_URI = FRONTEND_BASE_URL_IP + 'authorized';

const AUTHORIZE_BASE_URL = BACKEND_AUTH_SERVER_BASE_URL_NO_PROXY + 'authorize';
const AUTHORIZE_URL =
  AUTHORIZE_BASE_URL +
  '?response_type=' +
  RESPONSE_TYPE +
  '&client_id=' +
  CLIENT_ID +
  '&scope=' +
  SCOPE +
  '&redirect_uri=' +
  REDIRECT_URI;
const TOKEN_BASE_URL = BACKEND_AUTH_SERVER_BASE_URL + 'token';
const TOKEN_URL = (CODE: string): string =>
  TOKEN_BASE_URL +
  '?grant_type=' +
  GRANT_TYPE +
  '&code=' +
  CODE +
  '&scope=' +
  SCOPE +
  '&redirect_uri=' +
  REDIRECT_URI;
const REFRESH_TOKEN_URL = (REFRESH_TOKEN: string): string =>
  TOKEN_BASE_URL + '?grant_type=' + GRANT_TYPE_REFRESH_TOKEN + '&refresh_token=' + REFRESH_TOKEN;

const CLIENT_NAME = 'frontend';
const CLIENT_PASSWORD = 'secret';

// User data
const GET_USER_DATA = BACKEND_AUTH_SERVER_BASE_PATH_USER;
const UPDATE_USER_DATA = BACKEND_AUTH_SERVER_BASE_PATH_USER + '/update';

// Caff files
const GET_ALL_CAFF_FILES = BACKEND_SERVICE_BASE_URL;
const DOWNLOAD_CAFF_FILE = (id: number): string =>
  BACKEND_SERVICE_BASE_URL + '/' + id + '/download/';
const UPLOAD_CAFF_FILE = BACKEND_SERVICE_BASE_URL + '/upload';

const config = {
  defaults: {
    namespace: 'App',
  },
  authType: AUTH_TYPE,
  interceptorDebug: INTERCEPTOR_DEBUG,
  urls: {
    fontendBaseUrl: FRONTEND_BASE_URL,
    signUp: SIGN_UP_URL,
    oauth2: {
      authorizeBase: AUTHORIZE_BASE_URL,
      authorize: AUTHORIZE_URL,
      token: TOKEN_URL,
      refresh_token: REFRESH_TOKEN_URL,
      params: {
        responseType: RESPONSE_TYPE,
        clientId: CLIENT_ID,
        scope: SCOPE,
        redirectUri: REDIRECT_URI,
        clientName: CLIENT_NAME,
        clientPassword: CLIENT_PASSWORD,
      },
    },
    user: {
      getUserData: GET_USER_DATA,
      updateUserData: UPDATE_USER_DATA,
    },
    caff: {
      getAllCaffFiles: GET_ALL_CAFF_FILES,
      downloadCaffFile: DOWNLOAD_CAFF_FILE,
      uploadCaffFile: UPLOAD_CAFF_FILE,
    },
  },
};

export default config;
