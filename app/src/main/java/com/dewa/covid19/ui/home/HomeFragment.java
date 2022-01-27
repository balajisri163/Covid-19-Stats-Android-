package com.dewa.covid19.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dewa.covid19.R;
import com.dewa.covid19.model.Country;
import com.dewa.covid19.network.RetrofitClient;
import com.dewa.covid19.network.response.SummaryResponse;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    View view;

    ProgressBar progressBar;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    List<Country> countries;
    CountryAdapter countryAdapter;
    EditText search;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar=view.findViewById(R.id.progressBar_cyclic);
        linearLayout=view.findViewById(R.id.rlayout);
        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        countries=new ArrayList<Country>();
        countryAdapter= new CountryAdapter(getContext(),countries);
        recyclerView.setAdapter(countryAdapter);
        search = view.findViewById(R.id.search_bar);
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        callSummary();


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_employee(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                search_employee(s.toString());
            }
        });


        return view;
    }

    private void search_employee(String text) {
        ArrayList<Country> filteredList = new ArrayList<>();
        for (Country item : countries) {
            if (item.getCountry().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        countryAdapter.notifyData(filteredList);
    }

    private void callSummary() {
        Call<SummaryResponse> call = RetrofitClient.getInstance().getApi().GetSummary();
        call.enqueue(new Callback<SummaryResponse>() {
            @Override
            public void onResponse(Call<SummaryResponse> call, Response<SummaryResponse> response) {
                if (response.code() == 200) {
                    SummaryResponse commonResponse= response.body();
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    List<Country> list= commonResponse.getCountries();
                    countries.clear();
                    countries.addAll(list);
                    countryAdapter.notifyData(countries);
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);


                } else {
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SummaryResponse> call, Throwable t) {
                Log.e("Test",t.getMessage());
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


}