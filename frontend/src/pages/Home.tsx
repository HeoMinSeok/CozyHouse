import HomeBanner from '../components/home/HomeBanner.tsx';
import HomeMiniNav from '../components/home/HomeMiniNav.tsx';
import HomeContents from '../components/home/HomeContents.tsx';
import React, {useEffect} from 'react';
import useUserStore from '../stores/useUserStore';

const Home: React.FC = () => {
    const { handleSocialLogin,  user  } = useUserStore();

    useEffect(() => {
        const fetchData = async () => {
            const urlParams = new URLSearchParams(window.location.search);
            const loginMethod = urlParams.get('loginMethod');

            if (loginMethod === 'social') {
                await handleSocialLogin(); // 소셜 로그인 처리
            }

            // await fetchUserInfo(); // 사용자 정보 가져오기
        };

        fetchData().catch(error => {
            console.error('데이터를 가져오는 중 에러 발생', error);
        });
    }, [handleSocialLogin]);

    return (
        <div className="flex flex-wrap items-center justify-between max-w-screen-xl mx-auto p-4 bg-custom-light-bg dark:bg-custom-dark-bg">
            <div className="w-full">
                <div className="user-info">
                    {user ? (
                        <h2>안녕하세요, {user.nickname}님!</h2>
                    ) : (
                        <h2>로그인된 유저가 없습니다.</h2>
                    )}
                </div>
                <HomeBanner />
                <HomeMiniNav />
                <HomeContents />
            </div>
        </div>
    );
};

export default Home;
