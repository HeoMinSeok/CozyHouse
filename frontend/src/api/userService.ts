// import axios from 'axios';
import {UserLogin, UserSignUp, User} from './../types/userType';
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
        const {email, password} = userLogin;
        if (!email || !password) {
            console.error('이메일 또는 비밀번호가 비어 있습니다.');
            throw new Error('이메일 또는 비밀번호가 비어 있습니다.');
        }
        try {
            const response = await axios.post(`${this.BASE_URL}/login`,
                {email, password}, {withCredentials: true});
            const {access} = response.headers;
            localStorage.setItem('access', access);

            return await this.getMyProfile(access);
        } catch (e) {
            console.error("로그인 에러", e);
            throw e;
        }
    }

    // 인증번호 전송
    static async sendVerificationCode(phone: string): Promise<string> {
        try {
            const response = await axios.post(
                `${this.BASE_URL}/sms/send`, {phone});

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

    /* 액세스 토큰 변환 */
    static async changeAccessToken() {
        try {
            const response = await axios.get(`${this.BASE_URL}/change`, {
                withCredentials: true,
            });
            document.cookie = "access=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

            return response;
        } catch (e) {
            console.error('쿠키를 응답헤더로 반환 실패', e);
            return null;
        }
    }

    /* 액세스 토큰을 이용해 사용자 정보 가져오기 */
    static async getMyProfile(accessToken: string): Promise<User | null> {
        try {
            const userResponse = await axios.get(`${this.BASE_URL}/users`, {
                headers: {Authorization: `Bearer ${accessToken}`},
            });
            return userResponse.data;
        } catch (e) {
            console.error('사용자 정보 가져오기 실패:', e);
            return null;
        }
    }

    /* 리프레시 토큰을 이용해 액세스 토큰 재발급 */
    static async reissueAccessToken() {
        try {
            const response = await axios.post(`${this.BASE_URL}/reissue`,{}, {
                withCredentials: true
            });
            const { access } = response.headers;
            localStorage.setItem('access', access);
            return access;
        } catch (e) {
            console.error('액세스 토큰 재발급 실패:', e);
            return null;
        }
    }
}


/* 서버 연결 후 BASE_URL 및 AXIOS로 변경 */
