package com.windhans.client.forworld.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.windhans.client.forworld.Model.EnuiryStatusDetailModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.my_library.DateTimeFormat;

import java.util.List;

public class EnquiryStatusDeatilAdapter extends RecyclerView.Adapter<EnquiryStatusDeatilAdapter.MyViewHolder>{
    Context context;
    List<EnuiryStatusDetailModel> leadDetailsList;
    View view;



    public EnquiryStatusDeatilAdapter(FragmentActivity activityLeadDetails, List<EnuiryStatusDetailModel> leadDetailsList) {
        this.context=activityLeadDetails;
        this.leadDetailsList=leadDetailsList;
    }

  /*  public LeadDeatilAdapter(FragmentActivity activity, List<LeadDetails> leadDetailsList) {
        this.context=activity;
        this.leadDetailsList=leadDetailsList;
    }
*/
    @NonNull
    @Override
    public EnquiryStatusDeatilAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_lead_details1, viewGroup, false);
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lead_detail_status1, viewGroup, false);
      //  retView = inflater.inflate(R.layout.lead_detail_status1, container, false);
        EnquiryStatusDeatilAdapter.MyViewHolder holder = new EnquiryStatusDeatilAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EnquiryStatusDeatilAdapter.MyViewHolder myViewHolder, int position) {
        EnuiryStatusDetailModel model=leadDetailsList.get(position);
        myViewHolder.text_laedReason.setText(model.getLead_reason());
        String status=leadDetailsList.get(position).getStatus();
        if(status.equals("0"))
        {
            myViewHolder.textView_status.setText("Pending");
            myViewHolder.card_view_pickup.setCardBackgroundColor(context.getResources().getColor(R.color.apptheme));

        }
        if(status.equals("1"))
        {
            myViewHolder.textView_status.setText("Completed");
            myViewHolder.card_view_pickup.setCardBackgroundColor(context.getResources().getColor(R.color.green_a));
        }
        if(status.equals("2"))
        {
            myViewHolder.textView_status.setText("Progress");
            myViewHolder.card_view_pickup.setCardBackgroundColor(context.getResources().getColor(R.color.apptheme));
        }
        if(status.equals("3"))
        {
            myViewHolder.textView_status.setText("Rejected");
            myViewHolder.card_view_pickup.setCardBackgroundColor(context.getResources().getColor(R.color.red_a));
        }
        String date=leadDetailsList.get(position).getCreated_at();

        myViewHolder.textView_month.setText(DateTimeFormat.getDate1(date));
        myViewHolder.textView_date.setText(DateTimeFormat.getDate2(date));

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
      MyTextView_Poppins_Bold text_laedReason,textView_status,textView_month,textView_date;
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

