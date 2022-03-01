package com.windhans.client.forworld.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.windhans.client.forworld.Model.LeadDetailModel;
import com.windhans.client.forworld.Model.LeadDetailsNew;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class LeadDeatilAdapter extends RecyclerView.Adapter<LeadDeatilAdapter.MyViewHolder>{
    Context context;
    List<LeadDetailsNew> leadDetailsList = new ArrayList<>();
    View view;
    private String status="", prupose="";


    public LeadDeatilAdapter(FragmentActivity activityLeadDetails, List<LeadDetailsNew> leadDetailsList, String LeadStatus,
                             String prupose) {

        this.context=activityLeadDetails;
        this.leadDetailsList=leadDetailsList;
        this.status=LeadStatus;
        this.prupose=prupose;
    }

  /*  public LeadDeatilAdapter(FragmentActivity activity, List<LeadDetails> leadDetailsList) {
        this.context=activity;
        this.leadDetailsList=leadDetailsList;
    }
*/
    @NonNull
    @Override
    public LeadDeatilAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_lead_details1, viewGroup, false);
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lead_detail_status1, viewGroup, false);
      //  retView = inflater.inflate(R.layout.lead_detail_status1, container, false);
        LeadDeatilAdapter.MyViewHolder holder = new LeadDeatilAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeadDeatilAdapter.MyViewHolder myViewHolder, int position) {
        LeadDetailsNew model = leadDetailsList.get(position);

        myViewHolder.text_laedReason.setText(leadDetailsList.get(position).getLd_reason());


        if(leadDetailsList.get(position).getLd_status().equals("0"))
        {
            myViewHolder.textView_status.setText("Pending");
            myViewHolder.card_view_pickup.setCardBackgroundColor(context.getResources().getColor(R.color.apptheme));

        }
        if(leadDetailsList.get(position).getLd_status().equals("1"))
        {
            myViewHolder.textView_status.setText("Completed");
            myViewHolder.card_view_pickup.setCardBackgroundColor(context.getResources().getColor(R.color.green_a));
        }
        if(leadDetailsList.get(position).getLd_status().equals("2"))
        {
            myViewHolder.textView_status.setText("Progress");
            myViewHolder.card_view_pickup.setCardBackgroundColor(context.getResources().getColor(R.color.apptheme));
        }
        if(leadDetailsList.get(position).getLd_status().equals("3"))
        {
            myViewHolder.textView_status.setText("Rejected");
            myViewHolder.card_view_pickup.setCardBackgroundColor(context.getResources().getColor(R.color.red_a));
        }


        String date=leadDetailsList.get(position).getCreated_at();

        myViewHolder.textView_month.setText(DateTimeFormat.getDate1_1(date));
        myViewHolder.textView_date.setText(DateTimeFormat.getDate2_1(date));

       /* DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd ");
        DateFormat outputFormat1 = new SimpleDateFormat("MMM ");
        String inputDateStr=date;
        Date date1 = null;
        try {
            date1 = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date1);
        Log.d("date", "onBindViewHolder: "+outputDateStr);*/
    }

    @Override
    public int getItemCount() {
        return leadDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

    //  TextView edt_reason1;
      TextView text_laedReason,textView_status,textView_month,textView_date;
      CardView card_view_pickup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            text_laedReason=itemView.findViewById(R.id.text_laedReason);
            textView_status=itemView.findViewById(R.id.textView_status);
            textView_month=itemView.findViewById(R.id.textView_month);
            textView_date=itemView.findViewById(R.id.textView_date);
            card_view_pickup=itemView.findViewById(R.id.card_view_pickup);

        }
    }
}

