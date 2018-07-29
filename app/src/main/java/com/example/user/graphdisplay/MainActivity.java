package com.example.user.graphdisplay;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LineChart lineChart;
    LineChart lineChart2;
    ArrayList<String> inputNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawLineChart();
        get_Jason();
        drawBasedOnJson();

    }
    public void get_Jason(){
        String json = null;
        try {
            InputStream is = getAssets().open("data.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

            JSONArray jsonArr = new JSONArray(json);

            for(int i=0; i<jsonArr.length(); i++){
                JSONObject jsonObj = jsonArr.getJSONObject(i);

                inputNumbers.add(jsonObj.getString("value"));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void drawLineChart(){
        lineChart = (LineChart) findViewById(R.id.lineChart);

        ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Entry> yAxesCos = new ArrayList<>();
        ArrayList<Entry> yAxesSin = new ArrayList<>();

        double x = 0;
        int numDataPoints = 1000;

        for(int i=0; i<numDataPoints; i++){
            float sinFunciton = Float.parseFloat(String.valueOf(Math.sin(x)));
            float cosFunciton = Float.parseFloat(String.valueOf(Math.cos(x)));
            x = x + 0.1;
            xAxes.add(i, String.valueOf(x));
            yAxesSin.add(new Entry(i, sinFunciton));
            yAxesCos.add(new Entry(i, cosFunciton));
        }
        String[] xaxes = new String[xAxes.size()];

        for(int i=0; i<xAxes.size(); i++){
            xaxes[i] = xAxes.get(i).toString();
        }
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(yAxesCos, "cos");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.BLUE);
        LineDataSet lineDataSet2 = new LineDataSet(yAxesSin, "sin");
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(Color.RED);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);

        lineChart.setData(new LineData(lineDataSets));
        lineChart.setVisibleXRangeMaximum(65f);
    }

    private void drawBasedOnJson(){
        DataPoint[] datapoints = new DataPoint[inputNumbers.size()];

        for(int i=0; i<inputNumbers.size(); i++){
            datapoints[i] = new DataPoint(i, Integer.parseInt(inputNumbers.get(i)));
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(datapoints);
        graph.addSeries(series);
    }

}
