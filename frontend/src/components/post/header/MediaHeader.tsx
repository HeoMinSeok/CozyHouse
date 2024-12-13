import React, { useState } from 'react';
import { Link, useNavigate } from "react-router-dom";
import icons from '../../../assets/icons';

const MediaHeader: React.FC = () => {
  const [activeTab, setActiveTab] = useState('photo');
  const navigate = useNavigate();

  const handleTabClick = (tab: string) => {
    setActiveTab(tab);
    const newUrl = tab === 'photo' ? '/contents/write/media=photo' : '/contents/write/media=video';
    navigate(newUrl);
  };

  return (
    <header className="fixed w-full z-10 ">
      <nav className="bg-custom-light-bg border-gray-200 dark:bg-custom-dark-bg dark:border-gray-700 border-b-2">
        <div className="flex flex-wrap items-center justify-between max-w-screen-xl mx-auto p-4">
          <Link to="/" className="flex items-center space-x-3 rtl:space-x-reverse">
            <img src={icons.MainHome} className="h-8" alt="Flowbite Logo" />
            <span className="self-center text-2xl font-semibold whitespace-nowrap text-gray-900 dark:text-custom-dark-text">CozyHouse</span>
          </Link>

          <button className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 font-medium rounded-lg text-sm px-4 py-2 md:px-5 md:py-2.5 focus:outline-none">
            올리기
          </button>
        </div>
        <div className="flex items-center justify-center">
          <button
            onClick={() => handleTabClick('photo')}
            className={`text-lg font-medium px-4 py-2 ${activeTab === 'photo' ? 'border-b-2 border-blue-500 text-blue-500' : 'text-gray-500'}`}
          >
            사진
          </button>
          <button
            onClick={() => handleTabClick('video')}
            className={`text-lg font-medium px-4 py-2 ${activeTab === 'video' ? 'border-b-2 border-blue-500 text-blue-500' : 'text-gray-500'}`}
          >
            동영상
          </button>
        </div>
      </nav>
    </header>
  );
};

export default MediaHeader;
