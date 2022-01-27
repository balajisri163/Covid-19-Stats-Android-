package com.dewa.covid19.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.dewa.covid19.R;
import com.dewa.covid19.model.Country;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.AccountsViewHolder>{

    Context mctx;
    private List<Country> countries;

    public CountryAdapter(Context mctx, List<Country> countries) {
        this.mctx = mctx;
        this.countries = countries;
    }

    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate((R.layout.item_country), parent, false);
        return new CountryAdapter.AccountsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.AccountsViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.country.setText(country.getCountry());

        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        holder.confirmed.setText(myFormat.format(Double.parseDouble(country.getTotalConfirmed())));
        holder.death.setText(myFormat.format(Double.parseDouble(country.getTotalDeaths())));
        holder.stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mctx,country.getSlug(),Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putString("slug",country.getSlug());

                NavController navController = Navigation.findNavController((Activity) mctx, R.id.nav_host_fragment);
                navController.navigate(R.id.action_navigation_home_to_navigation_countrystats,bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public void notifyData(List<Country> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }


    public class AccountsViewHolder extends RecyclerView.ViewHolder {

        TextView country, confirmed, death, stats;

        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.country_name);
            confirmed = itemView.findViewById(R.id.confirmed_case);
            death = itemView.findViewById(R.id.deaths);
            stats = itemView.findViewById(R.id.stats);
        }
    }

    private String formatDate(String date_string) throws ParseException {
        String formatedDate="";
        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse(date_string);
        formatedDate= date.toString();

        return formatedDate;
    }




}
