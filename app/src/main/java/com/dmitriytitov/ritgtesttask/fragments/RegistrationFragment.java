package com.dmitriytitov.ritgtesttask.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dmitriytitov.ritgtesttask.Constants;
import com.dmitriytitov.ritgtesttask.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private static final int NAME_MIN_LENGTH = 4;
    private static final int PASS_MIN_LENGTH = 6;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passEditText;
    private EditText repeatPassEditText;
    private TextView nameHint;
    private TextView emailHint;
    private TextView passHint;
    private TextView repeatPassHint;

    private boolean nameIsCorrect = false;
    private boolean emailIsCorrect = false;
    private boolean passIsCorrect = false;
    private boolean repeatPassIsCorrect = false;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_registration, container, false);

        nameEditText = (EditText) rootView.findViewById(R.id.name_field);
        emailEditText = (EditText) rootView.findViewById(R.id.email_field);
        passEditText = (EditText) rootView.findViewById(R.id.password_field);
        repeatPassEditText = (EditText) rootView.findViewById(R.id.repeat_pass_field);

        nameHint = (TextView) rootView.findViewById(R.id.name_hint);
        emailHint = (TextView) rootView.findViewById(R.id.email_hint);
        passHint = (TextView) rootView.findViewById(R.id.password_hint);
        repeatPassHint = (TextView) rootView.findViewById(R.id.repeat_pass_hint);

        Button submitButton = (Button) rootView.findViewById(R.id.submit_button) ;

        nameEditText.addTextChangedListener(nameWatcher);
        emailEditText.addTextChangedListener(emailWatcher);
        passEditText.addTextChangedListener(passWatcher);
        repeatPassEditText.addTextChangedListener(repeatPassWatcher);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFieldsIsCorrect()) {
                    new UserDataPostRequest().execute();
                } else {
                    Toast toast = Toast.makeText(getContext(), "Filled is invalid", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        return rootView;
    }


    private class UserDataPostRequest extends AsyncTask<Void,Void,Boolean> {
        UserData userData;

        @Override
        protected void onPreExecute() {
            userData = new UserData(nameEditText.getText().toString(),
                    emailEditText.getText().toString(), passEditText.getText().toString());
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ResponseEntity<String> response;
            try {
                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity entity = new HttpEntity(userData, headers);

                 response = restTemplate.exchange(Constants.URL.POST_USER_DATA, HttpMethod.POST, entity
                        , String.class);
            } catch (RestClientException ex) {
                return false;
            }
            return response.getStatusCode() == HttpStatus.OK;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getContext(), "Data sent", Toast.LENGTH_SHORT);
                toast.show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(RegistrationFragment.this).attach(RegistrationFragment.this).commit();
            }
        }
    }


    private boolean allFieldsIsCorrect() {
        return nameIsCorrect & emailIsCorrect & passIsCorrect & repeatPassIsCorrect;
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
            if (nameHint.getVisibility() == View.INVISIBLE) {
                nameHint.setVisibility(View.VISIBLE);
            }
            if (s.length() >= NAME_MIN_LENGTH) {
                nameIsCorrect = true;
                nameHint.setTextColor(ContextCompat.getColor(getContext(), R.color.correct_input));
                nameHint.setText(getResources().getString(R.string.correct_input));
            } else {
                nameIsCorrect = false;
                nameHint.setTextColor(ContextCompat.getColor(getContext(), R.color.incorrect_input));
                if (s.length() == 0) {
                    nameHint.setText(getResources().getString(R.string.enter_name_war));
                } else if (s.length() < NAME_MIN_LENGTH) {
                    nameHint.setText(getResources().getString(R.string.short_name_war));
                }
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
            if (emailHint.getVisibility() == View.INVISIBLE) {
                emailHint.setVisibility(View.VISIBLE);
            }
            if (emailIsValid(s.toString())) {
                emailIsCorrect = true;
                emailHint.setTextColor(ContextCompat.getColor(getContext(), R.color.correct_input));
                emailHint.setText(getResources().getString(R.string.correct_input));
            } else {
                emailIsCorrect = false;
                emailHint.setTextColor(ContextCompat.getColor(getContext(), R.color.incorrect_input));
                if (s.length() == 0) {
                    emailHint.setText(getResources().getString(R.string.enter_email_war));
                } else {
                    emailHint.setText(getResources().getString(R.string.incorrect_email_war));
                }
            }
        }
    };


    private boolean emailIsValid(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private final TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (passHint.getVisibility() == View.INVISIBLE) {
                passHint.setVisibility(View.VISIBLE);
            }
            if (s.length() >= PASS_MIN_LENGTH) {
                passIsCorrect = true;
                passHint.setTextColor(ContextCompat.getColor(getContext(), R.color.correct_input));
                passHint.setText(getResources().getString(R.string.correct_input));
            } else {
                passIsCorrect = false;
                passHint.setTextColor(ContextCompat.getColor(getContext(), R.color.incorrect_input));
                if (s.length() == 0) {
                    passHint.setText(getResources().getString(R.string.enter_pass_war));
                } else if (s.length() < PASS_MIN_LENGTH) {
                    passHint.setText(getResources().getString(R.string.short_pass_war));
                }
            }
            if (repeatPassEditText.getText().length() >= PASS_MIN_LENGTH && !s.toString().equals(repeatPassEditText.getText().toString())) {
                repeatPassIsCorrect = false;
                repeatPassHint.setTextColor(ContextCompat.getColor(getContext(), R.color.incorrect_input));
                repeatPassHint.setText(getResources().getString(R.string.different_pass_war));
            } else if (repeatPassEditText.getText().length() >= PASS_MIN_LENGTH) {
                repeatPassIsCorrect = true;
                repeatPassHint.setTextColor(ContextCompat.getColor(getContext(), R.color.correct_input));
                repeatPassHint.setText(getResources().getString(R.string.correct_input));
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
            if (repeatPassHint.getVisibility() == View.INVISIBLE) {
                repeatPassHint.setVisibility(View.VISIBLE);
            }
            if (s.length() >= PASS_MIN_LENGTH && s.toString().equals(passEditText.getText().toString())) {
                repeatPassIsCorrect = true;
                repeatPassHint.setTextColor(ContextCompat.getColor(getContext(), R.color.correct_input));
                repeatPassHint.setText(getResources().getString(R.string.correct_input));
            } else {
                repeatPassIsCorrect = false;
                repeatPassHint.setTextColor(ContextCompat.getColor(getContext(), R.color.incorrect_input));
                if (s.length() == 0) {
                    repeatPassHint.setText(getResources().getString(R.string.repeat_pass_war));
                } else if (s.length() < PASS_MIN_LENGTH) {
                    repeatPassHint.setText(getResources().getString(R.string.short_pass_war));
                } else if (!s.toString().equals(passEditText.getText().toString())) {
                    repeatPassHint.setText(getResources().getString(R.string.different_pass_war));
                }
            }
        }
    };
}
