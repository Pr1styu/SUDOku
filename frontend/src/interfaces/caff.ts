import IComment from './comment';

export default interface ICaff {
  comments: IComment[];
  fileName: string;
  id: number;
  metaData: string[];
  preview: string;
  size: number;
}
