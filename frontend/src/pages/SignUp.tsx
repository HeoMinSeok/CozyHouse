import { useOutletContext, Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { useDebounce } from 'use-debounce';
import useUserStore from '../stores/useUserStore';
import { UserSignUp } from '../types/userType';

import icons from '../assets/icons';

import Toast, { showToast } from '../components/utils/Toast';

interface SignUpFormState extends UserSignUp {
  confirmPassword: string;
  verificationCode: string;
}

const SignUp = () => {
  const { darkMode } = useOutletContext<{ darkMode: boolean }>();
  const navigate = useNavigate();
  const { signUp, sendVerificationCode, verifyVerificationCode } = useUserStore();

  const [formState, setFormState] = useState<SignUpFormState>({
    email: '',
    password: '',
    confirmPassword: '',
    phone: '',
    nickname: '',
    verificationCode: ''
  });

  const [errors, setErrors] = useState({
    email: false,
    password: false,
    confirmPassword: false,
    phone: false,
    nickname: false,
    verificationCode: false
  });

  const [verificationSent, setVerificationSent] = useState(false);
  const [remainingTime, setRemainingTime] = useState<string>('05:00');
  const [dFormState] = useDebounce(formState, 300);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormState(prevState => ({
      ...prevState,
      [name]: value
    }));

    if (value.trim() !== '') {
      setErrors(prevErrors => ({
        ...prevErrors,
        [name]: false
      }));
    }
  };

  useEffect(() => {
    setErrors(prevErrors => ({
      ...prevErrors,
      password: dFormState.password !== dFormState.confirmPassword,
      confirmPassword: dFormState.password !== dFormState.confirmPassword,
    }));
  }, [dFormState]);

  // 인증코드 발급
  const handleVerification = async () => {
    try {
      await sendVerificationCode(formState);
      setVerificationSent(true);
      const expiryTime = Date.now() + 5 * 60 * 1000; // 5분 후
      startCountdown(expiryTime);
      alert('인증번호가 전송되었습니다.');
    } catch (e) {
      console.error('인증번호 전송 실패', e);
      alert('인증번호 전송에 실패했습니다.');
    }
  };

  // 인증코드 검증
  const handleVerifyCode = async () => {
    try {
      await verifyVerificationCode(formState);
      alert('인증이 완료되었습니다.');
    } catch (e) {
      console.error('인증 코드 검증 실패', e);
      alert('인증 코드 검증에 실패했습니다.');
    }
  };

  // 타이머 함수
  const startCountdown = (expiryTime: number) => {
    const interval = setInterval(() => {
      const now = Date.now();
      const timeLeft = expiryTime - now;
      if (timeLeft <= 0) {
        clearInterval(interval);
        setRemainingTime('00:00');
      } else {
        const minutes = Math.floor(timeLeft / 1000 / 60);
        const seconds = Math.floor((timeLeft / 1000) % 60);
        setRemainingTime(`${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`);
      }
    }, 1000);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { confirmPassword, ...userData } = formState;
    const newErrors = {
      email: !formState.email,
      password: !formState.password,
      confirmPassword: formState.password !== formState.confirmPassword,
      phone: !formState.phone,
      nickname: !formState.nickname,
      verificationCode: !formState.verificationCode
    };

    setErrors(newErrors);

    // if (Object.values(newErrors).some(error => error)) {
    //   showToast('모든 필드를 입력해주세요', 'warn', darkMode);
    //   const firstErrorField = Object.keys(newErrors).find(field => newErrors[field as keyof typeof newErrors]);
    //   if (firstErrorField) {
    //     (document.getElementsByName(firstErrorField)[0] as HTMLInputElement).focus();
    //   }
    //   return;
    // }

    try {
      await signUp(userData);
      showToast('WELCOM ' + `${formState.nickname}` + '님 👏🏻', 'success', darkMode);
      setTimeout(() => {
        navigate('/login');
      }, 1500);  
    } catch (error) {
      showToast(`${error}`, 'error', darkMode);
    }
  };

  return (
    <div className={`min-h-screen py-6 flex flex-col justify-center sm:py-12 ${darkMode ? 'dark:bg-custom-dark-bg' : 'bg-custom-light-bg'}`}>
      <div className="relative py-3 sm:max-w-xl sm:mx-auto">
        <div className="absolute inset-0 bg-gradient-to-r from-cyan-400 to-sky-500 shadow-lg transform -skew-y-6 sm:skew-y-0 sm:-rotate-6 sm:rounded-3xl"></div>
        <div className="relative px-4 py-10 border-4 bg-blue-50 shadow-lg sm:rounded-3xl sm:p-20 dark:bg-gray-700">
          <div className="max-w-md mx-auto">
            <div className="flex flex-col items-center justify-center mb-6 hover:cursor-pointer"
              onClick={() => navigate("/")}>
              <img src={icons.MainHome as string} className="h-16 w-16 mb-4" alt="CozyHouse Logo" />
              <h1 className="text-2xl font-semibold text-gray-900 dark:text-white text-center">CozyHouse</h1>
            </div>
            <div>
              <form onSubmit={handleSubmit}
                className="py-4 text-base leading-6 space-y-4 text-gray-700 dark:text-gray-400 sm:text-lg sm:leading-7">
                <div className="relative">
                  <input
                    name="email"
                    value={formState.email}
                    onChange={handleChange}
                    autoComplete="off"
                    type="email"
                    className={`peer placeholder-transparent h-10 w-full border-b-2 ${errors.email ? 'border-red-500' : 'border-gray-300'} text-gray-900 dark:text-white focus:outline-none focus:border-sky-400 bg-transparent`}
                    placeholder="Email address"
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
                    name="password"
                    value={formState.password}
                    onChange={handleChange}
                    autoComplete="off"
                    type="password"
                    className={`peer placeholder-transparent h-10 w-full border-b-2 ${errors.password ? 'border-red-500' : formState.password && formState.confirmPassword && formState.password === formState.confirmPassword ? 'border-green-500' : 'border-gray-300'} text-gray-900 dark:text-white focus:outline-none focus:border-sky-400 bg-transparent`}
                    placeholder="Password"
                  />
                  <label
                    htmlFor="password"
                    className="absolute left-0 -top-3.5 text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-gray-600 dark:peer-focus:text-gray-400 peer-focus:text-sm transition-all"
                  >
                    비밀번호
                  </label>
                </div>
                <div className="relative">
                  <input
                    name="confirmPassword"
                    value={formState.confirmPassword}
                    onChange={handleChange}
                    autoComplete="off"
                    type="password"
                    className={`peer placeholder-transparent h-10 w-full border-b-2 ${errors.confirmPassword ? 'border-red-500' : formState.password && formState.confirmPassword && formState.password === formState.confirmPassword ? 'border-green-500' : 'border-gray-300'} text-gray-900 dark:text-white focus:outline-none  focus:border-sky-400 bg-transparent`}
                    placeholder="Confirm Password"
                  />
                  <label
                    htmlFor="confirmPassword"
                    className="absolute left-0 -top-3.5 text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-gray-600 dark:peer-focus:text-gray-400 peer-focus:text-sm transition-all"
                  >
                    비밀번호 확인
                  </label>
                </div>
                <div className="relative">
                  <div className='flex'>
                    <input
                      name="phone"
                      value={formState.phone}
                      onChange={handleChange}
                      autoComplete="off"
                      type="text"
                      className={`peer placeholder-transparent h-10 w-full border-b-2 ${errors.phone ? 'border-red-500' : 'border-gray-300'} text-gray-900 dark:text-white focus:outline-none focus:border-sky-400 bg-transparent`}
                      placeholder="Phone"
                    />
                    <label
                      htmlFor="phone"
                      className="absolute left-0 -top-3.5 text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-gray-600 dark:peer-focus:text-gray-400 peer-focus:text-sm transition-all"
                    >
                      휴대폰
                    </label>
                    <div className='text-nowrap'>
                      <button type="button" className="text-sm ml-4 border-2 text-sky-500 dark:text-white border-gray-300 rounded-md px-4 py-2 h-11" onClick={handleVerification}>
                        인증
                      </button>
                    </div>
                  </div>
                </div>
                {verificationSent && (
                  <div className="relative">
                    <input
                      name="verificationCode"
                      value={formState.verificationCode}
                      onChange={handleChange}
                      autoComplete="off"
                      type="text"
                      className={`peer placeholder-transparent h-10 w-full border-b-2 ${errors.verificationCode ? 'border-red-500' : 'border-gray-300'} text-gray-900 dark:text-white focus:outline-none focus:border-sky-400 bg-transparent`}
                      placeholder="Verification Code"
                    />
                    <label
                      htmlFor="verificationCode"
                      className="absolute left-0 -top-3.5 text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-gray-600 dark:peer-focus:text-gray-400 peer-focus:text-sm transition-all"
                    >
                      인증 코드
                    </label>
                    <button type="button" onClick={handleVerifyCode} className="text-sm mt-2 border-2 text-sky-500 dark:text-white border-gray-300 rounded-md px-4 py-2 h-11">
                      인증 코드 확인
                    </button>
                    <div className="text-sm text-gray-600 dark:text-gray-400 mt-2">
                      인증 코드 만료 시간: {remainingTime}
                    </div>
                  </div>
                )}
                <div className="relative">
                  <input
                    name="nickname"
                    value={formState.nickname}
                    onChange={handleChange}
                    autoComplete="off"
                    type="text"
                    className={`peer placeholder-transparent h-10 w-full border-b-2 ${errors.nickname ? 'border-red-500' : 'border-gray-300'} text-gray-900 dark:text-white focus:outline-none focus:border-sky-400 bg-transparent`}
                    placeholder="Nickname"
                  />
                  <label
                    htmlFor="nickname"
                    className="absolute left-0 -top-3.5 text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-gray-600 dark:peer-focus:text-gray-400 peer-focus:text-sm transition-all"
                  >
                    닉네임
                  </label>
                </div>
                <div className="relative justify-center items-center flex">
                  <button type="submit" className="w-full bg-sky-500 text-white rounded-md px-2 py-1">회원가입</button>
                </div>
              </form>
              <div className="items-center justify-center flex gap-2 text-gray-900 dark:text-slate-300">
                <span>이미 아이디가 있으신가요?</span>
                <span className='border-b border-gray-400 dark:border-gray-300'><Link to="/login">로그인</Link></span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <Toast darkMode={darkMode} />
    </div>
  );
};

export default SignUp;
