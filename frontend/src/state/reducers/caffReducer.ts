import { ActionType } from '../action-types';
import { CaffAction } from '../actions';
import ICaff from '../../interfaces/caff';

const initialState = {
  preloadDone: false,
  downloadDone: [],
  caff_files: [],
  downloadFile: null,
};

type caffState = {
  preloadDone: boolean;
  downloadDone: number[];
  caff_files: ICaff[];
  downloadFile: Blob | null;
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
        downloadFile: new Blob([action.payload.file]),
        downloadDone: [...state.downloadDone, action.payload.id],
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
    default:
      return state;
  }
};

export default reducer;
