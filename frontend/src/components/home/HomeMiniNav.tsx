import React from 'react';
import { FcAlarmClock, FcHome, FcBearish } from 'react-icons/fc';
import { Link } from 'react-router-dom';

const icons = [
  { icon: <FcBearish />, label: '세일이다 ！', path: '/' },
  { icon: <FcHome />, label: '집들이', path: '/' },
  { icon: <FcAlarmClock />, label: '오늘의 딜', path: '/' },
  { icon: <FcAlarmClock />, label: '오늘의 딜', path: '/' },
  { icon: <FcAlarmClock />, label: '오늘의 딜', path: '/' },
  { icon: <FcAlarmClock />, label: '오늘의 딜', path: '/' },
  { icon: <FcAlarmClock />, label: '오늘의 딜', path: '/' },
  { icon: <FcAlarmClock />, label: '오늘의 딜', path: '/' },
];

const HomeMiniNav: React.FC = () => {
  return (
    <div className="flex flex-wrap px-3 gap-4 w-full justify-center md:justify-center md:gap-10 my-16">
      {icons.map((item, index) => (
        <Link to={item.path} key={index} className="text-center mx-4">
          <div className="flex justify-center">
            {React.cloneElement(item.icon, { className: "w-16 h-16" })}
          </div>
          <span className="text-gray-700 dark:text-white">{item.label}</span>
        </Link>
      ))}
    </div>
  );
};

export default HomeMiniNav;
