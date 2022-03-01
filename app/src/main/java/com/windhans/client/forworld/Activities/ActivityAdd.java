package com.windhans.client.forworld.Activities;

import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.windhans.client.forworld.Adapter.AddAdpter;
import com.windhans.client.forworld.Model.ModelAdd;
import com.windhans.client.forworld.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ActivityAdd extends AppCompatActivity {

    EditText edt_date, edt_days;
    Button btn_add, btn_clear;
    RecyclerView recyclerView;
    Calendar myCalendar;
    List<ModelAdd> addList = new ArrayList<>();
    SimpleDateFormat format;
    AddAdpter addAdpter;
    ModelAdd modelAdd;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edt_date = findViewById(R.id.edt_date);
        edt_days = findViewById(R.id.edt_days);
        btn_add = findViewById(R.id.btn_add);
        recyclerView = findViewById(R.id.recyclerView);

        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edt_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ActivityAdd.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        addAdpter = new AddAdpter(ActivityAdd.this, addList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(addAdpter);
        addAdpter.notifyDataSetChanged();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    setData();

                } else {
                    setData1();
                }


            }
        });
        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addList.clear();
                recyclerView.setAdapter(addAdpter);
                addAdpter.notifyDataSetChanged();
            }
        });
    }

    private void setData1() {
        int last_index = addList.size();
        Log.d("size", "setData1: " + addList.size());
        String startDate1 = addList.get(last_index - 1).getEndDate();
        //  String startDate1=modelAdd.getEndDate();
        Log.d("Sdate1", "onClick: " + startDate1);
        int days1 = Integer.parseInt(edt_days.getText().toString());
        String end_date1 = addDay(startDate1, days1);
        Log.d("Edate1", "onClick: " + end_date1);
        String total_days1 = edt_days.getText().toString();
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        Log.d("date", "setData1: "+formattedDate);
        dateBetween(startDate1,end_date1);
        int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"), end_date1,formattedDate);
        /*modelAdd.setStartDate(startDate1);
        modelAdd.setEndDate(end_date1);
        modelAdd.setTotal(total_days1);
        modelAdd.setRemaining(String.valueOf(dateDifference));*/
        int status = (last_index);
        String status1;
        Log.d("status", "setData1: "+status);
        if(status==1)
        {
            status1="holidays";
        }
        else {
            if (status % 2 == 0) {
                status1 = "Working";
            } else {
                status1 = "holidays";
            }
        }

        addList.add(new ModelAdd(startDate1, end_date1, total_days1, String.valueOf(dateDifference), status1));
        // addList.add(modelAdd);

        recyclerView.setAdapter(addAdpter);
        addAdpter.notifyDataSetChanged();
    }

    private void setData() {
        flag = 1;
        String startDate = edt_date.getText().toString();
        Log.d("Sdate", "onClick: " + startDate);

        int days = Integer.parseInt(edt_days.getText().toString());
        String end_date = addDay(startDate, days);
        String total_days = edt_days.getText().toString();
        Log.d("edate", "onClick: " + end_date);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);

       // int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"), startDate, end_date);
        dateBetween(startDate,end_date);

        int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"),end_date,formattedDate);
        Log.d("Ddate", "onClick: " + dateDifference);
        String status = "Working";

    /*    modelAdd.setStartDate(startDate);
        modelAdd.setEndDate(end_date);
        modelAdd.setTotal(total_days);
        modelAdd.setRemaining(String.valueOf(dateDifference));
        addList.add(modelAdd);*/
        addList.add(new ModelAdd(startDate, end_date, total_days, String.valueOf(dateDifference), status));
        recyclerView.setAdapter(addAdpter);
        addAdpter.notifyDataSetChanged();
        Log.d("addList", "setData: "+addList.size());
    }


    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
          //  return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
            return TimeUnit.DAYS.convert( format.parse(oldDate).getTime()-format.parse(newDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edt_date.setText(sdf.format(myCalendar.getTime()));

    }

    public static String addDay(String oldDate, int numberOfDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_YEAR, numberOfDays);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date newDate = new Date(c.getTimeInMillis());
        String resultDate = dateFormat.format(newDate);
        return resultDate;
    }

    public void dateBetween(String startDate, String end_date) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String oeStartDateStr = startDate;
        String oeEndDateStr = end_date;

        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);

        oeStartDateStr = oeStartDateStr.concat(year.toString());
        oeEndDateStr = oeEndDateStr.concat(year.toString());
        Date startDate1 = null,endDate1=null;
        try {
            startDate1= sdf.parse(oeStartDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
             endDate1 = sdf.parse(oeEndDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d = new Date();
        Log.d("date", "dateBetween: "+d);
        String currDt = sdf.format(d);


        if((d.after(startDate1) && (d.before(endDate1))) || (currDt.equals(sdf.format(startDate1)) ||currDt.equals(sdf.format(endDate1)))){
           // System.out.println("Date is between 1st april to 14th nov...");
            Toast.makeText(ActivityAdd.this, "Date is between ...\"", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ActivityAdd.this, "Date is not between...\"", Toast.LENGTH_SHORT).show();
            //System.out.println("Date is not between 1st april to 14th nov...");
        }
    }
}
