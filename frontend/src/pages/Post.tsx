import React from 'react';
import { useParams } from 'react-router-dom';
import PostHeader from '../components/common/PostHeader';
import MediaPost from '../components/post/MediaPost';
import ProjectPost from '../components/post/ProjectPost';
import AdvicePost from '../components/post/AdvicePost';
import ReviewPost from '../components/post/ReviewPost';

const Post: React.FC = () => {
  const { category, mediaType } = useParams<{ category: string, mediaType?: string }>();

  let component;
  let headerType;

  if (category === 'contents' && (mediaType === 'media=photo' || mediaType === 'media=video')) {
    component = <MediaPost mediaType={mediaType.split('=')[1]} />;
    headerType = 'media';
  } else if (category === 'projects') {
    component = <ProjectPost />;
    headerType = 'project';
  } else if (category === 'advices') {
    component = <AdvicePost />;
    headerType = 'advice';
  } else if (category === 'production_reviews') {
    component = <ReviewPost />;
    headerType = 'review';
  } else {
    component = <div>Invalid path</div>;
    headerType = null;
  }

  return (
    <div>
      {headerType && <PostHeader type={headerType} />}
      <div className="flex flex-col items-center justify-between w-full bg-custom-light-bg dark:bg-custom-dark-bg">
        <div className="max-w-screen-xl w-full p-4">
          {component}
        </div>
      </div>
    </div>
  );
};

export default Post;
