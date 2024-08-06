import HomeBanner from '../components/home/HomeBanner.tsx';
import HomeMiniNav from '../components/home/HomeMiniNav.tsx';
import HomeContents from '../components/home/HomeContents.tsx';
import React, {useEffect} from 'react';
import axios from "axios";
import userService from "../api/userService.ts";

const Home: React.FC = () => {
    //소셜로그인이면 엑세스 쿠키를 응답헤더로 변환 후 로컬에 저장
    useEffect(() => {
        const fetchData = async () => {
            const urlParams = new URLSearchParams(window.location.search);
            const loginMethod = urlParams.get('loginMethod');

            if (loginMethod === 'social') {
                await handleSocialLogin();
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
                const {access} = response.headers;
                localStorage.setItem('access', access);

                const userResponse = await axios.get(`${userService.BASE_URL}/users`, {
                    headers: {Authorization: `Bearer ${access}`},
                    withCredentials: true
                });
                console.log('Response Status:', userResponse.status);
                console.log('Response Data:', userResponse.data);
            } else {
                alert('액세스 토큰을 로컬스토리지에 저장 실패.');
            }
        }
    };

    const checkAccessToken = async () => {
        try {
            const response = await axios.get(`${userService.BASE_URL}/change`, {
                withCredentials: true
            });
            return response;
        } catch (error) {
            console.error('쿠키를 응답헤더로 반환 실패', error);
            return null;
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
