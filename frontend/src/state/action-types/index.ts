export enum ActionType {
  // Testing
  DEPOSIT = 'deposit',
  WITHDRAW = 'withdraw',
  BANKRUPT = 'bankrupt',
  // Authentication
  REGISTER_SUCCESS = 'register success',
  REGISTER_FAIL = 'register_fail',
  LOGIN_SUCCESS = 'login_success',
  LOGIN_FAIL = 'login_fail',
  LOGOUT = 'logout',
  SET_AUTH_MESSAGE = 'set_auth_message',
  CLEAR_AUTH_MESSAGE = 'clear_auth_message',
  // User data
  GET_USER_DATA = 'get_user_data',
  CLEAR_USER_DATA = 'clear_user_data',
  EDIT_USER_DATA = 'edit_user_data',
  SAVE_USER_DATA_EDIT = 'save_user_date_edit',
  // Caff files
  GET_ALL_CAFF_FILES = 'get_all_caff_files',
  GET_CAFF_FILE = 'get_caff_file',
  DOWNLOAD_CAFF_FILE = 'download_caff_file',
}
