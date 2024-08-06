import { useEffect } from "react";
import { Link } from "react-router-dom";
import useDropdownStore from "../../stores/useDropdownStore";
import useDarkModeStore from "../../stores/useDarkModeStore";
import icons from "../../assets/icons";
import { LightModeIcon, DarkModeIcon } from "../../assets/svg/SvgIcons";
import { FaUserFriends, FaSlackHash, FaHouseUser, FaListUl, FaThumbsUp, FaFireAlt, FaSearch, FaAngleDown } from "react-icons/fa";
import { FaStar, FaShopify } from "react-icons/fa6";

const Header: React.FC<{ darkMode: boolean }> = ({ darkMode }) => {
  const isCommunityOpen = useDropdownStore((state) => state.isCommunityOpen);
  const isShoppingOpen = useDropdownStore((state) => state.isShoppingOpen);
  const isPostOpen = useDropdownStore((state) => state.isPostOpen);
  const toggleCommunity = useDropdownStore((state) => state.toggleCommunity);
  const toggleShopping = useDropdownStore((state) => state.toggleShopping);
  const togglePost = useDropdownStore((state) => state.togglePost);
  const closeAll = useDropdownStore((state) => state.closeAll);

  const toggleDarkMode = useDarkModeStore((state) => state.toggleDarkMode);

  useEffect(() => {
    const handleClickOutside = () => {
      closeAll();
    };

    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, [closeAll]);

  const handleDropdownClick = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  return (
    <header className="fixed w-full z-10">
      <nav className="bg-custom-light-bg border-gray-200 dark:bg-custom-dark-bg dark:border-gray-700">
        <div className="flex flex-wrap items-center justify-between max-w-screen-xl mx-auto p-4">
          <Link to="/" className="flex items-center space-x-3 rtl:space-x-reverse">
            <img src={icons.MainHome} className="h-8" alt="Flowbite Logo" />
            <span className="self-center text-2xl font-semibold whitespace-nowrap text-gray-900 dark:text-custom-dark-text">CozyHouse</span>
          </Link>
          <div className="flex items-center md:order-2 justify-center w-full md:w-auto space-x-1 md:space-x-1 rtl:space-x-reverse gap-3">
            <Link to="/login" className="text-gray-900 dark:text-custom-dark-text hover:bg-gray-50 dark:hover:bg-gray-700 focus:ring-4 focus:ring-gray-300 dark:focus:ring-gray-800 font-medium rounded-lg text-sm px-4 py-2 md:px-2 md:py-2.5 focus:outline-none">로그인</Link>
            <Link to="/signup" className="text-gray-900 dark:text-custom-dark-text hover:bg-gray-50 dark:hover:bg-gray-700 focus:ring-4 focus:ring-gray-300 dark:focus:ring-gray-800 font-medium rounded-lg text-sm px-4 py-2 md:px-2 md:py-2.5 focus:outline-none">회원가입</Link>
            <Link to="/customer-center" className="text-gray-900 dark:text-custom-dark-text hover:bg-gray-50 dark:hover:bg-gray-700 focus:ring-4 focus:ring-gray-300 dark:focus:ring-gray-800 font-medium rounded-lg text-sm px-4 py-2 md:px-2 md:py-2.5 focus:outline-none">고객센터</Link>
            <div className="relative">
              <button onClick={(e) => { togglePost(); handleDropdownClick(e); }} className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 font-medium rounded-lg text-sm px-4 py-2 md:px-5 md:py-2.5 focus:outline-none">
                글쓰기 <FaAngleDown className="inline-block ml-2" />
              </button>
              {isPostOpen && (
                <div className="absolute left-1/2 transform -translate-x-1/2 mt-2 w-[350px] bg-white rounded-md shadow-lg dark:bg-gray-800 text-gray-900 dark:text-white">
                  <ul>
                    <Link to="/contents/write/media=photo">
                      <li className="flex items-center hover:bg-gray-100 dark:hover:bg-gray-700 py-4 px-6">
                        <img src={icons.PostMenuGallery} className="w-11 h-11 mr-5" alt="사진/동영상" />
                        <div>
                          <span className="block font-medium">사진/동영상 올리기</span>
                          <span className="text-sm text-gray-500 dark:text-gray-400">우리 집의 공간과 나의 일상을 기록해 보세요.</span>
                        </div>
                      </li>
                    </Link>
                    <Link to="/projects/write">
                      <li className="flex items-center hover:bg-gray-100 dark:hover:bg-gray-700 py-4 px-6">
                        <img src={icons.PostMenuHouses} className="w-11 h-11 mr-5" alt="집들이" />
                        <div>
                          <span className="block font-medium">집들이 글쓰기</span>
                          <span className="text-sm text-gray-500 dark:text-gray-400">집에 관한 이야기를 글로 작성해 보세요.</span>
                        </div>
                      </li>
                    </Link>
                    <Link to="/advices/write">
                      <li className="flex items-center hover:bg-gray-100 dark:hover:bg-gray-700 py-4 px-6">
                        <img src={icons.PostMenuKnowhow} className="w-11 h-11 mr-5" alt="노하우" />
                        <div>
                          <span className="block font-medium">노하우 글쓰기</span>
                          <span className="text-sm text-gray-500 dark:text-gray-400">다양한 인테리어 노하우를 공유해 주세요.</span>
                        </div>
                      </li>
                    </Link>
                    <Link to="/production_reviews/write">
                      <li className="flex items-center hover:bg-gray-100 dark:hover:bg-gray-700 py-4 px-6">
                        <img src={icons.PostMenuReview} className="w-11 h-11 mr-5" alt="상품 리뷰" />
                        <div>
                          <span className="block font-medium">상품 리뷰 쓰기</span>
                          <span className="text-sm text-gray-500 dark:text-gray-400">상품 리뷰를 작성하고 포인트도 받아 보세요.</span>
                        </div>
                      </li>
                    </Link>
                  </ul>
                </div>
              )}
            </div>
            <div className="flex flex-col justify-center border-2 rounded-[50%]">
              <input type="checkbox" id="light-switch" name="light-switch" className="light-switch sr-only" checked={darkMode} onChange={toggleDarkMode} />
              <label className="relative cursor-pointer p-2" htmlFor="light-switch">
                <LightModeIcon className={`${darkMode ? "hidden" : "block"}`} />
                <DarkModeIcon className={`${darkMode ? "block" : "hidden"}`} />
                <span className="sr-only">Switch to light / dark version</span>
              </label>
            </div>
          </div>

          <div id="mega-menu-icons" className="items-center justify-between hidden w-full md:flex md:w-auto md:order-1">
            <div className="mr-[100px]">
              <ul className="flex flex-col mt-4 font-medium md:flex-row md:mt-0 md:space-x-8 rtl:space-x-reverse">
                <li>
                  <button onClick={(e) => { toggleCommunity(); handleDropdownClick(e); }} className="flex items-center justify-between w-full py-2 px-3 font-medium text-gray-900 border-b border-gray-100 md:w-auto hover:bg-gray-50 dark:hover:bg-gray-700 dark:hover:text-blue-500 md:hover:bg-transparent md:border-0 md:hover:text-blue-600 md:p-0 dark:text-custom-dark-text md:dark:hover:bg-transparent dark:border-gray-700">
                    커뮤니티
                    <FaAngleDown />
                  </button>
                  {isCommunityOpen && (
                    <div className="absolute z-10 w-auto grid-cols-2 text-sm bg-white border border-gray-100 rounded-lg shadow-md dark:bg-gray-700 dark:border-gray-700 md:grid-cols-3">
                      <div className="p-4 pb-0 text-gray-900 dark:text-white md:pb-4 ">
                        <ul className="space-y-4" aria-labelledby="mega-menu-icons-dropdown-button">
                          <li>
                            <Link to="/" className="flex items-center text-gray-500 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-500 group gap-2">
                              <span className="sr-only">홈</span>
                              <FaUserFriends />
                              홈
                            </Link>
                          </li>
                          <li>
                            <a href="#" className="flex items-center text-gray-500 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-500 group gap-2">
                              <span className="sr-only">추천</span>
                              <FaStar />
                              추천
                            </a>
                          </li>
                          <li>
                            <a href="#" className="flex items-center text-gray-500 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-500 group gap-2">
                              <span className="sr-only">#채널</span>
                              <FaSlackHash />
                              #채널
                            </a>
                          </li>
                          <li>
                            <a href="#" className="flex items-center text-gray-500 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-500 group gap-2">
                              <span className="sr-only">집들이</span>
                              <FaHouseUser />
                              집들이
                            </a>
                          </li>
                        </ul>
                      </div>
                    </div>
                  )}
                </li>

                <li>
                  <button onClick={(e) => { toggleShopping(); handleDropdownClick(e); }} className="flex items-center justify-between w-full py-2 px-3 font-medium text-gray-900 border-b border-gray-100 md:w-auto hover:bg-gray-50 dark:hover:bg-gray-700 dark:hover:text-blue-500 md:hover:bg-transparent md:border-0 md:hover:text-blue-600 md:p-0 dark:text-custom-dark-text md:dark:hover:bg-transparent dark:border-gray-700">
                    쇼핑
                    <FaAngleDown />
                  </button>
                  {isShoppingOpen && (
                    <div className="absolute z-10 w-auto grid-cols-2 text-sm bg-white border border-gray-100 rounded-lg shadow-md dark:bg-gray-700 dark:border-gray-700 md:grid-cols-3">
                      <div className="p-4 pb-0 text-gray-900 dark:text-white md:pb-4">
                        <ul className="space-y-4" aria-labelledby="mega-menu-icons-dropdown-button">
                          <li>
                            <a href="#" className="flex items-center text-gray-500 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-500 group gap-2">
                              <span className="sr-only">쇼핑홈</span>
                              <FaShopify />
                              쇼핑홈
                            </a>
                          </li>
                          <li>
                            <a href="#" className="flex items-center text-gray-500 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-500 group gap-2">
                              <span className="sr-only">카테고리</span>
                              <FaListUl />
                              카테고리
                            </a>
                          </li>
                          <li>
                            <a href="#" className="flex items-center text-gray-500 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-500 group gap-2">
                              <span className="sr-only">베스트</span>
                              <FaThumbsUp />
                              베스트
                            </a>
                          </li>
                          <li>
                            <a href="#" className="flex items-center text-gray-500 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-500 group gap-2">
                              <span className="sr-only">오늘의딜</span>
                              <FaFireAlt />
                              오늘의딜
                            </a>
                          </li>
                        </ul>
                      </div>
                    </div>
                  )}
                </li>
              </ul>
            </div>

            <form className="flex items-center max-w-sm mx-auto mt-4 md:mt-0 ml-6">
              <div className="relative w-[220px]">
                <input type="text" className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="통합검색" required />
              </div>
              <button type="submit" className="p-3 ms-2 text-sm font-medium text-white bg-blue-700 rounded-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                <FaSearch />
                <span className="sr-only">Search</span>
              </button>
            </form>
          </div>
        </div>
      </nav>
    </header>
  );
};

export default Header;
