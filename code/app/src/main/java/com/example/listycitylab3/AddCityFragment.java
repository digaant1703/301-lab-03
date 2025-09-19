package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City originalCity, City updatedCity);
    }

    private AddCityDialogListener listener;
    private City originalCity;
    private boolean isEditMode;

    // Empty constructor for new city additions
    public AddCityFragment() {
        this.isEditMode = false;
    }

    // Static factory method for editing existing cities (Recommended approach)
    public static AddCityFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if we're editing an existing city
        if (getArguments() != null) {
            originalCity = (City) getArguments().getSerializable("city");
            isEditMode = true;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Pre-fill fields if editing existing city
        if (isEditMode && originalCity != null) {
            editCityName.setText(originalCity.getName());
            editProvinceName.setText(originalCity.getProvince());
            builder.setTitle("Edit city");
        } else {
            builder.setTitle("Add a city");
        }

        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEditMode ? "Save" : "Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (isEditMode) {
                        City updatedCity = new City(cityName, provinceName);
                        listener.editCity(originalCity, updatedCity);
                    } else {
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}