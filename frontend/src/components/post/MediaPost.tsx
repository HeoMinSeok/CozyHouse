import React from 'react';

const MediaPost: React.FC<{ mediaType: string }> = ({ mediaType }) => {
  return (
    <div className="flex flex-col items-center justify-center py-10 bg-custom-light-bg dark:bg-custom-dark-bg">
      <div className="w-full max-w-screen-xl">
        <div className="border-b border-gray-200 dark:border-gray-700 mb-4">
          <div className="flex justify-center space-x-4">
            <button
              className={`text-lg font-medium px-4 py-2 ${mediaType === 'photo' ? 'border-b-2 border-blue-500 text-blue-500' : 'text-gray-500'}`}
            >
              사진
            </button>
            <button
              className={`text-lg font-medium px-4 py-2 ${mediaType === 'video' ? 'border-b-2 border-blue-500 text-blue-500' : 'text-gray-500'}`}
            >
              동영상
            </button>
          </div>
        </div>
        <div className="flex flex-col md:flex-row justify-between">
          <div className="md:w-1/2 p-4">
            {mediaType === 'photo' ? (
              <div className="border-2 border-dashed border-gray-400 rounded-lg p-6 text-center">
                <p>사진을 끌어다 놓으세요</p>
                <p>10장까지 올릴 수 있어요.</p>
                <button className="mt-4 px-4 py-2 bg-blue-500 text-white rounded">PC에서 불러오기</button>
              </div>
            ) : (
              <div className="border-2 border-dashed border-gray-400 rounded-lg p-6 text-center">
                <p>동영상을 끌어다 놓으세요</p>
                <p>5GB 미만, 3~60초의 세로 영상이 좋아요.</p>
                <button className="mt-4 px-4 py-2 bg-blue-500 text-white rounded">PC에서 불러오기</button>
              </div>
            )}
          </div>
          <div className="md:w-1/2 p-4">
            <textarea
              className="w-full border rounded-lg p-2 mb-4"
              rows={6}
              placeholder={mediaType === 'photo' ? "어떤 사진인지 짧은 소개를 작성해보세요. 다양한 해시태그도 추가할 수 있어요." : "어떤 동영상인지 짧은 소개를 작성해보세요. 다양한 해시태그도 추가할 수 있어요."}
            ></textarea>
            <select className="w-full border rounded-lg p-2">
              <option>공간 정보 추가</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MediaPost;
