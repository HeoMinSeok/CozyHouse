const exampleContents = [
  { id: 1, imgSrc: '/path/to/image1.jpg', label: '하나둘셋12345' },
  { id: 2, imgSrc: '/path/to/image2.jpg', label: '룸룸' },
  { id: 3, imgSrc: '/path/to/image3.jpg', label: '두부' },
  { id: 4, imgSrc: '/path/to/image4.jpg', label: 'roomyourroom' },
  { id: 5, imgSrc: '/path/to/image5.jpg', label: '거북이와두루미상' },
  { id: 6, imgSrc: '/path/to/image6.jpg', label: '아름다운예' },
  { id: 7, imgSrc: '/path/to/image6.jpg', label: '아름다운예' },
  { id: 8, imgSrc: '/path/to/image6.jpg', label: '아름다운예' },
  { id: 9, imgSrc: '/path/to/image6.jpg', label: '아름다운예' },
  { id: 10, imgSrc: '/path/to/image6.jpg', label: '아름다운예' },
];

const HomeContents: React.FC = () => {
  return (
    <div className="px-4 ">
      <h2 className="text-xl font-bold mb-4 text-gray-700 dark:text-white">이런 사진 찾고 있나요?</h2>
      <p className="text-gray-600 mb-6 dark:text-white">좋아하실 만한 인테리어 콘텐츠를 추천해드려요</p>
      <div className="flex overflow-x-scroll scrollbar-hide space-x-4">
        {exampleContents.map((content) => (
          <div key={content.id} className="min-w-[150px] flex-shrink-0">
            <div className="w-full h-[200px] bg-gray-200 rounded-lg overflow-hidden mb-2">
              <img src={content.imgSrc} alt={content.label} className="w-full h-full object-cover" />
            </div>
            <div className="flex items-center space-x-2">
              <span className="text-sm text-gray-700 dark:text-slate-300">{content.label}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default HomeContents;
