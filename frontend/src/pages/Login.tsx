import React, { useState } from 'react';
import { useOutletContext, Link, useNavigate } from 'react-router-dom';
import { useDebounce } from 'use-debounce';
import useUserStore from '../stores/useUserStore';

import { VscGithub } from "react-icons/vsc";
import { GoogleLogin, KakaoLogin } from '../assets/svg/SvgIcons';
import icons from '../assets/icons';

import Toast, { showToast } from '../components/utils/Toast';

const Login = () => {
  const navigate = useNavigate();
  const { darkMode } = useOutletContext<{ darkMode: boolean }>();
  const login = useUserStore((state) => state.login);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  /*  디바운스 적용 입력 최적화 */
  const [dEmail] = useDebounce(email, 300);
  const [dPassword] = useDebounce(password, 300);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      await login({ email: dEmail, password: dPassword });
      showToast('Hi 👏🏻', 'success', darkMode);
      setTimeout(() => {
        navigate('/');
      }, 1500);
    } catch (error: any) {
      showToast(`${error}`, 'error', darkMode);
    }
  };

  return (
    <div className={`min-h-screen py-6 flex flex-col justify-center sm:py-12 ${darkMode ? 'dark:bg-custom-dark-bg' : 'bg-custom-light-bg'}`}>
      <div className="relative py-3 sm:max-w-xl sm:mx-auto">
        <div className="absolute inset-0 bg-gradient-to-r from-cyan-400 to-sky-500 shadow-lg transform -skew-y-6 sm:skew-y-0 sm:-rotate-6 sm:rounded-3xl"></div>
        <div className="relative px-4 py-10 border-4 bg-blue-50 shadow-lg sm:rounded-3xl sm:p-20 dark:bg-gray-700">
          <div className="max-w-md mx-auto">
            <div className="flex flex-col items-center justify-center mb-6 hover:cursor-pointer " onClick={() => navigate("/")}>
              <img src={icons.MainHome} className="h-16 w-16 mb-4 " alt="CozyHouse Logo" />
              <h1 className="text-2xl font-semibold text-gray-900 dark:text-white text-center">CozyHouse</h1>
            </div>
            <div>
              <form onSubmit={handleSubmit} className="py-4 text-base leading-6 space-y-4 text-gray-700 dark:text-gray-400 sm:text-lg sm:leading-7">
                <div className="relative">
                  <input
                    autoComplete="off"
                    id="email"
                    name="email"
                    type="email"
                    className="peer placeholder-transparent h-10 w-full border-b-2 border-gray-300 text-gray-900 dark:text-white focus:outline-none focus:border-sky-400 bg-transparent"
                    placeholder="Email address"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                  <label
                    htmlFor="email"
                    className="absolute left-0 -top-3.5 text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-gray-600 dark:peer-focus:text-gray-400 peer-focus:text-sm transition-all"
                  >
                    이메일
                  </label>
                </div>
                <div className="relative">
                  <input
                    autoComplete="off"
                    id="password"
                    name="password"
                    type="password"
                    className="peer placeholder-transparent h-10 w-full border-b-2 border-gray-300 text-gray-900 dark:text-white focus:outline-none focus:border-sky-400 bg-transparent"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                  <label
                    htmlFor="password"
                    className="absolute left-0 -top-3.5 text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-gray-600 dark:peer-focus:text-gray-400 peer-focus:text-sm transition-all"
                  >
                    비밀번호
                  </label>
                </div>
                <div className="relative justify-center items-center flex">
                  <button type="submit" className="w-full bg-sky-500 text-white rounded-md px-2 py-1">로그인</button>
                </div>
              </form>
              <div className="items-center justify-center flex gap-8 text-gray-900 dark:text-slate-300 mb-4">
                <span><Link to="/">비밀번호 재설정</Link></span>
                <span><Link to="/signup">회원가입</Link></span>
              </div>
            </div>
          </div>
          <p className="text-center text-gray-900 dark:text-white text-xs mb-1">SNS계정으로 간편 로그인/회원가입</p>
          <div className="flex items-center justify-center space-x-4">
            <a
              href="api/oauth2/authorization/google"
              className="flex items-center justify-center bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-700 rounded-lg shadow-md p-2 text-sm font-medium text-gray-800 dark:text-white hover:bg-gray-200 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 w-12 h-12"
            >
              <GoogleLogin />
            </a>
            <a
              href="/api/oauth2/authorization/kakao"
              className="flex items-center justify-center bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-700 rounded-lg shadow-md p-2 text-sm font-medium text-gray-800 dark:text-white hover:bg-gray-200 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 w-12 h-12"
            >
              <KakaoLogin />
            </a>
            <a
              href="/api/oauth2/authorization/github"
              className="flex items-center justify-center bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-700 rounded-lg shadow-md p-2 text-sm font-medium text-gray-800 dark:text-white hover:bg-gray-200 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 w-12 h-12"
            >
              <VscGithub style={{ width: "100%", height: "100%" }} />
            </a>
          </div>
        </div>
      </div>
      <Toast darkMode={darkMode} />
    </div>
  );
}

export default Login;
