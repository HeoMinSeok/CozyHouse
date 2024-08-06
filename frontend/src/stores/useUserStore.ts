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
            throw new Error('로그인에 실패했습니다');
        }
      },
      signUp: async (userSignup: UserSignUp) => {
        try {
          const newUser = await userService.signUp(userSignup);
          set({ user: newUser });
        } catch (error) {
          throw error;
        }
      },
      sendVerificationCode: async (userSignUp: UserSignUp) => {
        try {
          return await userService.sendVerificationCode(userSignUp.phone);
        } catch (error) {
          throw error;
        }
      },
      verifyVerificationCode: async (userSignUp: UserSignUp) => {
        try {
          return await userService.verifyVerificationCode(userSignUp.phone, userSignUp.verificationCode);
        } catch (error) {
          throw error;
        }
      },
    }),

    { name: "UserStore" } as DevtoolsOptions
  )
);

export default useUserStore;

