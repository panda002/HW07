package com.ownproj.homework07;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    Button btn_signup;

    private SignupFragmentInterface listner;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listner = (SignupFragmentInterface) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listner = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_signup = getActivity().findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(view -> listner.gotoProfileBuilder());
    }


    public interface SignupFragmentInterface {
        void gotoProfileBuilder();
    }
}
