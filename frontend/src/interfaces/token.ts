export default interface IToken {
  access_token?: string | null;
  refresh_token?: string | null;
  id_token?: string | null;
  scope?: string | null;
  token_type?: string | null;
  expires_in?: number | null;
}
