export interface UserAuthResponse {
  token: string;
  firstName: string;
  lastName: string;
  email: string;
  role: 'ADMIN' | 'USER'
}
