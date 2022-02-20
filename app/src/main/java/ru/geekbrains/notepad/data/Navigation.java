package ru.geekbrains.notepad.data;

import ru.geekbrains.notepad.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {
    private  final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public  void  addFragment(Fragment fragment, boolean useBackStack){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentListFragment, fragment);
        if (useBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

    }

    public  void  addFragment(Fragment fragment, boolean useBackStack, boolean isLandscape){
        int viewId = isLandscape ? R.id.descriptionNoteFrgLand : R.id.contentListFragment;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewId, fragment);
        if (useBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

    }
}
