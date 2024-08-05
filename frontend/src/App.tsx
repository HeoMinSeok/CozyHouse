import React from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import useDarkModeStore from './stores/useDarkModeStore';

import Header from './components/common/Header';
import Footer from './components/common/Footer';

const App: React.FC = () => {
  const location = useLocation();
  const isAuthPage = location.pathname === '/login' || location.pathname === '/signup';

  const darkMode = useDarkModeStore((state) => state.darkMode);
  const setDarkMode = useDarkModeStore((state) => state.setDarkMode);

  useEffect(() => {
    setDarkMode(localStorage.getItem('darkMode') === 'true');
  }, [setDarkMode]);

  return (
    <div className={`flex flex-col min-h-screen ${darkMode ? 'dark:bg-custom-dark-bg' : 'bg-custom-light-bg'}`}>
      {!isAuthPage && <Header darkMode={darkMode} />}
      <main className={`flex-grow ${!isAuthPage ? 'mt-20' : ''}`}>
        <Outlet context={{ darkMode }} />
      </main>
      {!isAuthPage && <Footer />}
    </div>
  );
};

export default App;
