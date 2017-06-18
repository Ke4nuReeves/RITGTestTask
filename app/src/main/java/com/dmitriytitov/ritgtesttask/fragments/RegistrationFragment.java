package com.dmitriytitov.ritgtesttask.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dmitriytitov.ritgtesttask.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    /*private EditText nameEditText;
    private EditText emailEditText;*/
    private EditText passEditText;
    /*private EditText repeatPassEditText;*/
    private TextView nameHint;
    private TextView emailHint;
    private TextView passHint;
    private TextView repeatPassHint;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_registration, container, false);

        EditText nameEditText = (EditText) rootView.findViewById(R.id.name_field);
        EditText emailEditText = (EditText) rootView.findViewById(R.id.email_field);
        passEditText = (EditText) rootView.findViewById(R.id.password_field);
        EditText repeatPassEditText = (EditText) rootView.findViewById(R.id.repeat_pass_field);

        nameHint = (TextView) rootView.findViewById(R.id.name_hint);
        emailHint = (TextView) rootView.findViewById(R.id.email_hint);
        passHint = (TextView) rootView.findViewById(R.id.password_hint);
        repeatPassHint = (TextView) rootView.findViewById(R.id.repeat_pass_hint);

        nameEditText.addTextChangedListener(nameWatcher);
        emailEditText.addTextChangedListener(emailWatcher);
        passEditText.addTextChangedListener(passWatcher);
        repeatPassEditText.addTextChangedListener(repeatPassWatcher);

        return rootView;
    }

    private final TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                nameHint.setVisibility(View.VISIBLE);
                nameHint.setText(getResources().getString(R.string.enter_name_war));
            } else if (s.length() < 4) {
                nameHint.setVisibility(View.VISIBLE);
                nameHint.setText(getResources().getString(R.string.short_name_war));
            } else {
              nameHint.setVisibility(View.INVISIBLE);
            }
        }
    };

    private final TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                emailHint.setVisibility(View.VISIBLE);
                emailHint.setText(getResources().getString(R.string.enter_email_war));
            } else if (s.length() < 4) {
                //TODO regular
                emailHint.setVisibility(View.VISIBLE);
                emailHint.setText(getResources().getString(R.string.incorrect_email_war));
            } else {
                emailHint.setVisibility(View.INVISIBLE);
            }
        }
    };

    private final TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                passHint.setVisibility(View.VISIBLE);
                passHint.setText(getResources().getString(R.string.enter_pass_war));
            } else if (s.length() < 6) {
                passHint.setVisibility(View.VISIBLE);
                passHint.setText(getResources().getString(R.string.short_pass_war));
            } else {
                passHint.setVisibility(View.INVISIBLE);
            }
        }
    };

    private final TextWatcher repeatPassWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                repeatPassHint.setVisibility(View.VISIBLE);
                repeatPassHint.setText(getResources().getString(R.string.repeat_pass_war));
            } else if (!s.equals(passEditText.getText())) {
                repeatPassHint.setVisibility(View.VISIBLE);
                repeatPassHint.setText(getResources().getString(R.string.different_pass_war));
            } else {
                repeatPassHint.setVisibility(View.INVISIBLE);
            }
        }
    };

}
