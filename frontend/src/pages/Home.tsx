import HomeBanner from '../components/home/HomeBanner.tsx';
import HomeMiniNav from '../components/home/HomeMiniNav.tsx';
import HomeContents from '../components/home/HomeContents.tsx';
import React, { useEffect } from 'react';
import axios from "axios";

const Home: React.FC = () => {
    useEffect(() => {
        const fetchData = async () => {
            const urlParams = new URLSearchParams(window.location.search);
            const loginMethod = urlParams.get('loginMethod');

            if (loginMethod === 'social') {
                await handleSocialLogin(); // handleSocialLogin에서 await 사용
            }
        };

        fetchData().catch(error => {
            console.error('데이터를 가져오는 중 에러 발생', error);
        });
    }, []);

    const handleSocialLogin = async () => {
        const accessToken = localStorage.getItem('access');

        if (!accessToken) {
            const response = await checkAccessToken();
            if (response) {
                const { access } = response.headers;
                localStorage.setItem('access', access);
            } else {
                alert('액세스 토큰을 로컬스토리지에 저장 실패.');
            }
        }
    };

    const checkAccessToken = async () => {
        try {
            const response = await axios.get('http://localhost:8081/change', {
                withCredentials: true
            });
            return response; // 성공적으로 응답을 받은 경우 응답 반환
        } catch (error) {
            console.error('쿠키를 응답헤더로 반환 실패', error);
            return null; // 실패 시 null 반환
        }
    }
    return (
        <div
            className="flex flex-wrap items-center justify-between max-w-screen-xl mx-auto p-4 bg-custom-light-bg dark:bg-custom-dark-bg">
            <div className="w-full">
                <HomeBanner />
                <HomeMiniNav />
                <HomeContents />
            </div>
        </div>
    );
};

export default Home;
