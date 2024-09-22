import { create } from "zustand";
import { devtools, DevtoolsOptions } from "zustand/middleware";
import { User, UserLogin, UserSignUp } from "../types/userType";
import userService from "../api/userService";

interface UserState {
    user: User | null;
    login: (userLogin: UserLogin) => Promise<void>;
    signUp: (userSignUp: UserSignUp) => Promise<void>;
    sendVerificationCode: (userSignUp: UserSignUp) => Promise<string>;
    verifyVerificationCode: (userSignUp: UserSignUp) => Promise<string>;
    getMyProfile: () => Promise<void>;
    handleSocialLogin: () => Promise<void>;
    reissueAccessToken: () => Promise<void>;
}

const useUserStore = create<UserState>()(
    devtools(
        (set) => ({
            user: null,
            verificationCodeSent: false,
            verificationCodeVerified: false,
            login: async (userLogin: UserLogin) => {
                try {
                    const user = await userService.login(userLogin);
                    set({ user });
                } catch (error) {
                    console.error('로그인 실패:', error);
                    throw Error('로그인에 실패했습니다');
                }
            },
            signUp: async (userSignup: UserSignUp) => {
                try {
                    const newUser = await userService.signUp(userSignup);
                    set({ user: newUser });
                } catch (error) {
                    console.error('회원가입 실패:', error);
                    throw error;
                }
            },
            sendVerificationCode: async (userSignUp: UserSignUp) => {
                try {
                    return await userService.sendVerificationCode(userSignUp.phone);
                } catch (error) {
                    console.error('인증번호 보내기 실패:', error);
                    throw error;
                }
            },
            verifyVerificationCode: async (userSignUp: UserSignUp) => {
                try {
                    return await userService.verifyVerificationCode(userSignUp.phone, userSignUp.verificationCode);
                } catch (error) {
                    console.error('인증번호 검증 실패:', error);
                    throw error;
                }
            },

            handleSocialLogin: async () => {
                try {
                    const accessToken = localStorage.getItem('access');
                    if (!accessToken) {
                        const response = await userService.changeAccessToken();
                        if (response) {
                            const { access } = response.headers;
                            localStorage.setItem('access', access);
                            const user = await userService.getMyProfile(access);
                            set({ user });
                            return;
                        }
                    }else if(accessToken){
                        const user = await userService.getMyProfile(accessToken);
                        set({ user });
                        return;
                    }
                } catch (error) {
                    console.error('소셜 로그인 처리 중 오류 발생:', error);
                    throw error;
                }
            },

            reissueAccessToken: async () => {
                try {
                    const newAccessToken = await userService.reissueAccessToken();
                    if (newAccessToken) {
                        const user = await userService.getMyProfile(newAccessToken);
                        set({ user });
                    } else {
                        console.error('리프레쉬토큰으로 사용가 정보 가져오기 중 오류 발생.');
                    }
                } catch (error) {
                    console.error('액세스 토큰 재발급 중 오류 발생:', error);
                }
            },

            //사용할 일 없으면 삭제
            getMyProfile: async () => {
                try {
                    const accessToken = localStorage.getItem('access');
                    if (accessToken) {
                        const user = await userService.getMyProfile(accessToken);
                        set({ user });
                    } else {
                        console.error('액세스 토큰이 없습니다.');
                    }
                } catch (error) {
                    console.error('사용자 정보 가져오기 중 오류 발생:', error);
                    throw error;
                }
            },
        }),

        { name: "UserStore" } as DevtoolsOptions
    )
);

export default useUserStore;