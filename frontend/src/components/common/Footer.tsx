import icons from "../../assets/icons";
import { DiscordIcon, GithubIcon } from "../../assets/svg/SvgIcons";

const Footer = () => {
  return (
    <footer className="bg-custom-light-bg dark:bg-custom-dark-bg">
      <div className="mx-auto w-full max-w-screen-xl p-4 py-6 lg:py-8">
        <div className="md:flex md:justify-between">
          <div className="mb-6 md:mb-0">
            <a href="https://flowbite.com/" className="flex items-center">
              <img src={icons.MainHome} className="h-8 me-3" alt="FlowBite Logo" />
              <span className="self-center text-2xl font-semibold whitespace-nowrap text-custom-light-text dark:text-custom-dark-text">CozyHouse</span>
            </a>
          </div>
          <div className="grid grid-cols-2 gap-8 sm:gap-6 sm:grid-cols-3">
            <div>
              <h2 className="mb-6 text-sm font-semibold text-custom-light-text uppercase dark:text-custom-dark-text">Resources</h2>
              <ul className="text-custom-light-text dark:text-custom-dark-text font-medium">
                <li className="mb-4">
                  <a href="https://flowbite.com/" className="hover:underline">Flowbite</a>
                </li>
                <li>
                  <a href="https://tailwindcss.com/" className="hover:underline">Tailwind CSS</a>
                </li>
              </ul>
            </div>
            <div>
              <h2 className="mb-6 text-sm font-semibold text-custom-light-text uppercase dark:text-custom-dark-text">Follow us</h2>
              <ul className="text-custom-light-text dark:text-custom-dark-text font-medium">
                <li className="mb-4">
                  <a href="#" className="hover:underline">Github</a>
                </li>
                <li>
                  <a href="#" className="hover:underline">Discord</a>
                </li>
              </ul>
            </div>
            <div>
              <h2 className="mb-6 text-sm font-semibold text-custom-light-text uppercase dark:text-custom-dark-text">Legal</h2>
              <ul className="text-custom-light-text dark:text-custom-dark-text font-medium">
                <li className="mb-4">
                  <a href="#" className="hover:underline">Privacy Policy</a>
                </li>
                <li>
                  <a href="#" className="hover:underline">Terms &amp; Conditions</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <hr className="my-6 border-custom-light-bg sm:mx-auto dark:border-custom-dark-bg lg:my-8" />
        <div className="sm:flex sm:items-center sm:justify-between">
          <span className="text-sm text-custom-light-text sm:text-center dark:text-custom-dark-text">© 2023 <a href="#" className="hover:underline">Flowbite™</a>. All Rights Reserved.
          </span>
          <div className="flex mt-4 sm:justify-center sm:mt-0">
            <a href="#" className="text-custom-light-text hover:text-custom-light-accent dark:text-custom-dark-text dark:hover:text-custom-dark-accent ms-5">
              <DiscordIcon />
              <span className="sr-only">Discord community</span>
            </a>
            <a href="#" className="text-custom-light-text hover:text-custom-light-accent dark:text-custom-dark-text dark:hover:text-custom-dark-accent ms-5">
              <GithubIcon />
              <span className="sr-only">GitHub account</span>
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
