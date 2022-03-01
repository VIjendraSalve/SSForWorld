package com.windhans.client.forworld.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ssforword.Model.GenrateEnquiryList;

import com.windhans.client.forworld.R;
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

public class EnquiryAdapter extends RecyclerView.Adapter<EnquiryAdapter.VisitHolder> {

    private List<GenrateEnquiryList> enquiryListsList;

    private View itemView;
    private Context context;
    private ProgressDialog progressDialog;

    public class VisitHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_address,tv_mobile,tv_city;
        private ImageView iv_delete_user;

        public VisitHolder(View view) {
            super(view);
            context = view.getContext();
            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_address = (TextView)view.findViewById(R.id.tv_address);
            tv_mobile = (TextView)view.findViewById(R.id.tv_mobile);
            tv_city = (TextView)view.findViewById(R.id.tv_city);
            // iv_delete_user = (ImageView) view.findViewById(R.id.iv_delete_user);

        }
    }

    public EnquiryAdapter(List<GenrateEnquiryList> enquiryListsList) {
        this.enquiryListsList = enquiryListsList;
    }

    @Override
    public EnquiryAdapter.VisitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enquriy_item_list_view, parent, false);

        return new EnquiryAdapter.VisitHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(EnquiryAdapter.VisitHolder holder, final int position) {
        final GenrateEnquiryList speciality = enquiryListsList.get(position);


        holder.tv_name.setText(speciality.getName());
        holder.tv_address.setText(speciality.getPurpose());
        holder.tv_mobile.setText(speciality.getContact());
        holder.tv_city.setText(speciality.getCity());

//        holder.iv_delete_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setMessage("Are you sure you want to delete?");
//                alertDialogBuilder.setCancelable(false)
//                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                DeleteTYPE(speciality.getId(),position);
//                            }
//                        })
//                        .setNegativeButton("NO",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                AlertDialog alert = alertDialogBuilder.create();
//                //alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
//                alert.show();
//            }
//        });

        // Toast.makeText(itemView.getContext(), ""+moviesList.get(position).getTitle(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return enquiryListsList.size();
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


        EnquiryAdapter.CRUDAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(EnquiryAdapter.CRUDAPI.class);
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
                        enquiryListsList.remove(posotion);
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

