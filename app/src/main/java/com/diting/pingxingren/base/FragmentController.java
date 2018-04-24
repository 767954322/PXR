package com.diting.pingxingren.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.diting.pingxingren.R;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 10, 11:20.
 * Description: .
 */
public class FragmentController {

    private final FragmentActivity fragmentActivity;


    public FragmentController(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public void add(boolean isShow, String tag, int resourceId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (isFragmentExist(tag)) {
            fragment = getFragment(tag);
        } else {
            fragmentTransaction.add(resourceId, fragment, tag);
        }
        if (!isShow) {
            fragmentTransaction.hide(fragment);
        } else {
            fragment.setUserVisibleHint(true);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeFragment(String tag, int resourceId, Fragment targetFragment) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.animator.fragment_left_enter, R.animator.fragment_left_exit
//                , R.animator.fragment_right_enter, R.animator.fragment_right_exit);

//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        List<Fragment> fragments = fragmentActivity.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (!tag.equals(fragment.getTag())) {
                    fragmentTransaction.hide(fragment);
                } else {
                    targetFragment = fragment;
                }
            }
        }
        if (!isFragmentExist(tag)) {
            fragmentTransaction.add(resourceId, targetFragment, tag);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        targetFragment.setUserVisibleHint(true);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeFragment(String tag) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.setCustomAnimations(R.animator.fragment_left_enter, R.animator.fragment_left_exit
//                , R.animator.fragment_right_enter, R.animator.fragment_right_exit);
        List<Fragment> fragments = fragmentActivity.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (!tag.equals(fragment.getTag())) {
                    fragmentTransaction.hide(fragment);
                } else {
                    fragment.setUserVisibleHint(true);
                    fragmentTransaction.show(fragment);
                }
            }
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeFragment(String tag, int resourceId, Fragment targetFragment, int HOMEPUSH) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (HOMEPUSH == 0) {
            fragmentTransaction.setCustomAnimations(R.anim.push_right_in,
                    R.anim.push_right_out, R.anim.push_right_in,
                    R.anim.push_right_out);

        } else {
            fragmentTransaction.setCustomAnimations(R.anim.push_left_in,
                    R.anim.push_left_out, R.anim.push_left_in,
                    R.anim.push_left_out);

        }
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        List<Fragment> fragments = fragmentActivity.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (!tag.equals(fragment.getTag())) {
                    fragmentTransaction.hide(fragment);
                } else {
                    targetFragment = fragment;
                }
            }
        }
        if (!isFragmentExist(tag)) {
            fragmentTransaction.add(resourceId, targetFragment, tag);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        targetFragment.setUserVisibleHint(true);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeFragment(String tag, int HOMEPUSH) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.setCustomAnimations(R.animator.fragment_left_enter, R.animator.fragment_left_exit
//                , R.animator.fragment_right_enter, R.animator.fragment_right_exit);
        if (HOMEPUSH == 0) {
            fragmentTransaction.setCustomAnimations(R.anim.push_right_in,
                    R.anim.push_right_out, R.anim.push_right_in,
                    R.anim.push_right_out);

        } else {
            fragmentTransaction.setCustomAnimations(R.anim.push_left_in,
                    R.anim.push_left_out, R.anim.push_left_in,
                    R.anim.push_left_out);

        }
        List<Fragment> fragments = fragmentActivity.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (!tag.equals(fragment.getTag())) {
                    fragmentTransaction.hide(fragment);
                } else {
                    fragment.setUserVisibleHint(true);
                    fragmentTransaction.show(fragment);
                }
            }
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

//    /**
//     *
//     * @param tag 首页fragment内嵌了几个fragment所以fragment切换时
//     */
//    public void changeMainFragments(String tag) {
////        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
//        List<Fragment> fragments = fragmentActivity.getSupportFragmentManager().getFragments();
//        if (fragments != null) {
//            for (int i = 0; i < fragments.size(); i++) {
//                Fragment fragment = fragments.get(i);
//                if (!tag.equals(fragment.getTag())) {
//                    if (fragment.getTag().equals(MainUI.TAG_INDEX)
//                            || fragment.getTag().equals(MainUI.TAG_FEED)
//                            || fragment.getTag().equals(MainUI.TAG_MINE)
//                            || fragment.getTag().equals(MainUI.TAG_MSG)) {
//                        fragmentTransaction.hide(fragment);
//                    }
//                } else {
//                    fragment.setUserVisibleHint(true);
//                    fragmentTransaction.show(fragment);
//                }
//            }
//        }
//
//        fragmentTransaction.commitAllowingStateLoss();
//    }

    public void hideFragment(String tag) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(getFragment(tag));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void hideFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    public void showFragment(String tag) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(getFragment(tag));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void removeFragment(String tag) {
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(getFragment(tag));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public boolean isFragmentExist(String tag) {
        boolean isExist = false;
        if (getFragment(tag) != null) {
            isExist = true;
        }
        return isExist;
    }

    public Fragment getFragment(String tag) {
        return fragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
    }
}
