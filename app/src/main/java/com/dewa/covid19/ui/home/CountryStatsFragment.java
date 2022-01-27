package com.dewa.covid19.ui.home;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dewa.covid19.R;
import com.dewa.covid19.model.Country;
import com.dewa.covid19.model.CountryLiveData;
import com.dewa.covid19.model.CountryStats;
import com.dewa.covid19.network.RetrofitClient;
import com.dewa.covid19.network.response.SummaryResponse;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryStatsFragment extends Fragment {
    View view;
    String slug,url,url1,url2,countrystaturl;
    TextView totalcase,totaldeath;
    TextView name,continent,population,lifeexpect;

    LineChart lineChart,lineChart2,lineChart3;
    LineData lineData,lineData2,lineData3;
    List<Entry> lineEntries,lineEntries2,lineEntries3;
    LineDataSet lineDataSet,lineDataSet2,lineDataSet3;

    String[] status= new String[]{"confirmed","recovered","deaths"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_country_stats, container, false);
        lineChart = view.findViewById(R.id.line);
        lineChart2 = view.findViewById(R.id.line2);
        lineChart3 = view.findViewById(R.id.line3);

        totalcase = view.findViewById(R.id.totalconfirmed);
        totaldeath= view.findViewById(R.id.totaldeath);
        name = view.findViewById(R.id.name);
        continent = view.findViewById(R.id.continent);
        population = view.findViewById(R.id.totalpopulation);
        lifeexpect = view.findViewById(R.id.lifeexpectancy);

        if(getArguments()!=null){
            slug= getArguments().getString("slug");
        }


        url= "/dayone/country/"+slug+"/status/"+status[0]+"/live";
        callCountryLiveData();

        url1= "/dayone/country/"+slug+"/status/"+status[1]+"/live";
        callCountryLiveData2();

        url2= "/dayone/country/"+slug+"/status/"+status[2]+"/live";
        callCountryLiveData3();

        countrystaturl = "/premium/country/data/"+slug;
        callCountryStats();

        return view;
    }


    private void callCountryStats() {
        Call<List<CountryStats>> call = RetrofitClient.getInstance().getApi().getCountryStats(countrystaturl);
        call.enqueue(new Callback<List<CountryStats>>() {
            @Override
            public void onResponse(Call<List<CountryStats>> call, Response<List<CountryStats>> response) {
                if (response.code() == 200) {
                    List<CountryStats> commonResponse= response.body();
                    name.setText(commonResponse.get(0).getCountry());
                    continent.setText(commonResponse.get(0).getContinent());
                    lifeexpect.setText(commonResponse.get(0).getLifeExpectancy());

                    NumberFormat myFormat = NumberFormat.getInstance();
                    myFormat.setGroupingUsed(true);
                    population.setText(myFormat.format(Double.parseDouble(commonResponse.get(0).getPopulation())));

                } else {
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<CountryStats>> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callCountryLiveData() {
        Call<List<CountryLiveData>> call = RetrofitClient.getInstance().getApi().getCountryLiveData(url);
        call.enqueue(new Callback<List<CountryLiveData>>() {
            @Override
            public void onResponse(Call<List<CountryLiveData>> call, Response<List<CountryLiveData>> response) {
                if (response.code() == 200) {
                    List<CountryLiveData> commonResponse= response.body();
                    try {
                        NumberFormat myFormat = NumberFormat.getInstance();
                        myFormat.setGroupingUsed(true);
                        Integer n = commonResponse.size();
                        totalcase.setText(String.valueOf(myFormat.format(Double.parseDouble(commonResponse.get(n-1).getCases()))));

                        List<CountryLiveData> last30= commonResponse.subList(commonResponse.size()-10,commonResponse.size());
                            getEntries(last30);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<CountryLiveData>> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callCountryLiveData2() {
        Call<List<CountryLiveData>> call = RetrofitClient.getInstance().getApi().getCountryLiveData(url1);
        call.enqueue(new Callback<List<CountryLiveData>>() {
            @Override
            public void onResponse(Call<List<CountryLiveData>> call, Response<List<CountryLiveData>> response) {
                if (response.code() == 200) {
                    List<CountryLiveData> commonResponse= response.body();
                    try {
                        List<CountryLiveData> last30= commonResponse.subList(commonResponse.size()-200,commonResponse.size());
                        getEntries2(last30);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<CountryLiveData>> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void callCountryLiveData3() {
        Call<List<CountryLiveData>> call = RetrofitClient.getInstance().getApi().getCountryLiveData(url2);
        call.enqueue(new Callback<List<CountryLiveData>>() {
            @Override
            public void onResponse(Call<List<CountryLiveData>> call, Response<List<CountryLiveData>> response) {
                if (response.code() == 200) {
                    List<CountryLiveData> commonResponse= response.body();
                    try {

                        NumberFormat myFormat = NumberFormat.getInstance();
                        myFormat.setGroupingUsed(true);
                        Integer n = commonResponse.size();
                        totaldeath.setText(String.valueOf(myFormat.format(Double.parseDouble(commonResponse.get(n-1).getCases()))));
                        List<CountryLiveData> last30= commonResponse.subList(commonResponse.size()-10,commonResponse.size());
                        getEntries3(last30);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<CountryLiveData>> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getEntries(List<CountryLiveData> countryLiveData) throws ParseException {
        lineEntries = new ArrayList<>();
        for (int i = 0; i < countryLiveData.size(); i++) {
            lineEntries.add(new Entry(i, Integer.parseInt(countryLiveData.get(i).getCases())));
        }
        lineChartSet(countryLiveData);
    }

    public void lineChartSet(List<CountryLiveData> countryLiveData) throws ParseException {

        lineDataSet = new LineDataSet(lineEntries,"Confirmed" );
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setColor(Color.parseColor("#ff6b6b"));
        lineDataSet.setCircleColor(Color.parseColor("#ff6b6b"));
        lineDataSet.setFillColor(Color.parseColor("#ff6b6b"));
        lineDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineDataSet.setValueTextSize(9f);
        lineDataSet.disableDashedHighlightLine();
        lineDataSet.setDrawFilled(false);
        lineDataSet.setValueFormatter(new Remover());
        lineDataSet.setLineWidth(2.0f);

        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
        lines.add(lineDataSet);


        List<String> date= new ArrayList<String>();
        for(int i =0; i<countryLiveData.size();i++){

            String formatdate= formatDate(countryLiveData.get(i).getDate());
            date.add(i,formatdate);

        }


        String[] xAxisData = new String[countryLiveData.size()];
        xAxisData = date.toArray(xAxisData);

        lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        xAxis.setGranularityEnabled(true);
        String[] finalXAxisData = xAxisData;
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return finalXAxisData[(int) value];
            }
        });

        lineChart.setData(lineData);
        lineChart.animateY(1000);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.getLegend().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
    }


    private void getEntries2(List<CountryLiveData> countryLiveData) throws ParseException {
        lineEntries2 = new ArrayList<>();
        for (int i = 0; i < countryLiveData.size(); i++) {
            lineEntries2.add(new Entry(i, Integer.parseInt(countryLiveData.get(i).getCases())));
        }
        lineChartSet2(countryLiveData);
    }

    public void lineChartSet2(List<CountryLiveData> countryLiveData) throws ParseException {

        lineDataSet2 = new LineDataSet(lineEntries2,"Recovered" );
        lineDataSet2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet2.setColor(Color.parseColor("#73a942"));
        lineDataSet2.setCircleColor(Color.parseColor("#73a942"));
        lineDataSet2.setFillColor(Color.parseColor("#73a942"));
        lineDataSet2.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineDataSet2.setValueTextSize(9f);
        lineDataSet2.disableDashedHighlightLine();
        lineDataSet2.setDrawFilled(false);
        lineDataSet2.setValueFormatter(new Remover());
        lineDataSet2.setLineWidth(2.0f);

        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
        lines.add(lineDataSet2);


        List<String> date= new ArrayList<String>();
        for(int i =0; i<countryLiveData.size();i++){

            String formatdate= formatDate(countryLiveData.get(i).getDate());
            date.add(i,formatdate);

        }


        String[] xAxisData = new String[countryLiveData.size()];
        xAxisData = date.toArray(xAxisData);

        lineData2 = new LineData();
        lineData2.addDataSet(lineDataSet2);
        XAxis xAxis = lineChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        xAxis.setGranularityEnabled(true);
        String[] finalXAxisData = xAxisData;
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return finalXAxisData[(int) value];
            }
        });

        lineChart2.setData(lineData2);
        lineChart2.animateY(1000);
        lineChart2.setDrawGridBackground(false);
        lineChart2.setDrawBorders(false);
        lineChart2.getDescription().setEnabled(false);
        lineChart2.getLegend().setEnabled(true);
        lineChart2.getLegend().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineChart2.setDoubleTapToZoomEnabled(false);
        lineChart2.getAxisLeft().setDrawGridLines(false);
        lineChart2.getAxisLeft().setDrawLabels(false);
        lineChart2.getAxisLeft().setDrawAxisLine(false);
        lineChart2.getXAxis().setDrawGridLines(false);
        lineChart2.getAxisRight().setDrawGridLines(false);
        lineChart2.getAxisRight().setDrawLabels(false);
        lineChart2.getAxisRight().setDrawAxisLine(false);
    }

    private void getEntries3(List<CountryLiveData> countryLiveData) throws ParseException {
        lineEntries3 = new ArrayList<>();
        for (int i = 0; i < countryLiveData.size(); i++) {
            lineEntries3.add(new Entry(i, Integer.parseInt(countryLiveData.get(i).getCases())));
        }
        lineChartSet3(countryLiveData);
    }

    public void lineChartSet3(List<CountryLiveData> countryLiveData) throws ParseException {

        lineDataSet3 = new LineDataSet(lineEntries3,"Death" );
        lineDataSet3.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet3.setColor(Color.parseColor("#c77dff"));
        lineDataSet3.setCircleColor(Color.parseColor("#c77dff"));
        lineDataSet3.setFillColor(Color.parseColor("#c77dff"));
        lineDataSet3.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineDataSet3.setValueTextSize(9f);
        lineDataSet3.disableDashedHighlightLine();
        lineDataSet3.setDrawFilled(false);
        lineDataSet3.setValueFormatter(new Remover());
        lineDataSet3.setLineWidth(2.0f);

        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
        lines.add(lineDataSet3);


        List<String> date= new ArrayList<String>();
        for(int i =0; i<countryLiveData.size();i++){

            String formatdate= formatDate(countryLiveData.get(i).getDate());
            date.add(i,formatdate);

        }


        String[] xAxisData = new String[countryLiveData.size()];
        xAxisData = date.toArray(xAxisData);

        lineData3 = new LineData();
        lineData3.addDataSet(lineDataSet3);
        XAxis xAxis = lineChart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        xAxis.setGranularityEnabled(true);
        String[] finalXAxisData = xAxisData;
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return finalXAxisData[(int) value];
            }
        });

        lineChart3.setData(lineData3);
        lineChart3.animateY(1000);
        lineChart3.setDrawGridBackground(false);
        lineChart3.setDrawBorders(false);
        lineChart3.getDescription().setEnabled(false);
        lineChart3.getLegend().setEnabled(true);
        lineChart3.getLegend().setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineChart3.setDoubleTapToZoomEnabled(false);
        lineChart3.getAxisLeft().setDrawGridLines(false);
        lineChart3.getAxisLeft().setDrawLabels(false);
        lineChart3.getAxisLeft().setDrawAxisLine(false);
        lineChart3.getXAxis().setDrawGridLines(false);
        lineChart3.getAxisRight().setDrawGridLines(false);
        lineChart3.getAxisRight().setDrawLabels(false);
        lineChart3.getAxisRight().setDrawAxisLine(false);
    }


    public class Remover extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return "" + ((int) value);
        }
    }

    private String formatDate(String date_string) throws ParseException {
        String formatedDate="";
        String pattern = "dd-MM-yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        SimpleDateFormat inFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = sdf.parse(date_string);
        formatedDate=simpleDateFormat.format(date);
        return formatedDate;
    }

}