package com.windhans.client.forworld.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ssforword.Model.GenrateEnquiryList;
import com.windhans.client.forworld.Model.MyDiscountRequest;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.DateTimeFormat;
import com.windhans.client.forworld.my_library.MyConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MyVendorDiscountRequstListAdapter extends RecyclerView.Adapter<MyVendorDiscountRequstListAdapter.VisitHolder> {

    private List<MyDiscountRequest> myDiscountRequestArrayList;

    private View itemView;
    private Context context;
    private ProgressDialog progressDialog;

    public class VisitHolder extends RecyclerView.ViewHolder {
        public TextView tv_requestId, tv_business_name,tv_requestAmount,tv_discountAmount,
                tv_product_name,tv_businessMobile,tv_status,tv_approval_date;
        private ImageView iv_delete_user;

        public VisitHolder(View view) {
            super(view);
            context = view.getContext();
            tv_requestId = (TextView)view.findViewById(R.id.tv_requestId);
            tv_business_name = (TextView)view.findViewById(R.id.tv_business_name);
            tv_requestAmount = (TextView)view.findViewById(R.id.tv_requestAmount);
            tv_discountAmount = (TextView)view.findViewById(R.id.tv_discountAmount);
            tv_product_name = (TextView)view.findViewById(R.id.tv_product_name);
            tv_businessMobile = (TextView)view.findViewById(R.id.tv_businessMobile);
            tv_status = (TextView)view.findViewById(R.id.tv_status);
            tv_approval_date = (TextView)view.findViewById(R.id.tv_approval_date);

        }
    }

    public MyVendorDiscountRequstListAdapter(List<MyDiscountRequest> myDiscountRequestArrayList) {
        this.myDiscountRequestArrayList = myDiscountRequestArrayList;
    }

    @Override
    public MyVendorDiscountRequstListAdapter.VisitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_my_discount_request, parent, false);

        return new MyVendorDiscountRequstListAdapter.VisitHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyVendorDiscountRequstListAdapter.VisitHolder holder, final int position) {
        final MyDiscountRequest myDiscountRequest = myDiscountRequestArrayList.get(position);


        holder.tv_requestId.setText(myDiscountRequest.getQr_request_id());
        holder.tv_business_name.setText(myDiscountRequest.getBusiness_name());
        holder.tv_requestAmount.setText(Constants.rs+myDiscountRequest.getTotal_amount());

        //holder.tv_discountAmount.setText(myDiscountRequest.getDiscount_amount());

        if(myDiscountRequest.getDiscount_amount().equals("null"))
            holder.tv_discountAmount.setText("--");
        else
            holder.tv_discountAmount.setText(Constants.rs+myDiscountRequest.getDiscount_amount());

        holder.tv_product_name.setText(myDiscountRequest.getProduct_title());
        holder.tv_businessMobile.setText(myDiscountRequest.getContact_mobile());

        if(myDiscountRequest.getStatus().equals("1")) {
            holder.tv_status.setText("Completed");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green_new));
        }
        else {
            holder.tv_status.setText("Pending");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.red_new));;
        }
        if(myDiscountRequest.getApproved_at().equals("null"))
            holder.tv_approval_date.setText("--");
        else
            holder.tv_approval_date.setText(DateTimeFormat.getDate1_9(myDiscountRequest.getApproved_at()));

    }

    @Override
    public int getItemCount() {
        return myDiscountRequestArrayList.size();
    }

    public interface CRUDAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/deleteLead")
        Call<ResponseBody> checkTYPE(
                @Field("lead_id") String lead_id
//                @Field("user_id") String user_id

        );
    }

    private void DeleteTYPE(String lead_id, final int posotion) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        progressDialog.show();


        MyVendorDiscountRequstListAdapter.CRUDAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(MyVendorDiscountRequstListAdapter.CRUDAPI.class);
        Call<ResponseBody> result = (Call<ResponseBody>) api.checkTYPE(
                lead_id
        );
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();

                    Log.d("user_id", "response=> " + output);
                    Log.d("Response", "response=> " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = Boolean.parseBoolean(jsonObject.getString("result"));
                    String reason = jsonObject.getString("reason");

                    if (res) {
                        myDiscountRequestArrayList.remove(posotion);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d("Retrofit Error:",t.getMessage());
                //progressDialog.dismiss();
            }
        });
    }

}

