package com.example.activity6;

import  androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity6.api.ApiUtilities;
import com.example.activity6.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView text_total_confirmed, text_total_active, text_total_recovered, text_total_death, text_total_tests;
    TextView text_today_confirmed, text_today_recovered, text_today_death;
    TextView text_date;
    PieChart pieChart;

    private List<CountryData> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        text_total_confirmed = findViewById(R.id.txt_totalconfirmed);
        text_total_active = findViewById(R.id.txt_totalactive);
        text_total_recovered = findViewById(R.id.txt_totalrecovered);
        text_total_death = findViewById(R.id.txt_totaldeath);
        text_total_tests = findViewById(R.id.txt_totaltest);
        text_today_confirmed = findViewById(R.id.txtview_todayconfirmed);
        text_today_recovered = findViewById(R.id.txtview_todayrecovered);
        text_today_death = findViewById(R.id.txtview_todaydeaths);
        text_date= findViewById(R.id.txtview_date);
        pieChart= findViewById(R.id.piechart);

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for(int i = 0;i<list.size(); i++){
                            if(list.get(i).getCountry().equals("Philippines")){
                                int total_confirm  = Integer.parseInt(list.get(i).getCases());
                                int total_death = Integer.parseInt(list.get(i).getDeaths());
                                int total_recovered = Integer.parseInt(list.get(i).getRecovered());
                                int total_active = Integer.parseInt(list.get(i).getActive());
                                int total_test = Integer.parseInt(list.get(i).getTests());

                                int today_confirm = Integer.parseInt(list.get(i).getTodayCases());
                                int today_death = Integer.parseInt(list.get(i).getTodayDeaths());
                                int today_recovered = Integer.parseInt(list.get(i).getTodayRecovered());

                                text_total_confirmed.setText(NumberFormat.getInstance().format(total_confirm));
                                text_total_active.setText(NumberFormat.getInstance().format(total_active));
                                text_total_recovered.setText(NumberFormat.getInstance().format(total_recovered));
                                text_total_death.setText(NumberFormat.getInstance().format(total_death));
                                text_total_tests.setText(NumberFormat.getInstance().format(total_test));

                                text_today_confirmed.setText(NumberFormat.getInstance().format(today_confirm));
                                text_today_recovered.setText(NumberFormat.getInstance().format(today_recovered));
                                text_today_death.setText(NumberFormat.getInstance().format(today_death));

                                setText(list.get(i).getUpdated());
                                pieChart.addPieSlice(new PieModel("Confirmed", total_confirm, Color.parseColor("#08E847")));
                                pieChart.addPieSlice(new PieModel("Active", total_active, Color.parseColor("#E80808")));
                                pieChart.addPieSlice(new PieModel("Recovered", total_recovered, Color.parseColor("#FFEB3B")));
                                pieChart.addPieSlice(new PieModel("Death", total_death, Color.parseColor("#0C0C0C")));

                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
                

    }
    private void setText(String updated){
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milliseconds = Long.parseLong(updated);

        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        text_date.setText("Updated at"+format.format(calendar.getTime()));
    }
}