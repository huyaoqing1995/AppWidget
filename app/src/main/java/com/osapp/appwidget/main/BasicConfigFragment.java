package com.osapp.appwidget.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.osapp.appwidget.R;

public abstract class BasicConfigFragment extends Fragment {

    protected View view;

    protected WidgetConfigActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (WidgetConfigActivity) getActivity();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(),container,false);
        View btBack = view.findViewById(R.id.bt_back);
        if(btBack!=null){
            btBack.setOnClickListener(view -> activity.getSupportFragmentManager().popBackStack());
        }
        return view;
    }

    protected abstract int getLayoutId();


    public static void addFragment(AppCompatActivity activity, Fragment fragment){
        if(activity.getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName())==null){
            addFragment(activity,fragment,fragment.getClass().getName());
        }
    }


    private static void addFragment(AppCompatActivity activity, @NonNull Fragment fragment,
                                    @NonNull String tag) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .setCustomAnimations(R.anim.anim_in,R.anim.anim_out,R.anim.anim_in,R.anim.anim_out)
                .add(R.id.main, fragment, tag)
                .commitAllowingStateLoss();

    }


}
