import { create } from "zustand";
import { DropdownState } from "../types/indext";

const useDropdownStore = create<DropdownState>((set) => ({
  isCommunityOpen: false,
  isShoppingOpen: false,
  isPostOpen: false,

  toggleCommunity: () =>
    set((state) => ({
      isCommunityOpen: !state.isCommunityOpen,
      isShoppingOpen: false,
      isPostOpen: false,
    })),

  toggleShopping: () =>
    set((state) => ({
      isShoppingOpen: !state.isShoppingOpen,
      isCommunityOpen: false,
      isPostOpen: false,
    })),

  togglePost: () =>
    set((state) => ({
      isPostOpen: !state.isPostOpen,
      isShoppingOpen: false,
      isCommunityOpen: false,
    })),

  closeAll: () => set({ isCommunityOpen: false, isShoppingOpen: false, isPostOpen: false}),
}));

export default useDropdownStore;
