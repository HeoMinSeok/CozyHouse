import React from 'react';
import {Outlet, useLocation} from 'react-router-dom';
import {useEffect, useCallback} from 'react';
import useDarkModeStore from './stores/useDarkModeStore';
import {jwtDecode} from 'jwt-decode';
import Header from './components/common/Header';
import Footer from './components/common/Footer';
import useUserStore from './stores/useUserStore';

//Strict Mode으로 중복요청이 되는 현상 (중복 요청 방지 플래그?)
//개발 환경에서만 발생하며, 실제 프로덕션 빌드에서는 이러한 문제가 발생하지 않음?
const App: React.FC = () => {
    const location = useLocation();
    const isAuthPage = location.pathname === '/login' || location.pathname === '/signup';
    const isPostPage = location.pathname.startsWith('/contents/write') ||
        location.pathname.startsWith('/projects/write') ||
        location.pathname.startsWith('/advices/write') ||
        location.pathname.startsWith('/production_reviews/write');

    const darkMode = useDarkModeStore((state) => state.darkMode);
    const setDarkMode = useDarkModeStore((state) => state.setDarkMode);
    const {handleSocialLogin, getMyProfile, reissueAccessToken} = useUserStore();

    useEffect(() => {
        setDarkMode(localStorage.getItem('darkMode') === 'true');
    }, [setDarkMode]);

    const [isLoggedIn, setIsLoggedIn] = React.useState(false);

    const fetchProfileInfo = useCallback(async () => {
        try {
            await getMyProfile();
        } catch (error) {
            console.error("프로필 정보를 가져오는 중 오류 발생:", error);
        }
    }, []);

    const expirationTime = 50000;

    const isTokenExpired = (token: string | null, expirationTime: number) => {
        if (!token) return true;

        const decodedToken = jwtDecode(token);
        const currentTime = Date.now() / 1000;

        return decodedToken.exp < currentTime || (currentTime - decodedToken.iat) >= expirationTime;
    };

    useEffect(() => {
        const accessToken = localStorage.getItem('access');
        const loginMethod = new URLSearchParams(window.location.search).get('loginMethod');

        const checkToken = async () => {
            if (accessToken) {
                // 토큰이 만료된 경우
                if (isTokenExpired(accessToken, expirationTime)) {
                    console.log('토큰이 만료되었습니다. 리프레시 토큰으로 재발급 시도 중...');
                    try {
                        await reissueAccessToken();
                        console.log('새로운 액세스 토큰이 발급되었습니다.');

                        // 새로운 토큰으로 소셜 로그인 처리
                        if (loginMethod === 'social') {
                            console.log('소셜 로그인 처리 중...');
                            await handleSocialLogin(); // 소셜 로그인 처리
                            setIsLoggedIn(true);
                            // 새로운 토큰으로 일반 로그인처리
                        } else {
                            await fetchProfileInfo(); // 사용자 정보 가져오기
                            setIsLoggedIn(true);
                        }
                    } catch (error) {
                        console.error('리프레시 토큰으로 재발급 중 에러 발생:', error);
                    }
                } else {
                    // 유효한 토큰일 경우
                    console.log('유효한 액세스 토큰입니다.');

                    // 유효한 토큰이면서 소셜 로그인인 경우
                    if (loginMethod === 'social') {
                        console.log('소셜 로그인 처리 중...');
                        await handleSocialLogin();
                        setIsLoggedIn(true);
                    } else {
                        // 유효한 토큰이면서 일반로그인 경우
                        await fetchProfileInfo();
                        setIsLoggedIn(true);
                    }
                }
            }
        };

        checkToken().catch(error => {
            console.error('토큰 확인 중 에러 발생', error);
        });
    }, []);

    return (
        <div className={`flex flex-caol min-h-screen ${darkMode ? 'dark:bg-custom-dark-bg' : 'bg-custom-light-bg'}`}>
            {!isAuthPage && !isPostPage && <Header darkMode={darkMode}/>}
            <main className={`flex-grow ${!isAuthPage && !isPostPage ? 'mt-20' : ''}`}>
                <Outlet context={{darkMode}}/>
            </main>
            {!isAuthPage && !isPostPage && <Footer/>}
        </div>
    );
};

export default App;