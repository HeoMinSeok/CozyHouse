import { Link } from 'react-router-dom';

import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay, Pagination, Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/navigation';

import icons from '../../assets/icons';

/* Mock */
const slides = [
  { id: 1, imgSrc: icons.MockBanner, alt: 'Slide 1' },
  { id: 2, imgSrc: icons.MockBanner, alt: 'Slide 2' },
  { id: 3, imgSrc: icons.MockBanner, alt: 'Slide 3' },
];

const HomeBanner: React.FC = () => {
  return (
    <div className="flex w-full h-[150px] md:h-[500px] gap-2 bg-custom-light-bg dark:bg-custom-dark-bg rounded-lg shadow-md overflow-hidden mt-4">
      <div className="flex-[6] h-full hidden md:block">
        <img src={icons.MockBanner} alt="Banner" className="w-full h-full object-cover rounded-lg" />
      </div>
      <div className="flex-[2] h-full md:basis-[300px] md:max-w-[300px] w-full relative">
        <Swiper
          spaceBetween={10}
          centeredSlides={true}
          rewind={true}
          autoplay={{
            delay: 2500,
            disableOnInteraction: false,
          }}
          pagination={{
            el: '.custom-swiper-pagination',
            type: 'fraction',
            clickable: true,
          }}
          navigation={true}
          modules={[Autoplay, Pagination, Navigation]}
          className="h-full swiper-container"
        >
          {slides.map(slide => (
            <SwiperSlide key={slide.id}>
              <img src={slide.imgSrc} alt={slide.alt} className="w-full object-cover rounded-lg h-full" />
            </SwiperSlide>
          ))}
        </Swiper>
        <Link to="/event" className="custom-swiper-pagination swiper-pagination" />
      </div>
    </div>
  );
};

export default HomeBanner;
