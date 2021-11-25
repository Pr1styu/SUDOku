// Backend base URLs
const BACKEND_PROTOCOL = 'http';
const BACKEND_HOST = 'localhost';
const BACKEND_PORT = '8080';
const BACKEND_BASE_PATH = 'caff';
const USE_PROXY = true;
const BACKEND_BASE_URL = USE_PROXY
  ? BACKEND_BASE_PATH
  : BACKEND_PROTOCOL + '://' + BACKEND_HOST + ':' + BACKEND_PORT + '/' + BACKEND_BASE_PATH;
// Backend service URLs

// Authentication
const SIGN_UP_URL = BACKEND_BASE_URL + 'register';
const SIGN_IN_URL = BACKEND_BASE_URL + 'authenticate';

// User data
const GET_USER_DATA = BACKEND_BASE_URL + 'userdata';
const UPDATE_USER_DATA = BACKEND_BASE_URL + 'updateUserdata';

// Caff files
const GET_ALL_CAFF_FILES = BACKEND_BASE_URL;
const DOWNLOAD_CAFF_FILE = BACKEND_BASE_URL + '/download/';

const config = {
  defaults: {
    namespace: 'App',
  },
  urls: {
    auth: {
      signIn: SIGN_IN_URL,
      signUp: SIGN_UP_URL,
    },
    user: {
      getUserData: GET_USER_DATA,
      updateUserData: UPDATE_USER_DATA,
    },
    caff: {
      getAllCaffFiles: GET_ALL_CAFF_FILES,
      downloadCaffFile: DOWNLOAD_CAFF_FILE,
    },
  },
};

export default config;
