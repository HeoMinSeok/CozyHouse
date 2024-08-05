import HomeBanner from '../components/home/HomeBanner';
import HomeContents from '../components/home/HomeContents';
import HomeMiniNav from '../components/home/HomeMiniNav';

const Home: React.FC = () => {
  return (
    <div className="flex flex-col items-center justify-between w-full bg-custom-light-bg dark:bg-custom-dark-bg">
      <div className="max-w-screen-xl w-full p-4">
        <HomeBanner />
        <HomeMiniNav />
        <HomeContents />
      </div>
    </div>
  );
};

export default Home;
