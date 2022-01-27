package com.dewa.covid19.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.dewa.covid19.R;
import com.dewa.covid19.model.Country;
import com.dewa.covid19.network.RetrofitClient;
import com.dewa.covid19.network.response.SummaryResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

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

public class DashboardFragment extends Fragment {
    View view;
    ProgressBar progressBar;
    TextView totalcase,todaycase,totaldeath,todaydeath;
    NestedScrollView nestedScrollView;
    CircularProgressBar circularProgressBar,recoverycircularProgressBar;
    TextView present_percentage,recovery_percentage;
    PieChart pieChart;
    BarChart barchart;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        todaycase = view.findViewById(R.id.todayconfirmed);
        totalcase = view.findViewById(R.id.totalconfirmed);
        totaldeath= view.findViewById(R.id.totaldeath);
        todaydeath = view.findViewById(R.id.todaydeath);
        progressBar = view.findViewById(R.id.progressBar_cyclic);
        nestedScrollView = view.findViewById(R.id.scroll_view);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        present_percentage=view.findViewById(R.id.present_percentage);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        present_percentage=view.findViewById(R.id.present_percentage);
        recoverycircularProgressBar = view.findViewById(R.id.recoverycircularProgressBar);
        recovery_percentage=view.findViewById(R.id.recovery_percentage);
        pieChart = view.findViewById(R.id.pieChart);

        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.GONE);

        callSummary();
        return view;
    }

    private void callSummary() {
        Call<SummaryResponse> call = RetrofitClient.getInstance().getApi().GetSummary();
        call.enqueue(new Callback<SummaryResponse>() {
            @Override
            public void onResponse(Call<SummaryResponse> call, Response<SummaryResponse> response) {
                if (response.code() == 200) {
                    SummaryResponse commonResponse= response.body();

                    NumberFormat myFormat = NumberFormat.getInstance();
                    myFormat.setGroupingUsed(true);


                    totalcase.setText(String.valueOf(myFormat.format(Double.parseDouble(commonResponse.getGlobal().getTotalConfirmed()))));
                    todaycase.setText(String.valueOf(myFormat.format(Double.parseDouble(commonResponse.getGlobal().getNewConfirmed()))));
                    totaldeath.setText(String.valueOf(myFormat.format(Double.parseDouble(commonResponse.getGlobal().getTotalDeaths()))));
                    todaydeath.setText(String.valueOf(myFormat.format(Double.parseDouble(commonResponse.getGlobal().getNewDeaths()))));

                    Double percentage=  ((Double.parseDouble(commonResponse.getGlobal().getTotalDeaths())/Double.parseDouble(commonResponse.getGlobal().getTotalConfirmed())) * 100);
                    NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                    DecimalFormat df = (DecimalFormat) nf;
                    df.applyPattern("##.##");
                    String percent=df.format(percentage);
                    circularProgressBar.setProgress(Float.parseFloat(percent));
                    present_percentage.setText(percent.concat(" %"));

                    Double recovery=(Double.parseDouble(commonResponse.getGlobal().getTotalConfirmed())-(Double.parseDouble(commonResponse.getGlobal().getTotalDeaths())));
                    Double rpercentage=  ((recovery/Double.parseDouble(commonResponse.getGlobal().getTotalConfirmed())) * 100);
                    NumberFormat rnf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                    DecimalFormat rdf = (DecimalFormat) rnf;
                    df.applyPattern("##.##");
                    String rpercent=rdf.format(rpercentage);
                    recoverycircularProgressBar.setProgress(Float.parseFloat(rpercent));
                    recovery_percentage.setText(rpercent.concat(" %"));

                    Integer recovered =Integer.parseInt(commonResponse.getGlobal().getTotalConfirmed())-Integer.parseInt(commonResponse.getGlobal().getTotalDeaths());

                    Integer death = Integer.parseInt(commonResponse.getGlobal().getTotalDeaths());

                    Map<String, Integer> typeAmountMap = new HashMap<>();
                    typeAmountMap.put("Total Recovered Cases",recovered);
                    typeAmountMap.put("Total Death",Integer.parseInt(commonResponse.getGlobal().getTotalDeaths()));


                    createPieChart(pieChart,typeAmountMap);
                    //createBarChart(commonResponse.getCountries());

                    progressBar.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.VISIBLE);
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

    public void createPieChart(PieChart pieChart, Map<String, Integer> typeAmountMap) {


        ArrayList<PieEntry> pieEntries = new ArrayList<>();


        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(getColors());

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white));
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieDataSet.setDrawValues(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelColor(ContextCompat.getColor(getContext(), R.color.white));
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        //Legends Attributes
        Legend legend = pieChart.getLegend();
        legend.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        legend.setTextSize(12);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setXEntrySpace(10);
        pieChart.getLegend().setEnabled(true);
        pieChart.animateXY(1000, 1000);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setClickable(false);
        pieChart.invalidate();
        pieChart.setTouchEnabled(true);
    }


    private ArrayList getColors() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(getContext(), R.color.yellow));
        colors.add(ContextCompat.getColor(getContext(), R.color.pink));
        colors.add(ContextCompat.getColor(getContext(), R.color.green));
        colors.add(ContextCompat.getColor(getContext(), R.color.yellow));
        return colors;
    }




}