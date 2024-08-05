export interface User {
  id: string;
  email: string;
  password: string;
  nickname: string;
  phone: string;
  verificationCode: string;
}

export interface UserLogin extends Pick<User, 'email' | 'password'> {}
export interface UserSignUp extends Omit<User, 'id'> {}