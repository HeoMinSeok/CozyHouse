import { ToastContainer, toast, ToastOptions } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface ToastProps {
  darkMode: boolean;
}

export const showToast = (message: string, type: 'success' | 'error' | 'warn', darkMode: boolean, options?: ToastOptions) => {
  const config: ToastOptions = {
    position: "bottom-center",
    autoClose: 1500,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: darkMode ? 'light' : 'colored',
    ...options
  };

  if (type === 'success') toast.success(message, config);
  else if (type === 'warn') toast.warn(message, config);
  else toast.error(message, config);
};

const Toast: React.FC<ToastProps> = ({ darkMode }) => {
  return <ToastContainer theme={darkMode ? 'dark' : 'light'} />;
};

export default Toast;
