import { ActionType } from '../action-types';
import { CaffAction } from '../actions';
import ICaff from '../../interfaces/caff';

const initialState = {
  preloadDone: false,
  uploadDone: false,
  caff_files: [],
};

type caffState = {
  preloadDone: boolean;
  uploadDone: boolean;
  caff_files: ICaff[];
};

const reducer = (state: caffState = initialState, action: CaffAction): caffState => {
  switch (action.type) {
    case ActionType.GET_ALL_CAFF_FILES:
      return {
        ...state,
        preloadDone: true,
      };
    case ActionType.GET_CAFF_FILE:
      return {
        ...state,
        caff_files: [...state.caff_files, action.payload],
      };
    case ActionType.DOWNLOAD_CAFF_FILE:
      return {
        ...state,
      };
    case ActionType.ADD_COMMENT:
      return {
        ...state,
        caff_files: state.caff_files.map((caff) =>
          caff.id === action.payload.id
            ? { ...caff, comments: [...caff.comments, action.payload.comment] }
            : caff
        ),
      };
    case ActionType.UPLOAD_CAFF_FILE:
      return {
        ...state,
        uploadDone: true,
        caff_files: [...state.caff_files, action.payload],
      };
    case ActionType.DELETE_CAFF_FILE:
      return {
        ...state,
        caff_files: state.caff_files.filter((caff) => {
          return caff.id !== action.payload;
        }),
      };
    case ActionType.RESET_UPLOAD_DONE:
      return {
        ...state,
        uploadDone: false,
      };
    default:
      return state;
  }
};

export default reducer;
