package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whatsappclone.chatsFragment;
import com.example.whatsappclone.contactsFragment;
import com.example.whatsappclone.groupsFragment;

public class TabsAccessorAdaptor extends FragmentPagerAdapter
{

    public TabsAccessorAdaptor(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                chatsFragment chatsfragment=new chatsFragment();
                return chatsfragment;
            case 1:
                groupsFragment groupfragment=new groupsFragment();
                return groupfragment;
            case 2:
                contactsFragment contactsfragment=new contactsFragment();
                return contactsfragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
               return "Chats";
            case 1:
                return "Groups";
            case 2:
                return "Contacts";
            default:
                return null;
        }
    }
}
