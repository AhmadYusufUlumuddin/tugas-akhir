package com.example.arkacamataadmin.main.profil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.arkacamataadmin.R;
import com.example.arkacamataadmin.config.SharePreference;

import java.util.HashMap;

public class ProfilFragment extends Fragment implements View.OnClickListener {
    SharePreference sharePreference;
    String username;
    EditText et_username;
    Button btn_profil, btn_password;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profil, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharePreference = new SharePreference(getContext());

        et_username = view.findViewById(R.id.et_username);
        btn_profil = view.findViewById(R.id.btn_profil);
        btn_password = view.findViewById(R.id.btn_password);
        btn_profil.setOnClickListener(this);
        btn_password.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        HashMap<String, String> user = sharePreference.getUserDetails();
        username = user.get(sharePreference.KEY_USERNAME);

        et_username.setText(username);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_profil){
            startActivity(new Intent(getContext(),ProfilUbahActivity.class));
        } else if (v == btn_password){
            startActivity(new Intent(getContext(),ProfilPasswordActivity.class));
        }
    }
}