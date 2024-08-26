import MediaHeader from '../post/header/MediaHeader';
import ProjectHeader from '../post/header/ProjectHeader';
import AdviceHeader from '../post/header/AdviceHeader';
import ReviewHeader from '../post/header/ReviewHeader';

const PostHeader: React.FC<{ type: string }> = ({ type }) => {
  let headerComponent;

  switch (type) {
    case 'media':
      headerComponent = <MediaHeader />;
      break;
    case 'project':
      headerComponent = <ProjectHeader />;
      break;
    case 'advice':
      headerComponent = <AdviceHeader />;
      break;
    case 'review':
      headerComponent = <ReviewHeader />;
      break;
    default:
      headerComponent = null;
  }

  return (
    <header>
      {headerComponent}
    </header>
  );
};

export default PostHeader;
