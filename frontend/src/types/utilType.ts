export interface DropdownState {
  isCommunityOpen: boolean;
  isShoppingOpen: boolean;
  isPostOpen: boolean;

  toggleCommunity: () => void;
  toggleShopping: () => void;
  togglePost: () => void;
  closeAll: () => void;
}

export interface DarkModeState {
  darkMode: boolean;
  setDarkMode: (value: boolean) => void;

  toggleDarkMode: () => void;
}

export interface SvgState {
  className?: string;
}

