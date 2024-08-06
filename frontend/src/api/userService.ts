// import axios from 'axios';
import { UserLogin, UserSignUp, User } from './../types/userType';
import axios from 'axios';

export default class userService {
  // static BASE_URL = "/api";
  static BASE_URL = "http://localhost:8081";

  /* 회원가입 Axios */
  static async signUp(userSignup: UserSignUp): Promise<User | null> {
    try {
      const response = await axios.post(
          `${this.BASE_URL}/users/signup`, userSignup,);

      if (response.status !== 200) {
        throw new Error('회원가입에 실패했습니다.');
      }
      const newUser: User = response.data;
      return newUser;
    } catch (e) {
      console.error("회원가입 에러", e);
      throw new Error('회원가입 중 문제가 발생했습니다.');
    }
  }

  /* 일반로그인 FETCH */
  static async login(userLogin: UserLogin): Promise<User | null> {
    const { email, password } = userLogin;
    if (!email || !password) {
      console.error('이메일 또는 비밀번호가 비어 있습니다.');
      throw new Error('이메일 또는 비밀번호가 비어 있습니다.');
    }
    try {
      const response = await axios.post(`${this.BASE_URL}/login`, {
        email,
        password
      }, {
        withCredentials: true
      });

      const { access } = response.headers;
      localStorage.setItem('access', access);

      //토큰을 이용한 사용자 정보추출
      const userResponse = await axios.get(`${this.BASE_URL}/users`, {
        headers: { Authorization: `Bearer ${access}` },
        withCredentials: true
      });

      console.log("userResponse.data = " + userResponse.data.email)
      console.log("userResponse.data = " + userResponse.data.nickname)
      const newUser: User = userResponse.data;
      return newUser;
    } catch (e) {
      console.error("로그인 에러", e);
      throw e;
    }
  }

  // 인증번호 전송
  static async sendVerificationCode(phone: string): Promise<string> {
    try {
      const response = await axios.post(
          `${this.BASE_URL}/sms/send`, { phone });

      if (response.status !== 200) {
        throw new Error('인증번호 전송에 실패했습니다.');
      }
      return response.data;
    } catch (e) {
      console.error("인증번호 전송 에러", e);
      throw e;
    }
  }

  // 인증번호 검증
  static async verifyVerificationCode(phone: string, verificationCode: string): Promise<string> {
      try {
          const response = await axios.post(
              `${this.BASE_URL}/sms/verify`,
              {phone, verificationCode}
          );

          if (response.status !== 200) {
              throw new Error('인증번호 검증에 실패했습니다.');
          }
          return response.data;
      } catch (e) {
          console.error("인증번호 검증 에러", e);
          throw e;
      }
  }
}

  /* 서버 연결 후 BASE_URL 및 AXIOS로 변경 */
