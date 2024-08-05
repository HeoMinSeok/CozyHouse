// import axios from 'axios';
import { UserLogin, UserSignUp, User } from './../types/userType';
import axios from 'axios';

export default class userService {
  static BASE_URL = "api";

  /* 로그인 FETCH */
  /* static async login(userLogin: UserLogin): Promise<User | null> {
    const { email, password } = userLogin;
    try {
      const response = await fetch(`${this.BASE_URL}/users`);
      if (!response.ok) {
        throw new Error('네트워크 응답이 실패');
      }

      const users: User[] = await response.json();
      const foundUser = users.find(user => user.email === email && user.password === password);
      if (!foundUser) {
        throw new Error('회원정보를 확인해주세요 ❗️');
      }
      return foundUser;
    } catch (e) {
      console.error("로그인 에러", e);
      throw e;
    }
  } */

  // /* 회원가입 FETCH */
  /* static async signUp(userSignup: UserSignUp): Promise<User | null> {
    try {
      const response = await fetch(`${this.BASE_URL}/users`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userSignup),
      });
  
      if (!response.ok) {
        throw new Error('회원가입에 실패했습니다.');
      }
  
      const newUser: User = await response.json();
      return newUser;
    } catch (e) {
      console.error("회원가입 에러", e);
      throw e;
    }
  } */

  /* 로그인 Axios */
  static async login(userLogin: UserLogin): Promise<User | null> {
    try {
      const response = await axios.post(
        `${this.BASE_URL}/users/login`, userLogin
      );

      if (response.status !== 200) {
        throw new Error('로그인에 실패했습니다.');
      }

      const user: User = response.data;
      return user;
    } catch (e) {
      console.error("로그인 에러", e);
      throw e;
    }
  }

  /* 회원가입 Axios */
  static async signUp(userSignup: UserSignUp): Promise<User | null> {
    try {
      const response = await axios.post(
        `${this.BASE_URL}/users/signup`, userSignup,);

      if (response.status !== 200) {
        throw new Error('회원가입에 실패했습니다.');
      }
      console.log(response);
      const newUser: User = response.data;
      return newUser;
    } catch (e) {
      console.error("회원가입 에러", e);
      throw e;
    }
  }
}

  /* 서버 연결 후 BASE_URL 및 AXIOS로 변경 */
