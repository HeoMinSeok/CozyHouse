import { createBrowserRouter } from 'react-router-dom';
import App from '../App';
import Home from '../pages/Home';
import Login from '../pages/Login';
import SignUp from '../pages/SignUp';
import Shopping from '../pages/Shopping';
import Post from '../pages/Post';

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      { path: '/', element: <Home /> },
      { path: '/shopping', element: <Shopping /> },
      { path: '/login', element: <Login /> },
      { path: '/signup', element: <SignUp /> },
      { path: '/:category/write/:mediaType', element: <Post /> },
      { path: '/:category/write', element: <Post /> },
    ],
  },
]);

export default router;
