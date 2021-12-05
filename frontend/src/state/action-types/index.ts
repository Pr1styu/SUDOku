export enum ActionType {
  // Testing
  DEPOSIT = 'deposit',
  WITHDRAW = 'withdraw',
  BANKRUPT = 'bankrupt',
  // Authentication
  REGISTER_SUCCESS = 'register success',
  REGISTER_FAIL = 'register_fail',
  LOGIN_SUCCESS = 'login_success',
  LOGIN_SUCCESS_OAUTH = 'login_success_oauth',
  LOGIN_FAIL = 'login_fail',
  LOGOUT = 'logout',
  SET_INFO_MESSAGE = 'set_info_message',
  CLEAR_INFO_MESSAGE = 'clear_info_message',
  // User data
  GET_USER_DATA = 'get_user_data',
  CLEAR_USER_DATA = 'clear_user_data',
  EDIT_USER_DATA = 'edit_user_data',
  SAVE_USER_DATA_EDIT = 'save_user_date_edit',
  // Caff files
  GET_ALL_CAFF_FILES = 'get_all_caff_files',
  CLEAR_CAFF_FILES = 'clear_caff_files',
  GET_CAFF_FILE = 'get_caff_file',
  DOWNLOAD_CAFF_FILE = 'download_caff_file',
  ADD_COMMENT = 'add_comment',
  UPLOAD_CAFF_FILE = 'upload_caff_file',
  RESET_UPLOAD_DONE = 'reset_upload_done',
  DELETE_CAFF_FILE = 'delete_caff_file',
}
